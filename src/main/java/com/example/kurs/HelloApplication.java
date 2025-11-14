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

            // Создаем сцену с новыми размерами 440x956
            Scene scene = new Scene(root, 440, 956);

            // Настраиваем окно
            primaryStage.setTitle("Kurs - Авторизация");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            // Простой вывод ошибки в консоль
            System.err.println("Ошибка загрузки FXML: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}