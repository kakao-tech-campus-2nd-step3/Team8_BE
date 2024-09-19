package com.example.sinitto.dto;

public class SinittoSignupRequest {

    String name;
    String phoneNumber;
    String email;
    String accountNumber;
    String bankName;

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
