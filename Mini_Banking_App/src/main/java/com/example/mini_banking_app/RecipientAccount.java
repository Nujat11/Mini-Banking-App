package com.example.mini_banking_app;

public class RecipientAccount {
    private String name;
    private double balance;
    private String accountNumber;

    public RecipientAccount(String name, double balance, String accountNumber) {
        this.name = name;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    public void receive(double amount) {
        this.balance += amount;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountId() {
        return accountNumber;
    }

    @Override
    public String toString() {
        return "RecipientAccount{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
