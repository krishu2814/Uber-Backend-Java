package com.krishu.uber.payment.repository;

import com.krishu.uber.payment.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Spring Data JPA repository for WalletTransaction entities
@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    // Retrieve ledger history for a wallet, ordered with the most recent transaction first
    List<WalletTransaction> findByWalletIdOrderByTimestampDesc(Long walletId);
}
