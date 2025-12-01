package sayana.Controllers;

import sayana.DAO.DbConnection;
import sayana.models.Product;
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

public class PlantsController {

    @FXML
    private VBox productsContainer;

    private DbConnection dbConnection = new DbConnection();

    @FXML
    public void initialize() {
        loadPlants();
    }

    private void loadPlants() {
        try {
            List<Product> plants = dbConnection.getProductsByCategory(2); // category_id = 2 для растений
            for (Product plant : plants) {
                // Создаем карточку продукта
                HBox productCard = createProductCard(plant);
                productsContainer.getChildren().add(productCard);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить растения: " + e.getMessage());
        }
    }

    private HBox createProductCard(Product product) {
        HBox card = new HBox();
        card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; -fx-spacing: 15;");

        VBox infoBox = new VBox();
        infoBox.setSpacing(5);

        Text nameText = new Text(product.getProductName());
        nameText.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-fill: #000000;");

        Text priceText = new Text(product.getPrice() + " руб.");
        priceText.setStyle("-fx-font-size: 14px; -fx-fill: #000000;");

        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            Text descText = new Text(product.getDescription());
            descText.setStyle("-fx-font-size: 12px; -fx-fill: #666666;");
            infoBox.getChildren().addAll(nameText, priceText, descText);
        } else {
            infoBox.getChildren().addAll(nameText, priceText);
        }

        Button addButton = new Button("Добавить");
        addButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #5C5B61, #1A573A); " +
                "-fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
        addButton.setOnAction(e -> handleAddToCart(product));

        card.getChildren().addAll(infoBox, addButton);
        return card;
    }

    private void handleAddToCart(Product product) {
        showAlert("Корзина", "Товар '" + product.getProductName() + "' добавлен в корзину");
    }

    @FXML
    private void handleBack() {
        try {
            Stage currentStage = (Stage) ((Node) productsContainer).getScene().getWindow();
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}