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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/auth-window.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 440, 600);
            primaryStage.setTitle("Авторизация");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true); // Разрешаем изменение размера
            primaryStage.setMinWidth(400); // Минимальная ширина
            primaryStage.setMinHeight(500); // Минимальная высота
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