package com.example.mini_banking_app;

import java.util.Arrays;
import java.util.List;

public class DummyRecipients {

    public static List<RecipientAccount> getAllRecipients() {
        return Arrays.asList(
                new RecipientAccount("Priyo", 300.00, "R001"),
                new RecipientAccount("Priya", 300.00, "R006"),
                new RecipientAccount("Susmi", 200.00, "R002"),
                new RecipientAccount("Abanti", 150.00, "R003"),
                new RecipientAccount("Orthi", 400.00, "R004"),
                new RecipientAccount("Abir", 500.00, "R005")
        );
    }
}
