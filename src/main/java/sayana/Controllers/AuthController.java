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
        // ... остальной код без изменений
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}