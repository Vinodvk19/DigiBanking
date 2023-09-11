package com.nl.bank.digibank.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransferRequest {
    @NotNull
    private String originatorAccountNumber;
    @NotNull
    private String counterpartyAccountNumber;
    @NotNull
    private BigDecimal amount;

    // Getters and setters
}
