package com.nl.bank.digibank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountOverview {

    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String currency;
}
