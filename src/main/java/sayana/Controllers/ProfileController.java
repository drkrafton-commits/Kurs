package sayana.Controllers;

import sayana.DAO.DbConnection;
import sayana.models.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ProfileController {

    @FXML private Label userNameLabel;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    private UserType currentUser;
    private DbConnection dbConnection = new DbConnection();

    public void setUser(UserType user) {
        this.currentUser = user;
        if (userNameLabel != null) {
            loadUserData();
        }
    }

    @FXML
    public void initialize() {
        System.out.println("ProfileController инициализирован");
        if (currentUser != null) {
            loadUserData();
        }
    }

    private void loadUserData() {
        if (currentUser != null) {
            System.out.println("Загружаем данные пользователя: " + currentUser.getFullName());

            if (userNameLabel != null) {
                userNameLabel.setText(currentUser.getFullName());
                System.out.println("userNameLabel установлен в: " + currentUser.getFullName());
            }

            fullNameField.setText(currentUser.getFullName());
            emailField.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "");
            phoneField.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage currentStage = (Stage) ((Node) fullNameField).getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setUser(currentUser);

            Stage mainStage = new Stage();
            mainStage.setTitle("Главное меню - Цветочный магазин");
            mainStage.setMaximized(true);
            mainStage.setMinWidth(1024);
            mainStage.setMinHeight(768);
            mainStage.setScene(new Scene(root));
            mainStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave() {
        if (currentUser != null) {
            try {
                boolean success = dbConnection.updateUserProfile(
                        currentUser.getUserId(),
                        fullNameField.getText(),
                        emailField.getText(),
                        phoneField.getText()
                );
                if (success) {
                    showAlert("Сохранение", "Данные успешно сохранены!");
                    currentUser.setFullName(fullNameField.getText());
                    currentUser.setEmail(emailField.getText());
                    currentUser.setPhone(phoneField.getText());
                    userNameLabel.setText(currentUser.getFullName());
                } else {
                    showAlert("Ошибка", "Не удалось сохранить данные");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Ошибка", "Ошибка при сохранении: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleChangePassword() {
        showAlert("Смена пароля", "Функция смены пароля");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}