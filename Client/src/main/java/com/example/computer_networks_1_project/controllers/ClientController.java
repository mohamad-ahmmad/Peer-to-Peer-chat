package com.example.computer_networks_1_project.controllers;

import com.example.computer_networks_1_project.ChatApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnSend;

    @FXML
    private Button btnSendAll;

    @FXML
    private Button btnTest;

    @FXML
    private ComboBox<String> comboBoxPeers;

    @FXML
    private Label labelStatus;

    @FXML
    private TextField txtDestinationIP;

    @FXML
    private TextField txtDestinationPort;

    @FXML
    private TextField txtMessageBox;

    @FXML
    private TextField txtSourceIP;

    @FXML
    private TextField txtSourcePort;

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void sendAll(ActionEvent event) {

    }

    @FXML
    void sendMessage(ActionEvent event) {

    }

    @FXML
    void testConnection(ActionEvent event) {

    }

    public void updatePeersList() { // Platform.runLater() is used since this method is not called by the UI Thread, and this method
        // changes the UI, so it is mandatory to use it.
        Platform.runLater(
                () -> {
                    List<String> peersList = ChatApplication.peersBuffer.getListOfActivePeers();
                    List<String> currentItems = comboBoxPeers.getItems();

                    Collections.sort(currentItems);
                    for(String peer: peersList) {
                        if (Collections.binarySearch(currentItems, peer) >= 0){
                            continue;
                        }
                        currentItems.add(peer);
                    }
                }
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // load active peers from chatApplication into comboBox
        ChatApplication.waitToLoad.countDown();
    }
}
