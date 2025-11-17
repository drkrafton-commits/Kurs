package sayana.Controllers;

import sayana.models.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController {

//    @FXML private Label userInfoLabel;
//    @FXML private Text welcomeText;
//    @FXML private javafx.scene.control.Button logoutButton;
//
//    private UserType currentUser;
//
//    public void setUser(UserType user) {
//        this.currentUser = user;
//        updateUserInfo();
//    }
//
//    @FXML
//    public void initialize() {
//        if (currentUser != null) {
//            updateUserInfo();
//        }
//    }
//
//    private void updateUserInfo() {
//        if (currentUser != null) {
//            userInfoLabel.setText("ФИО: " + currentUser.getFio() +
//                    "\nЛогин: " + currentUser.getLogin() +
//                    "\nРоль: " + currentUser.getRole());
//            welcomeText.setText("Добро пожаловать, " + currentUser.getFio() + "!");
//        }
//    }
//
//    @FXML
//    private void handleLogout() {
//        try {
//            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
//            currentStage.close();
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sayana/auth-window.fxml"));
//            Parent root = loader.load();
//
//            Stage authStage = new Stage();
//            authStage.setTitle("Авторизация");
//            authStage.setScene(new Scene(root, 440, 600));
//            authStage.setResizable(false);
//            authStage.show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}