package com.nl.bank.digibank;

import com.nl.bank.digibank.dto.AccountOverview;
import com.nl.bank.digibank.model.Account;
import com.nl.bank.digibank.repo.AccountRepository;
import com.nl.bank.digibank.repo.TransactionRepository;
import com.nl.bank.digibank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAccountOverview() {
        // Create a mock Account
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setAccountNumber("123456");
        mockAccount.setAccountType("SAVINGS");
        mockAccount.setBalance(new BigDecimal("1000.00"));
        mockAccount.setCurrency("EUR");

        // Mock the behavior of the accountRepository
        when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));

        // Call the method to test
        AccountOverview accountOverview = accountService.getAccountOverview(1L);

        // Verify the result
        assertEquals("123456", accountOverview.getAccountNumber());
        assertEquals("SAVINGS", accountOverview.getAccountType());
        assertEquals(new BigDecimal("1000.00"), accountOverview.getBalance());
        assertEquals("EUR", accountOverview.getCurrency());
    }


    @Test
    public void testTransferMoneySufficientBalance() {
        // Create sender and receiver accounts
        Account senderAccount = new Account();
        senderAccount.setId(1L);
        senderAccount.setBalance(new BigDecimal("1000.00"));

        Account receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setBalance(new BigDecimal("500.00"));

        // Mock repository methods to return the accounts
        when(accountRepository.findById(1L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiverAccount));

        // Perform the money transfer
        String result = accountService.transferMoney("NL31ABNA5105927792", "NL70ABNA8495828132", new BigDecimal("200.00"));

        // Verify that the sender's balance is updated correctly
        verify(accountRepository).save(senderAccount);
        BigDecimal expectedSenderBalance = new BigDecimal("800.00");
        assert (senderAccount.getBalance().compareTo(expectedSenderBalance) == 0);

        // Verify that the receiver's balance is updated correctly
        verify(accountRepository).save(receiverAccount);
        BigDecimal expectedReceiverBalance = new BigDecimal("700.00");
        assert (receiverAccount.getBalance().compareTo(expectedReceiverBalance) == 0);

        // Verify that a transaction record is saved
        verify(transactionRepository).save(any());

        // Verify the result message
        assert (result.equals("Transfer successful."));
    }

    @Test
    public void testTransferMoneyInsufficientBalance() {
        // Create sender and receiver accounts
        Account senderAccount = new Account();
        senderAccount.setId(1L);
        senderAccount.setBalance(new BigDecimal("100.00"));

        Account receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setBalance(new BigDecimal("500.00"));

        // Mock repository methods to return the accounts
        when(accountRepository.findById(1L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiverAccount));

        // Perform the money transfer with insufficient balance
        String result = accountService.transferMoney("NL31ABNA5105927792", "NL70ABNA8495828132", new BigDecimal("200.00"));

        // Verify that no balance updates are made
        verify(accountRepository, never()).save(senderAccount);
        verify(accountRepository, never()).save(receiverAccount);

        // Verify that no transaction record is saved
        verify(transactionRepository, never()).save(any());

        // Verify the result message
        assert (result.equals("Insufficient balance in sender account."));
    }
}

