module com.example.kurs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens sayana to javafx.fxml;
    exports sayana;
    exports sayana.Controllers;
    opens sayana.Controllers to javafx.fxml;
}