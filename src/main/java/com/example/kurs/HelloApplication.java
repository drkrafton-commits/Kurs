package com.example.kurs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Загружаем FXML файл
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kurs/auth-window.fxml"));
            Parent root = loader.load();

            // Создаем сцену
            Scene scene = new Scene(root, 440, 956);

            // Настраиваем окно
            primaryStage.setTitle("Kurs - Авторизация");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка загрузки", "Не удалось загрузить интерфейс: " + e.getMessage());
        }
    }

    private void showErrorAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Запускаем JavaFX приложение
        launch(args);
    }
}