package com.example.mini_banking_app;

import java.io.Serializable;

public class BankAccount implements Serializable {
    private double balance;
    private final String pin;
    private final String accountId;

    public BankAccount(double balance, String pin, String accountId) {
        this.balance = balance;
        this.pin = pin;
        this.accountId = accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public boolean verifyPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public String getPin() {
        return pin;
    }

    public String getAccountId() {
        return accountId;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "balance=" + balance +
                ", pin='" + pin + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
