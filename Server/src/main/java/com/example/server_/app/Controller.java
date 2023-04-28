package com.example.server_.app;

import com.example.server_.Server;
import com.example.server_.User;
import com.example.server_.events.DBLoadedEvent;
import com.example.server_.events.Event;
import com.example.server_.events.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Controller {

    private static int serverPort=-1;
    private Thread mainServer = null;
    @FXML
    private Button listen;

    @FXML
    private VBox log;

    @FXML
    private TextField port;

    @FXML
    private Text status;

    @FXML
    private VBox users;

    @FXML
    void onClickListen(ActionEvent event) {
        if(Objects.equals(listen.getText(), "Start listening")) {
            //Getting&Checking the port.
            try {
                serverPort = Integer.parseInt(this.port.getText());
            } catch (Exception e) {
                status.setText("Invalid port number");
                e.printStackTrace();
                return;
            }

            mainServer = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {

                                Server server = Server.getInstance(serverPort);
                                server.onUserConnected(event1 ->{

                                });

                                server.onDBLoaded(event1 -> {
                                    DBLoadedEvent data = (DBLoadedEvent) event1;
                                    List<User> users = data.getUsers();

                                    for(User user: users){

                                    }
                                });

                                server.start();


                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
            );
            mainServer.start();

            listen.setText("Stop listening");
        }else{
            mainServer.interrupt();
            listen.setText("Start listening");
        }
    }

}
