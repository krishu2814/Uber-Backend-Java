package com.krishu.uber.payment.service;

import com.krishu.uber.payment.dto.AddMoneyRequest;
import com.krishu.uber.payment.dto.SettleRideRequest;
import com.krishu.uber.payment.dto.SettlementResponseDto;
import com.krishu.uber.payment.dto.WalletResponseDto;
import com.krishu.uber.payment.entity.Wallet;
import com.krishu.uber.payment.entity.WalletTransaction;
import com.krishu.uber.payment.exception.BadRequestException;
import com.krishu.uber.payment.repository.WalletRepository;
import com.krishu.uber.payment.repository.WalletTransactionRepository;
import com.krishu.uber.payment.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletTransactionRepository walletTransactionRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet riderWallet;
    private Wallet driverWallet;

    @BeforeEach
    void setUp() {
        riderWallet = new Wallet(1L, 200.0);
        riderWallet.setId(10L);

        driverWallet = new Wallet(2L, 50.0);
        driverWallet.setId(20L);
    }

    @Test
    void testCreateWallet_NewUser_SuccessWithBonus() {
        when(walletRepository.existsByUserId(1L)).thenReturn(false);
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> {
            Wallet w = invocation.getArgument(0);
            w.setId(10L);
            return w;
        });

        WalletResponseDto response = walletService.createWallet(1L);

        assertNotNull(response);
        assertEquals(100.0, response.getBalance());
        verify(walletTransactionRepository, times(1)).save(any(WalletTransaction.class));
    }

    @Test
    void testAddMoney_Success() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(riderWallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(riderWallet);

        AddMoneyRequest request = new AddMoneyRequest(1L, 150.0);
        WalletResponseDto response = walletService.addMoney(request);

        assertNotNull(response);
        assertEquals(350.0, response.getBalance());
        verify(walletTransactionRepository, times(1)).save(any(WalletTransaction.class));
    }

    @Test
    void testSettleRide_80_20_Split_Success() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(riderWallet));
        when(walletRepository.findByUserId(2L)).thenReturn(Optional.of(driverWallet));
        when(walletRepository.findByUserId(0L)).thenReturn(Optional.of(new Wallet(0L, 0.0)));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(inv -> inv.getArgument(0));

        SettleRideRequest request = new SettleRideRequest(101L, 1L, 2L, 100.0);
        SettlementResponseDto settlement = walletService.settleRide(request);

        assertNotNull(settlement);
        assertEquals("SUCCESS", settlement.getStatus());
        assertEquals(100.0, settlement.getRiderRemainingBalance()); // 200 - 100 = 100
        assertEquals(130.0, settlement.getDriverNewBalance());      // 50 + 80 = 130
        verify(walletTransactionRepository, times(3)).save(any(WalletTransaction.class)); // rider debit, driver credit, platform credit
    }

    @Test
    void testSettleRide_InsufficientFunds_ThrowsBadRequest() {
        riderWallet.setBalance(30.0);
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(riderWallet));
        when(walletRepository.findByUserId(2L)).thenReturn(Optional.of(driverWallet));

        SettleRideRequest request = new SettleRideRequest(101L, 1L, 2L, 100.0);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> walletService.settleRide(request));
        assertTrue(ex.getMessage().contains("Insufficient balance"));
    }
}
