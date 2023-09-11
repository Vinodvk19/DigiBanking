package com.nl.bank.digibank.controller;

import com.nl.bank.digibank.dto.AccountOverview;
import com.nl.bank.digibank.dto.ApiResponse;
import com.nl.bank.digibank.dto.TransferRequest;
import com.nl.bank.digibank.model.Customer;
import com.nl.bank.digibank.service.AccountService;
import com.nl.bank.digibank.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;

    @GetMapping("/overview")
    public ResponseEntity<?> getAccountOverview(@AuthenticationPrincipal(errorOnInvalidType=true) Jwt jwt) {
        Customer customer = customerService.findByUsername(jwt.getSubject());
        if(customer == null){
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Customer does not exist"));
        }
        AccountOverview accountOverview = accountService.getAccountOverview(customer.getAccounts().get(0).getId());
        return ResponseEntity.ok(accountOverview);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@Valid @RequestBody TransferRequest transferRequest) {
        try {
            // Perform the money transfer
            String result = accountService.transferMoney(transferRequest.getOriginatorAccountNumber(), transferRequest.getCounterpartyAccountNumber(), transferRequest.getAmount());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to complete the transfer: " + e.getMessage());
        }
    }
}
