package com.example.mini_banking_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private PasswordField passwordField;

    private final String validAccountNumber = "A123";
    private final String validPin = "1234";
    @FXML
    private AnchorPane popUpAnchorPane;
    @FXML
    private TextField accountNumberTextField;

    @FXML
    public void loginButtonOnAction(ActionEvent event) {
        String accountNumber = accountNumberTextField.getText();
        String pin = passwordField.getText();

        if (accountNumber.isEmpty() || pin.isEmpty()) {
            showAlert("Error", "Please enter both account number and pin.");
            return;
        }

        if (validAccountNumber.equals(accountNumber) && validPin.equals(pin)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mini_banking_app/bankingAppView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Mini Banking App Dashboard");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException | NullPointerException e) {
                showAlert("Login Error", "Unable to load the dashboard. Check FXML path.");
                e.printStackTrace();
            }
        } else {
            showAlert("Error","Invalid account number or pin.");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
