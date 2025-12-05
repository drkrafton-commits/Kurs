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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.File;
import java.util.List;

public class FlowersController {

    @FXML private VBox flowersContainer;

    private UserType currentUser;
    private DbConnection dbConnection = new DbConnection();

    @FXML
    public void initialize() {
        loadFlowers();
    }

    public void setUser(UserType user) {
        this.currentUser = user;
        if (flowersContainer != null) {
            loadFlowers();
        }
    }

    private void loadFlowers() {
        try {
            List<Product> flowers = dbConnection.getProductsByCategory(1);
            flowersContainer.getChildren().clear();

            for (Product flower : flowers) {
                HBox productCard = createProductCard(flower);
                flowersContainer.getChildren().add(productCard);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить цветы: " + e.getMessage());
        }
    }

    private HBox createProductCard(Product product) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15;");
        card.setMinHeight(120);

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

        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            Text descText = new Text(product.getDescription());
            descText.setStyle("-fx-font-size: 12px; -fx-fill: #666666;");
            descText.setWrappingWidth(380);
            infoBox.getChildren().addAll(nameText, descText, priceText);
        } else {
            infoBox.getChildren().addAll(nameText, priceText);
        }

        HBox buttonBox = new HBox(10);
        buttonBox.setStyle("-fx-padding: 10 0 0 0;");

        Button addToCartButton = new Button("В корзину");
        addToCartButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #5C5B61, #1A573A); " +
                "-fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15; -fx-background-radius: 5;");
        addToCartButton.setOnAction(e -> handleAddToCart(product));

        Button favoriteButton = new Button("♥");
        favoriteButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #ff6b6b; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: #ff6b6b; " +
                "-fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12;");
        favoriteButton.setOnAction(e -> handleToggleFavorite(product));

        try {
            if (currentUser != null && dbConnection.isFavorite(currentUser.getUserId(), product.getProductId())) {
                favoriteButton.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; " +
                        "-fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: #ff6b6b; " +
                        "-fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12;");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        buttonBox.getChildren().addAll(addToCartButton, favoriteButton);
        infoBox.getChildren().add(buttonBox);

        card.getChildren().addAll(productImageView, infoBox);
        return card;
    }

    private void handleAddToCart(Product product) {
        if (currentUser == null) {
            showAlert("Ошибка", "Войдите в систему, чтобы добавить в корзину");
            return;
        }

        try {
            dbConnection.addToCart(currentUser.getUserId(), product.getProductId(), 1);
            showAlert("Корзина", "Товар '" + product.getProductName() + "' добавлен в корзину");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось добавить товар в корзину: " + e.getMessage());
        }
    }

    private void handleToggleFavorite(Product product) {
        if (currentUser == null) {
            showAlert("Ошибка", "Войдите в систему, чтобы добавить в избранное");
            return;
        }

        try {
            if (dbConnection.isFavorite(currentUser.getUserId(), product.getProductId())) {
                dbConnection.removeFromFavorites(currentUser.getUserId(), product.getProductId());
                showAlert("Избранное", "Товар удален из избранного");
            } else {
                dbConnection.addToFavorites(currentUser.getUserId(), product.getProductId());
                showAlert("Избранное", "Товар добавлен в избранное");
            }
            loadFlowers();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось обновить избранное: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage currentStage = (Stage) flowersContainer.getScene().getWindow();
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
    private void handleCart() {
        if (currentUser == null) {
            showAlert("Ошибка", "Войдите в систему, чтобы просмотреть корзину");
            return;
        }

        try {
            Stage currentStage = (Stage) flowersContainer.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/cart.fxml"));
            Parent root = loader.load();

            CartController cartController = loader.getController();
            cartController.setUser(currentUser);

            Stage cartStage = new Stage();
            cartStage.setTitle("Корзина - Цветочный магазин");
            cartStage.setScene(new Scene(root, 800, 900));
            cartStage.setResizable(true);
            cartStage.setMinWidth(600);
            cartStage.setMinHeight(700);
            cartStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть корзину: " + e.getMessage());
        }
    }

    @FXML
    private void handleFavorites() {
        if (currentUser == null) {
            showAlert("Ошибка", "Войдите в систему, чтобы просмотреть избранное");
            return;
        }

        try {
            Stage currentStage = (Stage) flowersContainer.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/favorites-window.fxml"));
            Parent root = loader.load();

            FavoritesController favoritesController = loader.getController();
            favoritesController.setUser(currentUser);

            Stage favoritesStage = new Stage();
            favoritesStage.setTitle("Избранное - Цветочный магазин");
            favoritesStage.setScene(new Scene(root, 800, 900));
            favoritesStage.setResizable(true);
            favoritesStage.setMinWidth(600);
            favoritesStage.setMinHeight(700);
            favoritesStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть избранное: " + e.getMessage());
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