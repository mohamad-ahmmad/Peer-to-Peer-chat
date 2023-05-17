package com.example.computer_networks_1_project.view;

import com.example.computer_networks_1_project.ChatApplication;
import com.example.computer_networks_1_project.Message;
import com.example.computer_networks_1_project.controllers.ClientController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.event.MouseEvent;

public class MessageUI extends VBox {
    private Label date;
    private Text text;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected = false;
    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    private boolean direction;

    public Label getDate() {
        return date;
    }

    public void setDate(Label date) {
        this.date = date;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    private Message message;
    public MessageUI(Message message) {
        this.message = message;
        this.setSpacing(10);
        this.setPadding(new Insets(5, 10, 10, 10));
        String color = !message.isDirection() ? "none" : "#AFA79F";
        this.setStyle("-fx-background-color: "+ color +"; -fx-border-radius: 10;");

        Button btn = ((ClientController)ChatApplication.fxmlLoader.getController()).getBtnDelete();


        this.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
            ((VBox)this.getParent())
                    .getChildren()
                    .forEach(element -> {
                        ((MessageUI) element).selected = false;
                        ((MessageUI) element).message.setSelected(false);
                    });

            this.selected = !this.selected;
            this.message.setSelected(!this.message.isSelected());
            btn.setDisable(!selected);
            System.out.println("clickeddddd");
        });

        date = new Label(message.getDate().toString());

        VBox.setMargin( this.date, new Insets( 0, 0, 5, 0 ) );
        text = new Text(message.getContent());

        this.getChildren().addAll(date, text);
    }
}
