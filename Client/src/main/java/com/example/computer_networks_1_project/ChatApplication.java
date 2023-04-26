package com.example.computer_networks_1_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatApplication extends Application {



    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(String.valueOf(ChatApplication.class.getResource("style.css")));
        stage.setResizable(false);
        stage.setTitle("Chat System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        // start thread 1 ( listening for incoming messages )
        // start thread 2 ( pinging the server for new online users )
    }
}