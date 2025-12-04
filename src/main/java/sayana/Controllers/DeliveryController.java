package sayana.Controllers;

import sayana.models.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent; // ДОБАВИТЬ ЭТОТ ИМПОРТ

public class DeliveryController {

    private UserType currentUser;

    public void setUser(UserType user) {
        this.currentUser = user;
    }

    @FXML
    private void handleBack(ActionEvent event) { // ДОБАВИТЬ параметр ActionEvent
        try {
            // Получаем текущее окно из события
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/main-window.fxml"));
            Parent root = loader.load();

            // Передаем пользователя обратно в главное меню
            MainController mainController = loader.getController();
            mainController.setUser(currentUser);

            Stage mainStage = new Stage();
            mainStage.setTitle("Главное меню - Цветочный магазин");

            // ОТКРЫТЬ НА ВЕСЬ ЭКРАН
            mainStage.setMaximized(true);
            mainStage.setMinWidth(1024);
            mainStage.setMinHeight(768);

            mainStage.setScene(new Scene(root));
            mainStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}