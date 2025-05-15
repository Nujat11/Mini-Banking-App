package com.example.mini_banking_app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BankingAppController {

    @FXML private AnchorPane root;
    @FXML private TextField amountTextField;
    @FXML private Label balanceLabel;
    @FXML private Label goalStatusLabel;
    @FXML private ListView<String> transactionHistoryList;
    @FXML private PasswordField pinField;
    @FXML private CheckBox darkModeCheckBox;
    @FXML private TextField searchRecipientsTextField;
    @FXML
    private ComboBox<String> toCurrencyComboBox1;
    @FXML
    private ComboBox<String> fromCurrencyComboBox;

    private final BankAccount userAccount = new BankAccount(500.00, "1234", "A123");
    private final ObservableList<RecipientAccount> recipients = FXCollections.observableArrayList(DummyRecipients.getAllRecipients());
    private final DummyCurrencyConverter converter = new DummyCurrencyConverter();
    private final ObservableList<String> history = FXCollections.observableArrayList();

    private double savingsGoal = 1000;
    private double currentSavings = 0;

    @FXML
    public void initialize() {
        fromCurrencyComboBox.setItems(FXCollections.observableArrayList("USD", "EUR", "BDT"));
        fromCurrencyComboBox.setValue("BDT");

        toCurrencyComboBox1.setItems(FXCollections.observableArrayList("USD", "EUR", "BDT"));
        toCurrencyComboBox1.setValue("BDT");

        transactionHistoryList.setItems(history);

        balanceLabel.setText("Balance: $0.00");

        updateGoalLabel();
    }

    private void updateBalanceLabel() {
        balanceLabel.setText(String.format("Balance: $%.2f", userAccount.getBalance()));
    }

    private void updateGoalLabel() {
        goalStatusLabel.setText(String.format("Goal: $%.2f / $%.2f", currentSavings, savingsGoal));
    }

    @FXML
    void checkBalanceButtonOnAction(ActionEvent event) {
        if (!isPinVerified()) return;
        updateBalanceLabel();
        history.add("Checked balance: $" + String.format("%.2f", userAccount.getBalance()));
        showAlert("Balance Information", "Your current balance is $" + String.format("%.2f", userAccount.getBalance()), Alert.AlertType.INFORMATION);
        pinField.clear();
    }

    @FXML
    void depositButtonOnAction(ActionEvent event) {
        if (!isPinVerified()) return;
        try {
            double amount = Double.parseDouble(amountTextField.getText());
            if (amount <= 0) {
                showAlert("Invalid Amount", "Please enter a positive amount to deposit.", Alert.AlertType.ERROR);
                return;
            }
            userAccount.deposit(amount);
            updateBalanceLabel();
            history.add("Deposited: $" + amount);
            showAlert("Deposit Successful", "You have deposited $" + amount + " successfully.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid numeric amount.", Alert.AlertType.ERROR);
        }
        pinField.clear();
    }

    @FXML
    void withdrawButtonOnAction(ActionEvent event) {
        if (!isPinVerified()) return;
        try {
            double amount = Double.parseDouble(amountTextField.getText());
            if (amount <= 0) {
                showAlert("Invalid Amount", "Please enter a positive amount to withdraw.", Alert.AlertType.ERROR);
                return;
            }
            if (userAccount.withdraw(amount)) {
                updateBalanceLabel();
                history.add("Withdrew: $" + amount);
                showAlert("Withdrawal Successful", "You have withdrawn $" + amount + " successfully.", Alert.AlertType.INFORMATION);
            } else {
                history.add("Withdrawal failed: Insufficient funds");
                showAlert("Withdrawal Failed", "Insufficient funds for withdrawal.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid numeric amount.", Alert.AlertType.ERROR);
        }
        pinField.clear();
    }

    @FXML
    void transferButtonOnAction(ActionEvent event) {
        if (!isPinVerified()) return;

        try {
            double amount = Double.parseDouble(amountTextField.getText());
            if (amount <= 0) {
                showAlert("Invalid Amount", "Please enter a positive amount to transfer.", Alert.AlertType.ERROR);
                return;
            }

            if (amount > userAccount.getBalance()) {
                showAlert("Transfer Failed", "Insufficient funds for transfer.", Alert.AlertType.ERROR);
                return;
            }

            RecipientAccount recipientAccount = findRecipientByIdOrName(searchRecipientsTextField.getText());
            if (recipientAccount == null) {
                showAlert("Transfer Failed", "Recipient not found.", Alert.AlertType.ERROR);
                return;
            }

            userAccount.withdraw(amount);
            recipientAccount.receive(amount);
            updateBalanceLabel();
            history.add("Transferred: $" + amount + " to " + recipientAccount.getName());
            showAlert("Transfer Successful", "Transferred $" + amount + " to " + recipientAccount.getName(), Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid numeric amount.", Alert.AlertType.ERROR);
        } finally {
            pinField.clear();
        }
    }


    @FXML
    void convertCurrencyButtonOnAction(ActionEvent event) {
        if (!isPinVerified()) return;
        try {
            double amount = Double.parseDouble(amountTextField.getText());
            if (amount <= 0) {
                showAlert("Invalid Amount", "Please enter a positive amount for conversion.", Alert.AlertType.ERROR);
                return;
            }

            String fromCurrency = fromCurrencyComboBox.getValue();
            String toCurrency = toCurrencyComboBox1.getValue();

            if (fromCurrency == null || toCurrency == null) {
                showAlert("Selection Error", "Please select both source and target currencies.", Alert.AlertType.ERROR);
                return;
            }

            double convertedAmount = converter.convert(fromCurrency, toCurrency, amount);
            String message = String.format("%.2f %s = %.2f %s", amount, fromCurrency, convertedAmount, toCurrency);
            showAlert("Conversion Result", message, Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid numeric amount.", Alert.AlertType.ERROR);
        }
        pinField.clear();
    }

    @FXML
    void setGoalButtonOnAction(ActionEvent event) {
        if (!isPinVerified()) return;
        try {
            double amount = Double.parseDouble(amountTextField.getText());
            if (amount <= 0) {
                showAlert("Invalid Amount", "Please enter a positive amount to save.", Alert.AlertType.ERROR);
                return;
            }
            currentSavings += amount;
            updateGoalLabel();
            history.add("Saved toward goal: $" + amount);
            showAlert("Savings Updated", "Successfully saved $" + amount + " toward your goal.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid numeric amount.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void darkModeCheckBoxOnAction(ActionEvent event) {
        if (darkModeCheckBox.isSelected()) {
            root.setStyle("-fx-background-color: #333333;");
            showAlert("Dark Mode", "Dark mode enabled.", Alert.AlertType.INFORMATION);
        } else {
            root.setStyle("-fx-background-color: #f0f4f7;");
            showAlert("Dark Mode", "Dark mode disabled.", Alert.AlertType.INFORMATION);
        }
        pinField.clear();
    }

    @FXML
    public void searchRecipientByNameOrId(ActionEvent event) {
        String query = searchRecipientsTextField.getText().toLowerCase();
        RecipientAccount foundRecipient = findRecipientByIdOrName(query);
        if (foundRecipient != null) {
            transactionHistoryList.getItems().add("Found recipient: " + foundRecipient.getName() + " (ID: " + foundRecipient.getAccountId() + ")");
            showAlert("Recipient Found", "Found recipient: " + foundRecipient.getName(), Alert.AlertType.INFORMATION);
        } else {
            transactionHistoryList.getItems().add("No matching recipient found.");
            showAlert("Search Result", "No matching recipient found.", Alert.AlertType.ERROR);
        }
        pinField.clear();
    }

    private RecipientAccount findRecipientByIdOrName(String query) {
        for (RecipientAccount recipient : DummyRecipients.getAllRecipients()) {
            if (recipient.getName().toLowerCase().contains(query) || recipient.getAccountId().toLowerCase().contains(query)) {
                return recipient;
            }
        }
        return null;
    }

    private boolean isPinVerified() {
        String enteredPin = pinField.getText();
        if (enteredPin == null || enteredPin.trim().isEmpty()) {
            showAlert("Authentication Failed", "Please enter your 4-digit PIN.", Alert.AlertType.ERROR);
            return false;
        }

        if (!userAccount.verifyPin(enteredPin)) {
            showAlert("Authentication Failed", "Incorrect PIN entered.", Alert.AlertType.ERROR);
            history.add("Operation failed: Incorrect PIN");
            return false;
        }

        return true;
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void allHistoryButtonOnAction(ActionEvent actionEvent) {
        if (!isPinVerified()) return;

        File file = new File("history.bin");

        List<String> existingHistory = new ArrayList<>();

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    existingHistory = (List<String>) obj;
                }
            } catch (IOException | ClassNotFoundException e) {
                showAlert("Read Failed", "Could not read existing history.", Alert.AlertType.ERROR);
                e.printStackTrace();
                return;
            }
        }

        String currentDate = java.time.LocalDate.now().toString();

        List<String> datedHistory = new ArrayList<>();
        for (String entry : history) {
            datedHistory.add("[" + currentDate + "] " + entry);
        }

        existingHistory.addAll(datedHistory);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(existingHistory);
        } catch (IOException e) {
            showAlert("Save Failed", "Could not save the transaction history.", Alert.AlertType.ERROR);
            e.printStackTrace();
            return;
        }

        StringBuilder historyText = new StringBuilder();
        for (String s : existingHistory) {
            historyText.append(s).append("\n");
        }

        showAlert("All Transaction History", historyText.toString(), Alert.AlertType.INFORMATION);

        transactionHistoryList.getItems().clear();
        transactionHistoryList.getItems().addAll(existingHistory);
        pinField.clear();
    }

}



