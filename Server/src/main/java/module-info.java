module com.example.server_ {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.server_.app to javafx.fxml;
    exports com.example.server_;
    exports com.example.server_.app;
}