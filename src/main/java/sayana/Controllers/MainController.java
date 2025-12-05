package sayana.Controllers;

import javafx.stage.StageStyle;
import sayana.models.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainController {

    @FXML private Label welcomeLabel;
    @FXML private Label userInfoLabel;
    @FXML private Button adminButton; // Добавляем кнопку админа

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

            // Добавляем отображение роли
            if (currentUser.isAdmin()) {
                userInfo.append(" | Роль: Администратор");
            }

            if (currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
                userInfo.append(" | Email: ").append(currentUser.getEmail());
            }

            userInfoLabel.setText(userInfo.toString());
            welcomeLabel.setText("Добро пожаловать, " + currentUser.getFullName() + "!");

            // Показываем или скрываем кнопку админа
            if (currentUser.isAdmin() && adminButton != null) {
                adminButton.setVisible(true);
            } else if (adminButton != null) {
                adminButton.setVisible(false);
            }
        }
    }
    // Новый метод для открытия админ-панели
    @FXML
    private void handleAdminPanel() {
        openWindow("/sayana/admin-menu.fxml", "Админ-панель - Цветочный магазин");
    }

    @FXML
    private void handleLogout() {
        try {
            Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/auth.fxml"));
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

    @FXML
    private void handleFlowers() {
        openWindow("/sayana/flowers.fxml", "Цветы - Цветочный магазин");
    }

    @FXML
    private void handlePlants() {
        openWindow("/sayana/plants.fxml", "Растения - Цветочный магазин");
    }

    @FXML
    private void handleCart() {
        openWindow("/sayana/cart.fxml", "Корзина - Цветочный магазин");
    }

    @FXML
    private void handleProfile() {
        openWindow("/sayana/profile.fxml", "Профиль - Цветочный магазин");
    }

    @FXML
    private void handleOrders() {
        openWindow("/sayana/orders.fxml", "Мои заказы - Цветочный магазин");
    }

    @FXML
    private void handleSearchClick() {
        openWindow("/sayana/admin-menu.fxml", "Админ-панель - Цветочный магазин");
    }

    @FXML
    private void handleHomeClick() {
        // Уже на главной странице
    }

    @FXML
    private void handleFavoritesClick() {
        openWindow("/sayana/favorites-window.fxml", "Избранное - Цветочный магазин");
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
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
                else if (controller instanceof OrdersController) {
                    ((OrdersController) controller).setUser(currentUser);
                }
                else if (controller instanceof AdminMenuController) {
                    ((AdminMenuController) controller).setUser(currentUser);
                }
            }

            Stage newStage = new Stage();
            newStage.setTitle(title);

            // УСТАНОВКА НА ВЕСЬ ЭКРАН
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setMaximized(true); // ОТКРЫТЬ НА ВЕСЬ ЭКРАН

            // Устанавливаем минимальный размер
            newStage.setMinWidth(1024);
            newStage.setMinHeight(768);

            newStage.setScene(new Scene(root));
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