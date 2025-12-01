package sayana.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class DeliveryController {

    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField timeField;

    @FXML
    private void handleBack() {
        try {
            Stage currentStage = (Stage) ((Node) addressField).getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/main-window.fxml"));
            Parent root = loader.load();

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
    private void handleOrder() {
        String address = addressField.getText();
        String phone = phoneField.getText();
        String time = timeField.getText();

        if (address.isEmpty() || phone.isEmpty() || time.isEmpty()) {
            showAlert("Ошибка", "Пожалуйста, заполните все поля");
            return;
        }

        showAlert("Заказ", "Заказ оформлен успешно! Адрес: " + address + ", Телефон: " + phone + ", Время: " + time);

        // Очищаем поля после успешного заказа
        addressField.clear();
        phoneField.clear();
        timeField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}