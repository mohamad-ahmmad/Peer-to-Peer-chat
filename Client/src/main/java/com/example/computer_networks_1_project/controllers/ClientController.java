package com.example.computer_networks_1_project.controllers;

import com.example.computer_networks_1_project.ChatApplication;
import com.example.computer_networks_1_project.CustomDatagramSocket;
import com.example.computer_networks_1_project.Peer;
import com.example.computer_networks_1_project.view.MessageUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    private CustomDatagramSocket mySocket;
    private String destinationIP;
    private int destinationPort;

    public void setUDPSocket(CustomDatagramSocket socket){
        this.mySocket = socket;
    }

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
    private VBox messagesContainer;

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
        System.out.println(ChatApplication.logout());
        Stage stage = (Stage) ((Scene)(((Node)event.getSource()).getScene())).getWindow();
        stage.close();
    }

    @FXML
    void sendAll(ActionEvent event) {
        List<Peer> activePeers = ChatApplication.peersBuffer.getActivePeers();
        activePeers.forEach(peer -> {
            try {
                ChatApplication.sendMessage(mySocket, txtMessageBox.getText(), peer.getIP(), peer.getPort());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void sendMessage(ActionEvent event) {
        try {
            String text = txtMessageBox.getText();
            ChatApplication.sendMessage(mySocket, text, destinationIP, destinationPort);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void testConnection(ActionEvent event) {
        labelStatus.setText("Succeeded");
    }

    private boolean flag;
    public void updateMessages(){
        flag = false;
        Platform.runLater(
                this::renderMessages
        );
    }

    private void renderMessages(){
        messagesContainer.getChildren().removeAll(messagesContainer.getChildren());
//        List<Peer> activePeers = ChatApplication.peersBuffer.getActivePeers();
        Peer chat = ChatApplication.peersBuffer.getPeer(new InetSocketAddress(destinationIP, destinationPort));
        chat.getMessagesOrdered().forEach(
                message -> {
                    MessageUI element = new MessageUI(message);
                    messagesContainer.getChildren().add(element);
                }
        );
    }

    public void updatePeersList() { // Platform.runLater() is used since this method is not called by the UI Thread, and this method
        // changes the UI, so it is mandatory to use it.
        Platform.runLater(
                () -> {
                    List<String> peersList = ChatApplication.peersBuffer.getNamesOfActivePeers();
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

    public Button getBtnDelete() {
        return btnDelete;
    }

    @FXML
    private Button btnDelete;

    @FXML
    void deleteMessage(ActionEvent event) {
        MessageUI msgToDelete = (MessageUI) messagesContainer.getChildren()
                .stream()
                .filter(message -> ((MessageUI) message).getMessage().isSelected())
                .findAny()
                .orElse(null);

        System.out.println(msgToDelete.getText());
        int index = messagesContainer.getChildren().indexOf(msgToDelete);

            ChatApplication.deleteMessage(index, currentPeer);




    }

    Peer currentPeer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // load active peers from chatApplication into comboBox
        ChatApplication.waitToLoad.countDown();



        comboBoxPeers.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (!newVal.equals("Select peer")) {
//                    String[] tokens = newVal.split(":");

                        currentPeer = ChatApplication.peersBuffer.getPeer(newVal);
//                    destinationIP = new String(tokens[0]);
//                    destinationPort = Integer.parseInt(tokens[1]);
                        destinationIP = currentPeer.getIP();
                    destinationPort = currentPeer.getPort();

                    txtDestinationIP.setText(destinationIP);
                    txtDestinationPort.setText(String.valueOf(destinationPort));

                    txtSourceIP.setText("127.0.0.1");
                    txtSourcePort.setText(String.valueOf(mySocket.getLocalPort()));
                    renderMessages();
                }
            }
        });

//        comboBoxPeers.getSelectionModel().selectedItemProperty().addListener(
//                (obs, old, newVal) -> {
//                    flag = true;
//                }
//        );
    }
}
