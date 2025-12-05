package sayana.Controllers;

import sayana.DAO.DbConnection;
import sayana.models.Order;
import sayana.models.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.List;

public class OrdersController {

    @FXML private VBox ordersContainer;
    @FXML private Text emptyOrdersText;

    private UserType currentUser;
    private DbConnection dbConnection = new DbConnection();

    public void setUser(UserType user) {
        this.currentUser = user;
        if (ordersContainer != null) {
            loadOrders();
        }
    }

    @FXML
    public void initialize() {
        if (currentUser != null) {
            loadOrders();
        }
    }

    private void loadOrders() {
        try {
            List<Order> orders = dbConnection.getUserOrders(currentUser.getUserId());
            ordersContainer.getChildren().clear();

            if (orders.isEmpty()) {
                emptyOrdersText.setVisible(true);
                emptyOrdersText.setText("У вас пока нет заказов");
            } else {
                emptyOrdersText.setVisible(false);

                for (Order order : orders) {
                    HBox orderCard = createOrderCard(order);
                    ordersContainer.getChildren().add(orderCard);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить заказы: " + e.getMessage());
        }
    }

    private HBox createOrderCard(Order order) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15;");
        card.setMinHeight(100);

        VBox infoBox = new VBox(5);
        infoBox.setPrefWidth(500);

        // Номер заказа и дата
        Text orderHeader = new Text("Заказ #" + order.getOrderId() + " • " + order.getFormattedDate());
        orderHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-fill: #000000;");

        // Статус заказа
        String status = order.getStatusText();
        String statusColor = getStatusColor(order.getOrderStatus());

        Text statusText = new Text("Статус: " + status);
        statusText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: " + statusColor + ";");

        // Сумма заказа
        Text amountText = new Text("Сумма: " + String.format("%.2f руб.", order.getTotalAmount()));
        amountText.setStyle("-fx-font-size: 14px; -fx-fill: #000000;");

        // Кнопка детализации (можно будет расширить)
        HBox buttonBox = new HBox(10);

        if (order.getOrderStatus().equals("pending")) {
            Button cancelButton = new Button("Отменить");
            cancelButton.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; " +
                    "-fx-font-size: 12px; -fx-padding: 5 10;");
            cancelButton.setOnAction(e -> handleCancelOrder(order));
            buttonBox.getChildren().add(cancelButton);
        }

        infoBox.getChildren().addAll(orderHeader, statusText, amountText, buttonBox);
        card.getChildren().add(infoBox);

        return card;
    }

    private String getStatusColor(String status) {
        switch (status) {
            case "pending": return "#FF9800"; // оранжевый
            case "confirmed": return "#2196F3"; // синий
            case "in_progress": return "#4CAF50"; // зеленый
            case "delivered": return "#9C27B0"; // фиолетовый
            case "cancelled": return "#F44336"; // красный
            default: return "#000000";
        }
    }

    private void handleCancelOrder(Order order) {
        try {
            boolean success = dbConnection.updateOrderStatus(order.getOrderId(), "cancelled");
            if (success) {
                showAlert("Отмена заказа", "Заказ #" + order.getOrderId() + " отменен");
                loadOrders(); // Обновляем список
            } else {
                showAlert("Ошибка", "Не удалось отменить заказ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Ошибка при отмене заказа: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage currentStage = (Stage) ordersContainer.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/main-window.fxml"));
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
    private void handleRefresh() {
        loadOrders();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}