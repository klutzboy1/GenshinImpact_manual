package com.example.GenshinImpact_manual.entity;

import androidx.annotation.NonNull;

import java.util.Date;


public class User {
    private String registerEmail;
    private String encryptedPassword;

    public User() {
    }

    public User(String registerEmail, String encryptedPassword) {
        this.registerEmail = registerEmail;
        this.encryptedPassword = encryptedPassword;
    }
    public String getRegisterEmail() {
        return registerEmail;
    }

    public void setRegisterEmail(String registerEmail) {
        this.registerEmail = registerEmail;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "registerEmail='" + registerEmail + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                '}';
    }
}