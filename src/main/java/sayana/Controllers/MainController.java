package sayana.Controllers;

import sayana.models.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

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
        openWindow("/sayana/flowers-window.fxml", "Цветы - Цветочный магазин");
    }

    @FXML
    private void handlePlantsClick() {
        openWindow("/sayana/plants-window.fxml", "Растения - Цветочный магазин");
    }

    @FXML
    private void handleDeliveryClick() {
        openWindow("/sayana/delivery-window.fxml", "Доставка - Цветочный магазин");
    }

    @FXML
    private void handleProfileClick() {
        openWindow("/sayana/profile-window.fxml", "Профиль - Цветочный магазин");
    }

    // Обработчики для нижней навигации
    @FXML
    private void handleSearchClick() {
        openWindow("/sayana/admin-menu.fxml", "Админ-панель - Цветочный магазин");
    }

    @FXML
    private void handleHomeClick() {
        // Уже на главной странице
    }

    @FXML
    private void handleCartClick() {
        openWindow("/sayana/cart-window.fxml", "Корзина - Цветочный магазин");
    }

    @FXML
    private void handleFavoritesClick() {
        openWindow("/sayana/favorites-window.fxml", "Избранное - Цветочный магазин");
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller != null) {
                // Все контроллеры, которые требуют пользователя
                if (controller instanceof ProfileController) {
                    ((ProfileController) controller).setUser(currentUser);
                }
                else if (controller instanceof FlowersController) {
                    ((FlowersController) controller).setUser(currentUser);
                }
                else if (controller instanceof PlantsController) {
                    ((PlantsController) controller).setUser(currentUser);
                }
                else if (controller instanceof CartController) {
                    ((CartController) controller).setUser(currentUser);
                }
                else if (controller instanceof FavoritesController) {
                    ((FavoritesController) controller).setUser(currentUser);
                }
                else if (controller instanceof AdminMenuController) {
                    ((AdminMenuController) controller).setUser(currentUser);
                }
            }

            Stage newStage = new Stage();
            newStage.setTitle(title);

            int width = 800;
            int height = 900;

            if (fxmlPath.contains("admin")) {
                width = 1000;
            }

            newStage.setScene(new Scene(root, width, height));
            newStage.setResizable(true);
            newStage.setMinWidth(600);
            newStage.setMinHeight(700);
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть окно: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}