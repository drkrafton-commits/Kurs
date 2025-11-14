package com.example.kurs;

import com.example.kurs.DAO.DbConnection;
import com.example.kurs.models.UserType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    private DbConnection dbConnection = new DbConnection();

    @FXML
    private void handleLogin() {
        String username = loginField.getText();
        String password = passwordField.getText();

        try {
            UserType user = dbConnection.authenticate(username, password);
            if (user != null) {
                showAlert("Успех", "Вход выполнен: " + user.getFio());
                loginField.clear();
                passwordField.clear();
            } else {
                showAlert("Ошибка", "Неверный логин или пароль");
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка базы данных");
        }
    }

    @FXML
    private void handleRegistration() {
        TextField fioField = new TextField();
        TextField newLoginField = new TextField();
        PasswordField newPasswordField = new PasswordField();

        javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Регистрация");

        javafx.scene.control.ButtonType registerButtonType =
                new javafx.scene.control.ButtonType("Зарегистрировать",
                        javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType,
                javafx.scene.control.ButtonType.CANCEL);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 10, 10, 10));

        grid.add(new javafx.scene.control.Label("ФИО:"), 0, 0);
        grid.add(fioField, 1, 0);
        grid.add(new javafx.scene.control.Label("Логин:"), 0, 1);
        grid.add(newLoginField, 1, 1);
        grid.add(new javafx.scene.control.Label("Пароль:"), 0, 2);
        grid.add(newPasswordField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        java.util.Optional<Boolean> result = dialog.showAndWait();

        if (result.isPresent() && result.get()) {
            try {
                dbConnection.addUser(fioField.getText(), newLoginField.getText(), newPasswordField.getText());
                showAlert("Успех", "Пользователь зарегистрирован");
            } catch (Exception e) {
                showAlert("Ошибка", "Ошибка регистрации");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}