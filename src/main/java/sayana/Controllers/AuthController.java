package sayana.Controllers;

import sayana.DAO.DbConnection;
import sayana.models.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    private DbConnection dbConnection = new DbConnection();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            UserType user = dbConnection.authenticate(username, password);
            if (user != null) {
                openMainWindow(user);
            } else {
                showAlert("Ошибка", "Неверный логин или пароль");
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка базы данных: " + e.getMessage());
        }
    }

    private void openMainWindow(UserType user) {
        try {
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setUser(user);

            Stage mainStage = new Stage();
            mainStage.setTitle("Главное меню - Цветочный магазин");
            mainStage.setMaximized(true);
            mainStage.setMinWidth(1024);
            mainStage.setMinHeight(768);
            mainStage.setScene(new Scene(root));
            mainStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть главное окно: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        TextField fullNameField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField innField = new TextField();
        TextField pasportField = new TextField();
        TextField birthField = new TextField();

        javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Регистрация");
        dialog.setHeaderText("Введите данные для регистрации");

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
        grid.add(fullNameField, 1, 0);
        grid.add(new javafx.scene.control.Label("Логин:"), 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(new javafx.scene.control.Label("Пароль:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(new javafx.scene.control.Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new javafx.scene.control.Label("Телефон:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(new javafx.scene.control.Label("ИНН:"), 0, 5);
        grid.add(innField, 1, 5);
        grid.add(new javafx.scene.control.Label("Номер паспорта:"), 0, 6);
        grid.add(pasportField, 1, 6);
        grid.add(new javafx.scene.control.Label("Дата рождения:"), 0, 7);
        grid.add(birthField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                return "register";
            }
            return null;
        });

        java.util.Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && result.get().equals("register")) {
            String fullName = fullNameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String inn = innField.getText().trim();
            String pasport = pasportField.getText().trim();
            Integer birth = Integer.valueOf(birthField.getText().trim());
            String role = "user";

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                showAlert("Ошибка", "ФИО, логин и пароль обязательны для заполнения");
                return;
            }

            if (!email.isEmpty() && !isValidEmail(email)) {
                showAlert("Ошибка", "Введите корректный email адрес");
                return;
            }

            try {
                if (dbConnection.checkUsernameExists(username)) {
                    showAlert("Ошибка", "Пользователь с таким логином уже существует");
                    return;
                }

                if (!email.isEmpty() && dbConnection.checkEmailExists(email)) {
                    showAlert("Ошибка", "Пользователь с таким email уже существует");
                    return;
                }

                boolean success = dbConnection.addUser(fullName, username, password, email, phone, inn, pasport, birth, role);
                if (success) {
                    showAlert("Успех", "Пользователь успешно зарегистрирован");
                    this.usernameField.setText(username);
                    this.passwordField.clear();
                } else {
                    showAlert("Ошибка", "Не удалось зарегистрировать пользователя");
                }
            } catch (Exception e) {
                showAlert("Ошибка", "Ошибка при регистрации: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}