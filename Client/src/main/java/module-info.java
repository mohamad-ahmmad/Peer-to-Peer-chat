module com.example.computer_networks_1_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens com.example.computer_networks_1_project to javafx.fxml;
    opens com.example.computer_networks_1_project.controllers to javafx.fxml;
    opens com.example.computer_networks_1_project.requests to javafx.fxml;

    exports com.example.computer_networks_1_project;
    exports com.example.computer_networks_1_project.controllers;
    exports com.example.computer_networks_1_project.requests;
}