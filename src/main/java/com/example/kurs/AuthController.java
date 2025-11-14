package com.example.kurs;

import com.example.kurs.DAO.DbConnection;
import com.example.kurs.models.UserType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AuthController {

    @FXML private Text headerText;
    @FXML private Text loginLabel;
    @FXML private TextField loginField;
    @FXML private Text passwordLabel;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    private DbConnection dbConnection;

    @FXML
    public void initialize() {
        // Инициализация подключения к БД
        dbConnection = new DbConnection();

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
        String username = loginField.getText().trim();
        String password = passwordField.getText().trim();

        // Проверка на пустые поля
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Пожалуйста, заполните все поля");
            return;
        }

        try {
            // Аутентификация через базу данных
            UserType user = dbConnection.authenticate(username, password);

            if (user != null) {
                showAlert("Успех", "Вход выполнен успешно!\nДобро пожаловать, " + user.getFio());
                clearFields();
                // Здесь можно добавить переход на главное окно приложения
            } else {
                showAlert("Ошибка", "Неверный логин или пароль");
                passwordField.clear();
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка подключения к базе данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegistration() {
        // Создаем диалоговое окно для регистрации
        TextField fioField = new TextField();
        fioField.setPromptText("ФИО");

        TextField loginField = new TextField();
        loginField.setPromptText("Логин");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");

        javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Регистрация");
        dialog.setHeaderText("Введите данные для регистрации");

        javafx.scene.control.ButtonType registerButtonType = new javafx.scene.control.ButtonType("Зарегистрировать", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, javafx.scene.control.ButtonType.CANCEL);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        grid.add(new javafx.scene.control.Label("ФИО:"), 0, 0);
        grid.add(fioField, 1, 0);
        grid.add(new javafx.scene.control.Label("Логин:"), 0, 1);
        grid.add(loginField, 1, 1);
        grid.add(new javafx.scene.control.Label("Пароль:"), 0, 2);
        grid.add(passwordField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                return true;
            }
            return null;
        });

        java.util.Optional<Boolean> result = dialog.showAndWait();

        if (result.isPresent() && result.get()) {
            String fio = fioField.getText().trim();
            String login = loginField.getText().trim();
            String password = passwordField.getText().trim();

            if (fio.isEmpty() || login.isEmpty() || password.isEmpty()) {
                showAlert("Ошибка", "Все поля должны быть заполнены");
                return;
            }

            try {
                boolean success = dbConnection.addUser(fio, login, password);
                if (success) {
                    showAlert("Успех", "Пользователь успешно зарегистрирован");
                } else {
                    showAlert("Ошибка", "Не удалось зарегистрировать пользователя");
                }
            } catch (Exception e) {
                showAlert("Ошибка", "Ошибка при регистрации: " + e.getMessage());
            }
        }
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
        // Стили для текста
        headerText.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-fill: #333333;");
        loginLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #555555;");
        passwordLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #555555;");

        // Стили для полей ввода
        loginField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #cccccc; " +
                "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-pref-width: 360px; -fx-pref-height: 45px;");
        passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #cccccc; " +
                "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-pref-width: 360px; -fx-pref-height: 45px;");

        // Стили для кнопок
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 16px; -fx-padding: 15px 30px; -fx-border-radius: 8px; -fx-background-radius: 8px; " +
                "-fx-cursor: hand; -fx-pref-width: 220px; -fx-pref-height: 45px;");

        registerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #4CAF50; -fx-font-size: 16px; " +
                "-fx-border-color: #4CAF50; -fx-border-width: 2px; -fx-border-radius: 8px; " +
                "-fx-background-radius: 8px; -fx-cursor: hand; -fx-pref-width: 220px; -fx-pref-height: 45px;");

        // Эффекты при наведении
        setupHoverEffects();

        // Эффекты фокуса для полей ввода
        setupFocusEffects();
    }

    private void setupHoverEffects() {
        // Эффекты для кнопки Login
        loginButton.setOnMouseEntered(e ->
                loginButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 16px; -fx-padding: 15px 30px; -fx-border-radius: 8px; -fx-background-radius: 8px; " +
                        "-fx-cursor: hand; -fx-pref-width: 220px; -fx-pref-height: 45px;"));

        loginButton.setOnMouseExited(e ->
                loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 16px; -fx-padding: 15px 30px; -fx-border-radius: 8px; -fx-background-radius: 8px; " +
                        "-fx-cursor: hand; -fx-pref-width: 220px; -fx-pref-height: 45px;"));

        // Эффекты для кнопки Регистрация
        registerButton.setOnMouseEntered(e ->
                registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                        "-fx-border-color: #4CAF50; -fx-border-width: 2px; -fx-border-radius: 8px; " +
                        "-fx-background-radius: 8px; -fx-cursor: hand; -fx-pref-width: 220px; -fx-pref-height: 45px;"));

        registerButton.setOnMouseExited(e ->
                registerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #4CAF50; -fx-font-size: 16px; " +
                        "-fx-border-color: #4CAF50; -fx-border-width: 2px; -fx-border-radius: 8px; " +
                        "-fx-background-radius: 8px; -fx-cursor: hand; -fx-pref-width: 220px; -fx-pref-height: 45px;"));
    }

    private void setupFocusEffects() {
        loginField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                loginField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #4CAF50; " +
                        "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-pref-width: 360px; -fx-pref-height: 45px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(76,175,80,0.2), 5, 0, 0, 0);");
            } else {
                loginField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #cccccc; " +
                        "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-pref-width: 360px; -fx-pref-height: 45px;");
            }
        });

        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #4CAF50; " +
                        "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-pref-width: 360px; -fx-pref-height: 45px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(76,175,80,0.2), 5, 0, 0, 0);");
            } else {
                passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 12px 16px; -fx-border-color: #cccccc; " +
                        "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-pref-width: 360px; -fx-pref-height: 45px;");
            }
        });
    }

    // Закрытие соединения при завершении
    public void close() {
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }
}