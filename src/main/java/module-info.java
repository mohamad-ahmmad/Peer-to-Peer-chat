module com.example.computer_networks_1_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.computer_networks_1_project to javafx.fxml;
    exports com.example.computer_networks_1_project;
}