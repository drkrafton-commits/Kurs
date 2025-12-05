package sayana.Controllers;

import sayana.DAO.DbConnection;
import sayana.models.Product;
import sayana.models.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class AdminMenuController {

    @FXML private VBox productsContainer;
    @FXML private ComboBox<Integer> categoryComboBox;
    @FXML private TextField productNameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField priceField;
    @FXML private TextField compositionField;
    @FXML private ImageView productImageView;
    @FXML private Text imagePathText;

    private UserType currentUser;
    private DbConnection dbConnection = new DbConnection();
    private File selectedImageFile;

    public void setUser(UserType user) {
        this.currentUser = user;
        if (productsContainer != null) {
            loadAllProducts();
            loadCategories();
        }
    }

    @FXML
    public void initialize() {
        if (currentUser != null) {
            loadAllProducts();
            loadCategories();
        }
    }

    private void loadCategories() {
        try {
            // Загружаем категории 1-3 (цветы, растения, доставка)
            for (int i = 1; i <= 3; i++) {
                categoryComboBox.getItems().add(i);
            }
            categoryComboBox.getSelectionModel().selectFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllProducts() {
        try {
            // Загружаем все продукты из всех категорий
            productsContainer.getChildren().clear();

            for (int categoryId = 1; categoryId <= 3; categoryId++) {
                List<Product> products = dbConnection.getProductsByCategory(categoryId);
                for (Product product : products) {
                    HBox productCard = createProductCard(product);
                    productsContainer.getChildren().add(productCard);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить продукты: " + e.getMessage());
        }
    }

    private HBox createProductCard(Product product) {
        HBox card = new HBox();
        card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; -fx-spacing: 15;");

        // Загружаем изображение продукта
        ImageView productImage = new ImageView();
        productImage.setFitWidth(80);
        productImage.setFitHeight(80);
        productImage.setStyle("-fx-background-radius: 10;");

        try {
            // Пытаемся загрузить изображение продукта
            String imageUrl = dbConnection.getProductImageUrl(product.getProductId());
            if (imageUrl != null && !imageUrl.isEmpty()) {
                File imageFile = new File(imageUrl);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    productImage.setImage(image);
                }
            }
        } catch (Exception e) {
            // Используем заглушку если нет изображения
            productImage.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 10;");
        }

        VBox infoBox = new VBox();
        infoBox.setSpacing(5);

        Text nameText = new Text(product.getProductName());
        nameText.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-fill: #000000;");

        Text priceText = new Text(product.getPrice() + " руб.");
        priceText.setStyle("-fx-font-size: 14px; -fx-fill: #000000;");

        Text categoryText = new Text("Категория: " + product.getCategoryId());
        categoryText.setStyle("-fx-font-size: 12px; -fx-fill: #666666;");

        HBox buttonBox = new HBox(10);

        Button editButton = new Button("Изменить");
        editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                "-fx-font-size: 12px; -fx-padding: 5 10;");
        editButton.setOnAction(e -> handleEditProduct(product));

        Button deleteButton = new Button("Удалить");
        deleteButton.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; " +
                "-fx-font-size: 12px; -fx-padding: 5 10;");
        deleteButton.setOnAction(e -> handleDeleteProduct(product));

        buttonBox.getChildren().addAll(editButton, deleteButton);
        infoBox.getChildren().addAll(nameText, priceText, categoryText, buttonBox);
        card.getChildren().addAll(productImage, infoBox);

        return card;
    }

    @FXML
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение продукта");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );

        selectedImageFile = fileChooser.showOpenDialog(productImageView.getScene().getWindow());

        if (selectedImageFile != null) {
            try {
                // Показываем превью изображения
                Image image = new Image(selectedImageFile.toURI().toString());
                productImageView.setImage(image);
                imagePathText.setText("Файл: " + selectedImageFile.getName());
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Ошибка", "Не удалось загрузить изображение");
            }
        }
    }

    @FXML
    private void handleAddProduct() {
        try {
            // Проверяем заполнение обязательных полей
            if (productNameField.getText().isEmpty() || priceField.getText().isEmpty()) {
                showAlert("Ошибка", "Заполните название и цену продукта");
                return;
            }

            int categoryId = categoryComboBox.getValue();
            String productName = productNameField.getText();
            String description = descriptionField.getText();
            double price = Double.parseDouble(priceField.getText());
            String composition = compositionField.getText();

            // Добавляем продукт в базу данных
            boolean success = dbConnection.addProduct(categoryId, productName, description, price, composition);

            if (success) {
                // Получаем ID добавленного продукта
                int productId = dbConnection.getLastProductId();

                // Сохраняем изображение если оно выбрано
                if (selectedImageFile != null) {
                    saveProductImage(productId);
                }

                showAlert("Успех", "Продукт успешно добавлен");
                clearForm();
                loadAllProducts();
            } else {
                showAlert("Ошибка", "Не удалось добавить продукт");
            }
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректную цену");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Ошибка при добавлении продукта: " + e.getMessage());
        }
    }

    private void saveProductImage(int productId) throws Exception {
        // Создаем папку для изображений если её нет
        File imagesDir = new File("product_images");
        if (!imagesDir.exists()) {
            imagesDir.mkdir();
        }

        // Копируем файл в папку с изображениями
        String fileName = "product_" + productId + "_" + System.currentTimeMillis() +
                getFileExtension(selectedImageFile.getName());
        File destinationFile = new File(imagesDir, fileName);

        Files.copy(selectedImageFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Сохраняем путь к изображению в базу данных
        dbConnection.saveProductImage(productId, "product_images/" + fileName);
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot);
        }
        return ".jpg";
    }

    private void handleEditProduct(Product product) {
        // Заполняем форму данными продукта для редактирования
        categoryComboBox.setValue(product.getCategoryId());
        productNameField.setText(product.getProductName());
        descriptionField.setText(product.getDescription());
        priceField.setText(String.valueOf(product.getPrice()));
        compositionField.setText(product.getComposition());

        // Загружаем изображение продукта если есть
        try {
            String imageUrl = dbConnection.getProductImageUrl(product.getProductId());
            if (imageUrl != null && !imageUrl.isEmpty()) {
                File imageFile = new File(imageUrl);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    productImageView.setImage(image);
                    imagePathText.setText("Файл: " + imageFile.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Меняем кнопку на "Сохранить изменения"
        // (можно добавить флаг редактирования и ID редактируемого продукта)
    }

    private void handleDeleteProduct(Product product) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Подтверждение удаления");
        confirmAlert.setHeaderText("Удалить продукт?");
        confirmAlert.setContentText("Вы уверены, что хотите удалить продукт \"" + product.getProductName() + "\"?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean success = dbConnection.deleteProduct(product.getProductId());
                    if (success) {
                        showAlert("Успех", "Продукт удален");
                        loadAllProducts();
                    } else {
                        showAlert("Ошибка", "Не удалось удалить продукт");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Ошибка", "Ошибка при удалении: " + e.getMessage());
                }
            }
        });
    }

    private void clearForm() {
        productNameField.clear();
        descriptionField.clear();
        priceField.clear();
        compositionField.clear();
        productImageView.setImage(null);
        imagePathText.setText("Изображение не выбрано");
        selectedImageFile = null;
        categoryComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleBack() {
        try {
            Stage currentStage = (Stage) productsContainer.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/main.fxml"));
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