module com.example.kurs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.kurs to javafx.fxml;
    exports com.example.kurs;
}