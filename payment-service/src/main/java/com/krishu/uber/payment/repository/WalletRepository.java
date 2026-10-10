package com.krishu.uber.payment.repository;

import com.krishu.uber.payment.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Spring Data JPA repository for Wallet entities
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    // Retrieve digital wallet belonging to a specific user
    Optional<Wallet> findByUserId(Long userId);

    // Check if a wallet is already created for a specific user
    boolean existsByUserId(Long userId);
}
