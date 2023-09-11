package com.nl.bank.digibank.repo;

import com.nl.bank.digibank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);
    Boolean existsByUsernameIgnoreCase(String username);
}
