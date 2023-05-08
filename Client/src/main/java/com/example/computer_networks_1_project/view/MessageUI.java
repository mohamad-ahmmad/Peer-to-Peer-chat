package com.example.computer_networks_1_project.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MessageUI extends HBox {

    public MessageUI(String content){
        this.getChildren().add(new Label(content));
    }
}
