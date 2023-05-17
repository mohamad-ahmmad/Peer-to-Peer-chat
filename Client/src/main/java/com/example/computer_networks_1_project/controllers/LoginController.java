package com.example.computer_networks_1_project.controllers;

import com.example.computer_networks_1_project.ChatApplication;
import com.example.computer_networks_1_project.CustomDatagramSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class LoginController {

    public CustomDatagramSocket getMySocket() {
        return mySocket;
    }

    public void setUDPSocket(CustomDatagramSocket mySocket) {
        this.mySocket = mySocket;
    }

    private CustomDatagramSocket mySocket;


    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;


    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtIP;

    @FXML
    void openSignUp(MouseEvent event) throws IOException {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader(ChatApplication.class.getResource("signup.fxml"));
        Parent root = loader.load();
        ((SignUpController)loader.getController()).setUDPSocket(mySocket);
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.showAndWait();
    }
    @FXML
    void login(ActionEvent event)  {
        try {
            InetAddress IP = InetAddress.getByName(txtIP.getText());
            int port = Integer.parseInt(txtPort.getText());

            String response = ChatApplication.login(IP, port, txtUsername.getText(), txtPassword.getText());

            Stage stage = (Stage) ((Scene)(((Node)event.getSource()).getScene())).getWindow();
            stage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("client.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ((ClientController)fxmlLoader.getController()).setUDPSocket(mySocket);

            Stage appStage = new Stage();
            appStage.setScene(scene);
            ChatApplication.fxmlLoader = fxmlLoader;

            appStage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}
