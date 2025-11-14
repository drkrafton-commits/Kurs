package com.example.kurs;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AuthController {

    @FXML
    private Text headerText;

    @FXML
    private Text loginLabel;

    @FXML
    private TextField loginField;

    @FXML
    private Text passwordLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        System.out.println("Контроллер инициализирован!");

        // Устанавливаем текст
        headerText.setText("Вход");
        loginLabel.setText("Login");
        passwordLabel.setText("Password");
        loginButton.setText("Login");
        registerButton.setText("Регистрация");

        // Устанавливаем подсказки
        loginField.setPromptText("Введите логин");
        passwordField.setPromptText("Введите пароль");

        // Применяем стили
        applyStyles();
    }

    @FXML
    private void handleLogin() {
        String username = loginField.getText();
        String password = passwordField.getText();

        System.out.println("Попытка входа: " + username);

        // Проверка на пустые поля
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            showAlert("Ошибка", "Пожалуйста, заполните все поля");
            return;
        }

        // Проверка учетных данных
        if (authenticate(username, password)) {
            showAlert("Успех", "Вход выполнен успешно!");
            clearFields();
        } else {
            showAlert("Ошибка", "Неверный логин или пароль");
            passwordField.clear();
        }
    }

    @FXML
    private void handleRegistration() {
        showAlert("Регистрация", "Функция регистрации будет реализована позже");
    }

    private boolean authenticate(String username, String password) {
        // Простая проверка - в реальном приложении здесь будет база данных
        return "admin".equals(username) && "123456".equals(password);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        loginField.clear();
        passwordField.clear();
        loginField.requestFocus();
    }

    private void applyStyles() {
        // Увеличиваем шрифт заголовка
        headerText.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-fill: #333333;");
        loginLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #555555;");
        passwordLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #555555;");

        // Увеличиваем размер полей ввода
        loginField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #cccccc; " +
                "-fx-border-radius: 6px; -fx-background-radius: 6px; -fx-pref-width: 360px; -fx-pref-height: 40px;");
        passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #cccccc; " +
                "-fx-border-radius: 6px; -fx-background-radius: 6px; -fx-pref-width: 360px; -fx-pref-height: 40px;");

        // Увеличиваем размер кнопок
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 16px; -fx-padding: 15px 30px; -fx-border-radius: 6px; -fx-background-radius: 6px; " +
                "-fx-cursor: hand; -fx-pref-width: 200px; -fx-pref-height: 40px;");

        registerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #4CAF50; -fx-font-size: 16px; " +
                "-fx-border-color: #4CAF50; -fx-border-width: 2px; -fx-border-radius: 6px; " +
                "-fx-background-radius: 6px; -fx-cursor: hand; -fx-pref-width: 200px; -fx-pref-height: 40px;");

        // Эффекты при наведении
        setupHoverEffects();
    }

    private void setupHoverEffects() {
        // Эффекты для кнопки Login
        loginButton.setOnMouseEntered(e ->
                loginButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 16px; -fx-padding: 15px 30px; -fx-border-radius: 6px; -fx-background-radius: 6px; " +
                        "-fx-cursor: hand; -fx-pref-width: 200px; -fx-pref-height: 40px;"));

        loginButton.setOnMouseExited(e ->
                loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 16px; -fx-padding: 15px 30px; -fx-border-radius: 6px; -fx-background-radius: 6px; " +
                        "-fx-cursor: hand; -fx-pref-width: 200px; -fx-pref-height: 40px;"));

        // Эффекты для кнопки Регистрация
        registerButton.setOnMouseEntered(e ->
                registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                        "-fx-border-color: #4CAF50; -fx-border-width: 2px; -fx-border-radius: 6px; " +
                        "-fx-background-radius: 6px; -fx-cursor: hand; -fx-pref-width: 200px; -fx-pref-height: 40px;"));

        registerButton.setOnMouseExited(e ->
                registerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #4CAF50; -fx-font-size: 16px; " +
                        "-fx-border-color: #4CAF50; -fx-border-width: 2px; -fx-border-radius: 6px; " +
                        "-fx-background-radius: 6px; -fx-cursor: hand; -fx-pref-width: 200px; -fx-pref-height: 40px;"));

        // Эффекты фокуса для полей ввода
        loginField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                loginField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #4CAF50; " +
                        "-fx-border-radius: 6px; -fx-background-radius: 6px; -fx-pref-width: 360px; -fx-pref-height: 40px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(76,175,80,0.2), 5, 0, 0, 0);");
            } else {
                loginField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #cccccc; " +
                        "-fx-border-radius: 6px; -fx-background-radius: 6px; -fx-pref-width: 360px; -fx-pref-height: 40px;");
            }
        });

        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #4CAF50; " +
                        "-fx-border-radius: 6px; -fx-background-radius: 6px; -fx-pref-width: 360px; -fx-pref-height: 40px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(76,175,80,0.2), 5, 0, 0, 0);");
            } else {
                passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #cccccc; " +
                        "-fx-border-radius: 6px; -fx-background-radius: 6px; -fx-pref-width: 360px; -fx-pref-height: 40px;");
            }
        });
    }
}