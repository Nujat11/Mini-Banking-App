module com.example.mini_banking_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mini_banking_app to javafx.fxml;
    exports com.example.mini_banking_app;
}