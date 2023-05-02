package com.example.server_.view;

import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class LogView extends FlowPane {

    private String message;
    public LogView(String message) {
        super();
        this.getChildren().add(new Text(message));
        this.message=message;

    }

    public String getMessage() {
        return message;
    }
}
