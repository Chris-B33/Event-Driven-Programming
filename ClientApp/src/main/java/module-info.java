module com.mycompany.clientapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.clientapp to javafx.fxml;
    exports com.mycompany.clientapp;
}
