package sayana.Controllers;

import sayana.DAO.DbConnection;
import sayana.models.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ProfileController {

    @FXML private Text userNameText;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    private UserType currentUser;
    private DbConnection dbConnection = new DbConnection();

    public void setUser(UserType user) {
        this.currentUser = user;

        // Проверяем, инициализированы ли компоненты
        if (userNameText != null) {
            loadUserData();
        } else {
            System.out.println("Компоненты еще не инициализированы, отложим загрузку данных");
        }
    }

    @FXML
    public void initialize() {
        System.out.println("ProfileController инициализирован");

        // Если пользователь уже был установлен до initialize()
        if (currentUser != null) {
            loadUserData();
        }
    }

    private void loadUserData() {
        if (currentUser != null) {
            System.out.println("Загружаем данные пользователя: " + currentUser.getFullName());

            // Устанавливаем имя пользователя в Text
            if (userNameText != null) {
                userNameText.setText(currentUser.getFullName());
                System.out.println("userNameText установлен в: " + currentUser.getFullName());
            } else {
                System.out.println("userNameText не инициализирован!");
            }

            // Заполняем остальные поля
            fullNameField.setText(currentUser.getFullName());
            emailField.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "");
            phoneField.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
            addressField.setText("г. Москва, ул. Примерная, д. 10, кв. 25");
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage currentStage = (Stage) ((Node) fullNameField).getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/main-window.fxml"));
            Parent root = loader.load();

            // Передаем пользователя обратно в главное меню
            MainController mainController = loader.getController();
            mainController.setUser(currentUser);

            Stage mainStage = new Stage();
            mainStage.setTitle("Главное меню - Цветочный магазин");
            mainStage.setScene(new Scene(root, 800, 900));
            mainStage.setResizable(true);
            mainStage.setMinWidth(600);
            mainStage.setMinHeight(700);
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
                    // Обновляем данные пользователя
                    currentUser.setFullName(fullNameField.getText());
                    currentUser.setEmail(emailField.getText());
                    currentUser.setPhone(phoneField.getText());
                    userNameText.setText(currentUser.getFullName());
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