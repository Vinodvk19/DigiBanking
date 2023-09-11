package com.nl.bank.digibank.dto;

import lombok.Data;

@Data
public class CustomerRegisterResponse {
    private String username;
    private String password;

    public CustomerRegisterResponse(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
