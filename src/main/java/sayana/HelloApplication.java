package sayana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/auth.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 440, 600);
            primaryStage.setTitle("Авторизация - Цветочный магазин");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(500);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка загрузки FXML: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}