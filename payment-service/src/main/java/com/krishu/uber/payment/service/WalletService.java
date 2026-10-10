package com.krishu.uber.payment.service;

import com.krishu.uber.payment.dto.AddMoneyRequest;
import com.krishu.uber.payment.dto.SettleRideRequest;
import com.krishu.uber.payment.dto.SettlementResponseDto;
import com.krishu.uber.payment.dto.TransactionDto;
import com.krishu.uber.payment.dto.WalletResponseDto;

import java.util.List;

// Service interface outlining business contracts for wallet and ledger management
public interface WalletService {

    // Initialize a digital wallet with ₹100 welcome bonus for a user
    WalletResponseDto createWallet(Long userId);

    // Retrieve wallet by owner's user ID
    WalletResponseDto getWalletByUserId(Long userId);

    // Retrieve wallet by internal wallet primary key
    WalletResponseDto getWalletById(Long walletId);

    // Add funds / top-up wallet balance
    WalletResponseDto addMoney(AddMoneyRequest request);

    // Perform atomic double-entry settlement for a completed ride fare
    SettlementResponseDto settleRide(SettleRideRequest request);

    // Retrieve complete immutable ledger history for a wallet
    List<TransactionDto> getTransactions(Long walletId);

    // Retrieve transaction ledger history using user ID directly
    List<TransactionDto> getTransactionsByUserId(Long userId);
}
