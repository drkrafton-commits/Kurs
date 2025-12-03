package sayana.Controllers;

import sayana.DAO.DbConnection;
import sayana.models.Product;
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
// ДОБАВЛЕННЫЕ ИМПОРТЫ
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.File;
import java.util.List;

public class FavoritesController {

    @FXML private VBox favoritesContainer;
    @FXML private Text emptyFavoritesText;

    private UserType currentUser;
    private DbConnection dbConnection = new DbConnection();

    public void setUser(UserType user) {
        this.currentUser = user;
        if (favoritesContainer != null) {
            loadFavorites();
        }
    }

    @FXML
    public void initialize() {
        if (currentUser != null) {
            loadFavorites();
        }
    }

    private void loadFavorites() {
        try {
            List<Product> favorites = dbConnection.getUserFavorites(currentUser.getUserId());
            favoritesContainer.getChildren().clear();

            if (favorites.isEmpty()) {
                emptyFavoritesText.setVisible(true);
            } else {
                emptyFavoritesText.setVisible(false);

                for (Product product : favorites) {
                    HBox productCard = createFavoriteProductCard(product);
                    favoritesContainer.getChildren().add(productCard);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить избранное: " + e.getMessage());
        }
    }

    private HBox createFavoriteProductCard(Product product) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15;");
        card.setMinHeight(120);

        // Изображение продукта
        ImageView productImageView = new ImageView();
        productImageView.setFitWidth(100);
        productImageView.setFitHeight(100);
        productImageView.setStyle("-fx-background-radius: 10;");

        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                File imageFile = new File(product.getImageUrl());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    productImageView.setImage(image);
                } else {
                    productImageView.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 10;");
                }
            } catch (Exception e) {
                productImageView.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 10;");
            }
        } else {
            productImageView.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 10;");
        }

        VBox infoBox = new VBox(5);
        infoBox.setPrefWidth(400);

        Text nameText = new Text(product.getProductName());
        nameText.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-fill: #000000;");
        nameText.setWrappingWidth(380);

        Text priceText = new Text(String.format("%.2f руб.", product.getPrice()));
        priceText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1A573A;");

        HBox buttonBox = new HBox(10);

        Button addToCartButton = new Button("В корзину");
        addToCartButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #5C5B61, #1A573A); " +
                "-fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15;");
        addToCartButton.setOnAction(e -> handleAddToCart(product));

        Button removeButton = new Button("Удалить");
        removeButton.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; " +
                "-fx-font-size: 12px; -fx-padding: 8 15;");
        removeButton.setOnAction(e -> handleRemoveFromFavorites(product));

        buttonBox.getChildren().addAll(addToCartButton, removeButton);
        infoBox.getChildren().addAll(nameText, priceText, buttonBox);
        card.getChildren().addAll(productImageView, infoBox);

        return card;
    }

    private void handleAddToCart(Product product) {
        try {
            dbConnection.addToCart(currentUser.getUserId(), product.getProductId(), 1);
            showAlert("Корзина", "Товар добавлен в корзину");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось добавить товар в корзину: " + e.getMessage());
        }
    }

    private void handleRemoveFromFavorites(Product product) {
        try {
            dbConnection.removeFromFavorites(currentUser.getUserId(), product.getProductId());
            showAlert("Избранное", "Товар удален из избранного");
            loadFavorites(); // Перезагружаем список
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось удалить товар: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage currentStage = (Stage) favoritesContainer.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/main-window.fxml"));
            Parent root = loader.load();

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}