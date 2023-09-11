package com.nl.bank.digibank.common;

import java.util.Random;

public class IBANGenerator {

    public static String generateNLIBAN() {
        // NL Country Code
        String countryCode = "NL";

        // Generate random check digits (two digits)
        String checkDigits = generateRandomDigits(2);

        // Bank Identifier (4 characters)
        String bankIdentifier = "ABNA";

        // Generate a random customer account number (10 digits)
        String customerAccountNumber = generateRandomDigits(10);

        // Combine the elements to create the IBAN
        String iban = countryCode + checkDigits + bankIdentifier + customerAccountNumber;

        // Ensure the IBAN length is 18 characters
        if (iban.length() != 18) {
            throw new IllegalArgumentException("Invalid IBAN length");
        }

        return iban;
    }

    // Helper method to generate random digits
    private static String generateRandomDigits(int numDigits) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(numDigits);

        for (int i = 0; i < numDigits; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

}
