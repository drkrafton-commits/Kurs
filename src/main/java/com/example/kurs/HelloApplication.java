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
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/kurs/auth-window.fxml"));
            Scene scene = new Scene(root, 440, 600); // Уменьшена высота
            primaryStage.setTitle("Авторизация");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            // Центрируем окно на экране
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}