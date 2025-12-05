package sayana.Controllers;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import sayana.DAO.DbConnection;
import sayana.models.CartItem;
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
import javafx.scene.image.ImageView;
import javafx.scene.image.Image; // ДОБАВИТЬ ЭТОТ ИМПОРТ
import java.io.File;
import java.util.List;
import sayana.models.Address;

public class CartController {

    @FXML private VBox cartItemsContainer;
    @FXML private Text emptyCartText;
    @FXML private VBox totalContainer;
    @FXML private Text totalAmountText;

    private UserType currentUser;
    private DbConnection dbConnection = new DbConnection();

    public void setUser(UserType user) {
        this.currentUser = user;
        if (cartItemsContainer != null) {
            loadCart();
        }
    }

    @FXML
    public void initialize() {
        if (currentUser != null) {
            loadCart();
        }
    }

    private void loadCart() {
        try {
            List<CartItem> cartItems = dbConnection.getUserCart(currentUser.getUserId());
            cartItemsContainer.getChildren().clear();

            if (cartItems.isEmpty()) {
                emptyCartText.setVisible(true);
                totalContainer.setVisible(false);
            } else {
                emptyCartText.setVisible(false);
                totalContainer.setVisible(true);

                double total = 0;

                for (CartItem item : cartItems) {
                    HBox itemCard = createCartItemCard(item);
                    cartItemsContainer.getChildren().add(itemCard);
                    total += item.getTotalPrice();
                }

                totalAmountText.setText(String.format("%.2f руб.", total));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить корзину: " + e.getMessage());
        }
    }

    private HBox createCartItemCard(CartItem item) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; -fx-spacing: 15;");
        card.setMinHeight(120);

        Product product = item.getProduct();

        // Изображение продукта
        ImageView productImageView = new ImageView();
        productImageView.setFitWidth(80);
        productImageView.setFitHeight(80);
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
        infoBox.setSpacing(5);

        Text nameText = new Text(product.getProductName());
        nameText.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-fill: #000000;");

        Text priceText = new Text(String.format("%.2f руб. x %d = %.2f руб.",
                product.getPrice(), item.getQuantity(), item.getTotalPrice()));
        priceText.setStyle("-fx-font-size: 14px; -fx-fill: #000000;");

        HBox controlsBox = new HBox(10);

        Button removeButton = new Button("Удалить");
        removeButton.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; " +
                "-fx-font-size: 12px; -fx-padding: 5 10;");
        removeButton.setOnAction(e -> handleRemoveFromCart(item));

        controlsBox.getChildren().add(removeButton);

        infoBox.getChildren().addAll(nameText, priceText, controlsBox);
        card.getChildren().addAll(productImageView, infoBox);

        return card;
    }


    private void handleRemoveFromCart(CartItem item) {
        try {
            dbConnection.removeFromCart(currentUser.getUserId(), item.getProductId());
            showAlert("Корзина", "Товар удален из корзины");
            loadCart(); // Перезагружаем корзину
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось удалить товар: " + e.getMessage());
        }
    }


    @FXML
    private void handleCheckout() {
        try {
            List<CartItem> cartItems = dbConnection.getUserCart(currentUser.getUserId());
            if (cartItems.isEmpty()) {
                showAlert("Ошибка", "Корзина пуста");
                return;
            }

            // Получаем адрес пользователя (берем первый или запрашиваем новый)
            List<Address> addresses = dbConnection.getUserAddresses(currentUser.getUserId());
            int addressId;

            if (addresses.isEmpty()) {
                // Запрашиваем адрес у пользователя
                addressId = requestAddress();
                if (addressId == -1) {
                    showAlert("Ошибка", "Необходимо указать адрес доставки");
                    return;
                }
            } else {
                addressId = addresses.get(0).getAddressId();
            }

            // Создаем заказ
            boolean success = dbConnection.createOrderWithItems(
                    currentUser.getUserId(),
                    addressId,
                    cartItems
            );

            if (success) {
                showAlert("Заказ оформлен", "Ваш заказ успешно оформлен! Номер заказа будет отправлен вам на email.");

                // Очищаем корзину (уже сделано в транзакции, но перезагружаем)
                loadCart();

                // Предлагаем перейти к заказам
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Заказ оформлен");
                alert.setHeaderText("Заказ успешно оформлен!");
                alert.setContentText("Хотите перейти к просмотру ваших заказов?");

                ButtonType yesButton = new ButtonType("Да", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("Нет", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        openOrdersWindow();
                    }
                });
            } else {
                showAlert("Ошибка", "Не удалось оформить заказ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось оформить заказ: " + e.getMessage());
        }
    }
    private int requestAddress() {
        // Временно возвращаем ID 1 для адреса по умолчанию
        return 1;
    }

    private void openOrdersWindow() {
        try {
            Stage currentStage = (Stage) cartItemsContainer.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/orders-window.fxml"));
            Parent root = loader.load();

            OrdersController ordersController = loader.getController();
            ordersController.setUser(currentUser);

            Stage ordersStage = new Stage();
            ordersStage.setTitle("Мои заказы - Цветочный магазин");
            ordersStage.setScene(new Scene(root, 800, 900));
            ordersStage.setResizable(true);
            ordersStage.setMinWidth(600);
            ordersStage.setMinHeight(700);
            ordersStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            // ИСПРАВЛЕНО: используем cartItemsContainer вместо productsContainer
            Stage currentStage = (Stage) cartItemsContainer.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/main-window.fxml"));
            Parent root = loader.load();

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}