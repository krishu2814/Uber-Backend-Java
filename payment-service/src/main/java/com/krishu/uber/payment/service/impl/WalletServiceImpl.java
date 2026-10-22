package com.krishu.uber.payment.service.impl;

import com.krishu.uber.payment.dto.*;
import com.krishu.uber.payment.entity.Wallet;
import com.krishu.uber.payment.entity.WalletTransaction;
import com.krishu.uber.payment.enums.TransactionType;
import com.krishu.uber.payment.exception.BadRequestException;
import com.krishu.uber.payment.exception.ResourceNotFoundException;
import com.krishu.uber.payment.repository.WalletRepository;
import com.krishu.uber.payment.repository.WalletTransactionRepository;
import com.krishu.uber.payment.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Core implementation of wallet management, balance top-ups, and double-entry settlements
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletServiceImpl(WalletRepository walletRepository,
                             WalletTransactionRepository walletTransactionRepository) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Override
    @Transactional
    public WalletResponseDto createWallet(Long userId) {
        if (userId == null || userId <= 0) {
            throw new BadRequestException("A valid positive User ID is required to create a wallet.");
        }

        // Return existing wallet if already initialized for this user
        if (walletRepository.existsByUserId(userId)) {
            Wallet existing = walletRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user ID: " + userId));
            return mapToResponseDto(existing, "Wallet is already active for user ID: " + userId);
        }

        // Create new wallet with ₹100 Welcome Bonus
        Wallet wallet = new Wallet(userId, 100.0);
        wallet = walletRepository.save(wallet);

        // Record immutable ledger entry for welcome credit
        WalletTransaction welcomeTx = new WalletTransaction(
                wallet.getId(),
                null,
                100.0,
                TransactionType.CREDIT,
                "Welcome Bonus Credit",
                wallet.getBalance()
        );
        walletTransactionRepository.save(welcomeTx);

        return mapToResponseDto(wallet, "Wallet created successfully with ₹100 Welcome Bonus!");
    }

    @Override
    public WalletResponseDto getWalletByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user ID: " + userId));
        return mapToResponseDto(wallet, "Wallet retrieved successfully");
    }

    @Override
    public WalletResponseDto getWalletById(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with ID: " + walletId));
        return mapToResponseDto(wallet, "Wallet retrieved successfully");
    }

    @Override
    @Transactional
    public WalletResponseDto addMoney(AddMoneyRequest request) {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new BadRequestException("Top-up amount must be greater than zero.");
        }

        Wallet wallet = walletRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user ID: " + request.getUserId()));

        double newBalance = round(wallet.getBalance() + request.getAmount());
        wallet.setBalance(newBalance);
        wallet = walletRepository.save(wallet);

        // Log ledger credit transaction
        WalletTransaction creditTx = new WalletTransaction(
                wallet.getId(),
                null,
                round(request.getAmount()),
                TransactionType.CREDIT,
                "Wallet Top-up",
                newBalance
        );
        walletTransactionRepository.save(creditTx);

        return mapToResponseDto(wallet, "Added ₹" + round(request.getAmount()) + " successfully. New balance: ₹" + newBalance);
    }

    @Override
    @Transactional
    public SettlementResponseDto settleRide(SettleRideRequest request) {
        Double fare = round(request.getFareAmount());
        if (fare <= 0) {
            throw new BadRequestException("Fare amount must be greater than zero.");
        }

        // 1. Fetch Rider Wallet
        Wallet riderWallet = walletRepository.findByUserId(request.getRiderUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Rider wallet not found for user ID: " + request.getRiderUserId() + ". Please initialize wallet first."));

        // 2. Fetch or Auto-initialize Driver Wallet
        Wallet driverWallet = walletRepository.findByUserId(request.getDriverUserId())
                .orElseGet(() -> {
                    Wallet newDriverWallet = new Wallet(request.getDriverUserId(), 0.0);
                    return walletRepository.save(newDriverWallet);
                });

        // 3. Verify Rider Wallet Balance
        if (riderWallet.getBalance() < fare) {
            throw new BadRequestException("Insufficient balance in rider wallet. Available: ₹"
                    + riderWallet.getBalance() + ", Required: ₹" + fare + ". Please add money to wallet.");
        }

        // 4. Debit Fare from Rider
        double riderNewBalance = round(riderWallet.getBalance() - fare);
        riderWallet.setBalance(riderNewBalance);
        walletRepository.save(riderWallet);

        WalletTransaction riderDebitTx = new WalletTransaction(
                riderWallet.getId(),
                request.getRideId(),
                fare,
                TransactionType.DEBIT,
                "Trip Fare Paid for Ride #" + request.getRideId(),
                riderNewBalance
        );
        walletTransactionRepository.save(riderDebitTx);

        // 5. Driver Earnings (80%) and Platform Commission (20%)
        double driverEarnings = round(fare * 0.80);
        double platformCommission = round(fare - driverEarnings);

        // Credit 80% driver earnings
        double driverNewBalance = round(driverWallet.getBalance() + driverEarnings);
        driverWallet.setBalance(driverNewBalance);
        walletRepository.save(driverWallet);

        WalletTransaction driverCreditTx = new WalletTransaction(
                driverWallet.getId(),
                request.getRideId(),
                driverEarnings,
                TransactionType.CREDIT,
                "Trip Earnings (80% of ₹" + fare + ") for Ride #" + request.getRideId(),
                driverNewBalance
        );
        walletTransactionRepository.save(driverCreditTx);

        // Credit 20% commission to Platform Revenue Wallet (userId = 0)
        Wallet platformWallet = walletRepository.findByUserId(0L)
                .orElseGet(() -> walletRepository.save(new Wallet(0L, 0.0)));
        double platformNewBalance = round(platformWallet.getBalance() + platformCommission);
        platformWallet.setBalance(platformNewBalance);
        walletRepository.save(platformWallet);

        WalletTransaction platformCreditTx = new WalletTransaction(
                platformWallet.getId(),
                request.getRideId(),
                platformCommission,
                TransactionType.CREDIT,
                "Platform Commission (20% of ₹" + fare + ") for Ride #" + request.getRideId(),
                platformNewBalance
        );
        walletTransactionRepository.save(platformCreditTx);

        return new SettlementResponseDto(
                request.getRideId(),
                fare,
                request.getRiderUserId(),
                riderNewBalance,
                request.getDriverUserId(),
                driverNewBalance,
                "SUCCESS",
                "Ride #" + request.getRideId() + " fare of ₹" + fare + " settled successfully (₹" + driverEarnings + " to driver, ₹" + platformCommission + " platform commission)."
        );
    }

    @Override
    public List<TransactionDto> getTransactions(Long walletId) {
        if (!walletRepository.existsById(walletId)) {
            throw new ResourceNotFoundException("Wallet not found with ID: " + walletId);
        }
        return walletTransactionRepository.findByWalletIdOrderByTimestampDesc(walletId)
                .stream()
                .map(this::mapToTransactionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> getTransactionsByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user ID: " + userId));
        return getTransactions(wallet.getId());
    }

    private WalletResponseDto mapToResponseDto(Wallet wallet, String message) {
        return new WalletResponseDto(
                wallet.getId(),
                wallet.getUserId(),
                wallet.getBalance(),
                wallet.getCreatedAt(),
                message
        );
    }

    private TransactionDto mapToTransactionDto(WalletTransaction tx) {
        return new TransactionDto(
                tx.getId(),
                tx.getWalletId(),
                tx.getRideId(),
                tx.getAmount(),
                tx.getType(),
                tx.getDescription(),
                tx.getBalanceAfter(),
                tx.getTimestamp()
        );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
