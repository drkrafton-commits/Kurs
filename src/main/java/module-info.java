module com.example.kurs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens sayana to javafx.fxml;
    exports sayana;
    exports sayana.Controllers;
    opens sayana.Controllers to javafx.fxml;
    exports sayana.models;
    opens sayana.models to javafx.fxml;
    exports sayana.DAO;
    opens sayana.DAO to javafx.fxml;
}