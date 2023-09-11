package com.nl.bank.digibank.service;

import com.nl.bank.digibank.common.IBANGenerator;
import com.nl.bank.digibank.dto.AccountOverview;
import com.nl.bank.digibank.model.Account;
import com.nl.bank.digibank.model.Customer;
import com.nl.bank.digibank.model.Transaction;
import com.nl.bank.digibank.repo.AccountRepository;
import com.nl.bank.digibank.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public AccountOverview getAccountOverview(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        AccountOverview accountOverview = new AccountOverview();
        accountOverview.setAccountNumber(account.getAccountNumber());
        accountOverview.setAccountType(account.getAccountType());
        accountOverview.setBalance(account.getBalance());
        accountOverview.setCurrency(account.getCurrency());
        return accountOverview;
    }

    public Account createAccount(Customer customer) {
        Account account = new Account();
        account.setAccountNumber(IBANGenerator.generateNLIBAN());
        account.setAccountType("SAVINGS");
        account.setBalance(BigDecimal.valueOf(10000));
        account.setCurrency("EUR");
        account.setCustomer(customer);
        return accountRepository.save(account);
    }

    @Transactional
    public String transferMoney(String originatorAccountNumber, String counterpartyAccountNumber, BigDecimal amount) {
        // Retrieve sender and receiver accounts
        Account senderAccount = accountRepository.findByAccountNumber(originatorAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        Account receiverAccount = accountRepository.findByAccountNumber(counterpartyAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

        // Check if sender has sufficient balance
        BigDecimal senderBalance = senderAccount.getBalance();
        if (senderBalance.compareTo(amount) < 0) {
            return "Insufficient balance in sender account.";
        }

        // Deduct the amount from the sender account
        senderAccount.setBalance(senderBalance.subtract(amount));

        // Update the sender account in the database
        accountRepository.save(senderAccount);

        // Add the amount to the receiver account
        BigDecimal receiverBalance = receiverAccount.getBalance();
        receiverAccount.setBalance(receiverBalance.add(amount));

        // Update the receiver account in the database
        accountRepository.save(receiverAccount);

        // Create a transaction record for the transfer
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);
        transaction.setTimestamp(LocalDateTime.now());

        // Save the transaction in the database
        transactionRepository.save(transaction);

        return "Transfer successful.";
    }
}