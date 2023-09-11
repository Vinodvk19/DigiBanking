package com.nl.bank.digibank.service;

import com.nl.bank.digibank.dto.CustomerRegisterResponse;
import com.nl.bank.digibank.dto.CustomerRegistration;
import com.nl.bank.digibank.exception.UserAlreadyExistAuthenticationException;
import com.nl.bank.digibank.model.Customer;
import com.nl.bank.digibank.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    public static final String ACCOUNT_TYPE = "SAVINGS";
    public static final String NOOP = "{noop}";

    private final CustomerRepository customerRepository;
    private final AccountService accountService;
    @Value("${allowed.countries}")
    private String allowedCountries;

       public CustomerRegisterResponse registerCustomer(CustomerRegistration signUpRequest) {
        if (customerRepository.existsByUsernameIgnoreCase(signUpRequest.getUsername())) {
            throw new UserAlreadyExistAuthenticationException("Customer with username " + signUpRequest.getUsername() + " already exist");
        }
        // Check if the customer's country is allowed
        if (!isCountryAllowed(signUpRequest.getCountry())) {
            throw new IllegalArgumentException("Customer's country is not allowed.");
        }

        // Check if the customer is above 18 years old
        if (!isCustomerAbove18(signUpRequest.getDateOfBirth())) {
            throw new IllegalArgumentException("Customer must be above 18 years old.");
        }
/*
        // Assign  Unique IBAN account number
        customer.setIban(IBANGenerator.generateNLIBAN());

        //Assign Account type
        customer.setAccountType(ACCOUNT_TYPE);*/
        // Generate a default password
        String defaultPassword = generateDefaultPassword();
        Customer customer = new Customer();
        BeanUtils.copyProperties(signUpRequest, customer);
        // Set the default password in the customer object
        customer.setPassword(NOOP + defaultPassword);
        customer.setEnabled(true);


        // Save the customer to the database
        customer = customerRepository.save(customer);
        accountService.createAccount(customer);
        return new CustomerRegisterResponse(customer.getUsername(), defaultPassword);
    }

    private String generateDefaultPassword() {
        // Generate a random password of length 8
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public Customer findByUsername(String username) {
        Optional<Customer> customerOptional = customerRepository.findByUsername(username);
        return customerOptional.orElse(null); // Return null if not found
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    private boolean isCountryAllowed(String countryCode) {
        // Split the allowedCountries string into a list of country codes
        List<String> allowedCountriesList = Arrays.asList(allowedCountries.split(","));

        // Check if the country code is allowed
        return allowedCountriesList.contains(countryCode);
    }

    private boolean isCustomerAbove18(LocalDate dateOfBirth) {
        // check if the customer is above 18 years old
        // Calculate the age based on the date of birth
        LocalDate now = LocalDate.now();
        int age = Period.between(dateOfBirth, now).getYears();
        return age >= 18;
    }

   /* public void transferMoney(Long senderCustomerId, Long receiverCustomerId, BigDecimal amount) {
        // Retrieve sender and receiver customers from the database
        Customer sender = customerRepository.findById(senderCustomerId).orElse(null);
        Customer receiver = customerRepository.findById(receiverCustomerId).orElse(null);

        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Invalid sender or receiver customer.");
        }

        // Check if the sender has a sufficient balance
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance for the transfer.");
        }

        // Perform the transfer
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        // Create and save a transaction record
        Transaction transaction = new Transaction();
        transaction.setSenderAccountId(sender.getId());
        transaction.setReceiverAccountId(receiver.getId());
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setCustomers(sender);
        transaction.setCustomers(receiver);

        transactionRepository.save(transaction);
    }*/


    public Optional<Customer> findUserByUsername(String username) {
           return customerRepository.findByUsername(username);
    }
}
