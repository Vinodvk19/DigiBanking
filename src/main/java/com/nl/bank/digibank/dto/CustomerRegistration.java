package com.nl.bank.digibank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class CustomerRegistration {
    @NotBlank
    private String username;
    @NotBlank
    private String fullName;
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
    @NotBlank
    private String mobileNumber;
    @NotBlank
    private String country;
    private MultipartFile idDocument;
}
