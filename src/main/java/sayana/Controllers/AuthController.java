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
//                showAlert("Успех", "Вход выполнен: " + user.getFio());
                openMainWindow(user);
            } else {
                showAlert("Ошибка", "Неверный логин или пароль");
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка базы данных");
        }
    }

    private void openMainWindow(UserType user) {
        try {
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/main-window.fxml"));
            Parent root = loader.load();

//            MainController mainController = loader.getController();
//            mainController.setUser(user);

            Stage mainStage = new Stage();
            mainStage.setTitle("Главное меню");
            mainStage.setScene(new Scene(root, 440, 600));
            mainStage.setResizable(false);
            mainStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть главное окно: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegistration() {
        TextField fioField = new TextField();
        TextField newLoginField = new TextField();
        PasswordField newPasswordField = new PasswordField();

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
        grid.add(fioField, 1, 0);
        grid.add(new javafx.scene.control.Label("Логин:"), 0, 1);
        grid.add(newLoginField, 1, 1);
        grid.add(new javafx.scene.control.Label("Пароль:"), 0, 2);
        grid.add(newPasswordField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Упрощенная обработка результата
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                return "register";
            }
            return null;
        });

        java.util.Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && result.get().equals("register")) {
            String fio = fioField.getText().trim();
            String login = newLoginField.getText().trim();
            String password = newPasswordField.getText().trim();

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
        alert.setContentText(message);
        alert.showAndWait();
    }
}