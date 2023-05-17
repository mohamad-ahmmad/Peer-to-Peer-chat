package com.example.computer_networks_1_project.controllers;

import com.example.computer_networks_1_project.ChatApplication;
import com.example.computer_networks_1_project.CustomDatagramSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.InetSocketAddress;
import java.util.Objects;

public class SignUpController {

    public void setUDPSocket(CustomDatagramSocket mySocket) {
        this.mySocket = mySocket;
    }

    private CustomDatagramSocket mySocket;


    @FXML
    private TextField txtConfirm;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;


    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtIP;

    private boolean validateInput(){
        String password = txtPassword.getText();
        String confirm = txtConfirm.getText();

        return Objects.equals(confirm, password);
    }


    @FXML
    private Label labelStatus;
    @FXML
    void signUp(ActionEvent event) {

        while (!validateInput()) {
            labelStatus.setText("confirm password and password are not identical");
        }

        InetSocketAddress serverAddress = new InetSocketAddress(txtIP.getText(), Integer.parseInt(txtPort.getText()));
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        int clientPort = mySocket.getLocalPort();

        String response = ChatApplication.signup(username, clientPort, password, serverAddress);
        if ("ok".equals(response)){
            labelStatus.setText("close this window and login");
        } else {
            labelStatus.setText(response);
        }
    }

}
