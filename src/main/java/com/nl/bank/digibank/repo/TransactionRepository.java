package com.nl.bank.digibank.repo;

import com.nl.bank.digibank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Custom query methods, if needed
}
