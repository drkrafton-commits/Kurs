package sayana.Controllers;

import sayana.models.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController {

    @FXML private Text userInfoLabel;
    @FXML private Text welcomeText;
    @FXML private javafx.scene.control.Button logoutButton;

    private UserType currentUser;

    public void setUser(UserType user) {
        this.currentUser = user;
        updateUserInfo();
    }

    @FXML
    public void initialize() {
        if (currentUser != null) {
            updateUserInfo();
        }
    }

    private void updateUserInfo() {
        if (currentUser != null) {
            StringBuilder userInfo = new StringBuilder();
            userInfo.append("ФИО: ").append(currentUser.getFullName()).append(" | ");
            userInfo.append("Логин: ").append(currentUser.getUsername());

            if (currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
                userInfo.append(" | Email: ").append(currentUser.getEmail());
            }

            userInfoLabel.setText(userInfo.toString());
            welcomeText.setText("Добро пожаловать, " + currentUser.getFullName() + "!");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/auth-window.fxml"));
            Parent root = loader.load();

            Stage authStage = new Stage();
            authStage.setTitle("Авторизация - Цветочный магазин");
            authStage.setScene(new Scene(root, 440, 600));
            authStage.setResizable(true);
            authStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Обработчики для карточек меню
    @FXML
    private void handleFlowersClick() {
        showAlert("Цветы", "Переход в раздел цветов");
    }

    @FXML
    private void handlePlantsClick() {
        showAlert("Растения", "Переход в раздел растений");
    }

    @FXML
    private void handleDeliveryClick() {
        showAlert("Доставка", "Переход в раздел доставки");
    }

    @FXML
    private void handleProfileClick() {
        showAlert("Профиль", "Переход в раздел профиля");
    }

    // Обработчики для нижней навигации
    @FXML
    private void handleSearchClick() {
        showAlert("Поиск", "Открытие поиска");
    }

    @FXML
    private void handleHomeClick() {
        showAlert("Главная", "Переход на главную");
    }

    @FXML
    private void handleCartClick() {
        showAlert("Корзина", "Открытие корзины");
    }

    @FXML
    private void handleFavoritesClick() {
        showAlert("Избранное", "Открытие избранного");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}