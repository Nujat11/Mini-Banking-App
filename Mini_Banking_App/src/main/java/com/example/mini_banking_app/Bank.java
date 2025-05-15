package com.example.mini_banking_app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bank implements Serializable {
    private List<BankAccount> accounts;
    private List<RecipientAccount> recipients;

    public Bank() {
        accounts = new ArrayList<>();
        recipients = new ArrayList<>();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public BankAccount findAccountByPin(String pin) {
        for (BankAccount account : accounts) {
            if (account.getPin().equals(pin)) {
                return account;
            }
        }
        return null;
    }

    public void addRecipient(RecipientAccount recipient) {
        recipients.add(recipient);
    }

    public List<RecipientAccount> getRecipients() {
        return recipients;
    }


    @Override
    public String toString() {
        return "Bank{" +
                "accounts=" + accounts +
                ", recipients=" + recipients +
                '}';
    }
}
