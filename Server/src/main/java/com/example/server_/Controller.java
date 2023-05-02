package com.example.server_;

import com.example.server_.app.Server;
import com.example.server_.app.User;
import com.example.server_.events.DBLoadedEvent;
import com.example.server_.events.UserConnectedEvent;
import com.example.server_.events.UserDisconnectedEvent;
import com.example.server_.view.LogView;
import com.example.server_.view.UserBarView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
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

    private UserBarView findUserBarFromUser(User user){
        for(Node node : users.getChildren()){
            UserBarView bar = (UserBarView)node;
            if(user.getName().equals(bar.getName()))
                return bar;

        }

        return null;
    }
    private void addMessageToTheLog(String message){
        Platform.runLater(()->{
            log.getChildren().add(new Text(message));
        });
    }
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
            //Starting the server and implements the handlers
            mainServer = new Thread(
                    new Runnable() {

                        @Override
                        public void run() {
                            try {

                                Server server = Server.getInstance(serverPort);

                                //Handler for connecting any user
                                server.onUserConnected(event1 ->{
                                    User user = ((UserConnectedEvent)event1).getUser();
                                    UserBarView bar = findUserBarFromUser(user);
                                    if(user != null){
                                        addMessageToTheLog(" "+user.getName()+" have connected.");
                                        bar.setConnected(true);
                                        bar.setIP(user.getIp());
                                        bar.setPort(user.getPort());
                                        return;
                                    }
                                    //Add if the user doesn't on the list
                                    Platform.runLater(()->{
                                        users.getChildren().add(new UserBarView(user.getName(), user.getIp(), user.getPort()));
                                    });
                                });
                                //handler when the db is loaded
                                server.onDBLoaded(event1 -> {
                                    DBLoadedEvent data = (DBLoadedEvent) event1;
                                    List<User> dbList= data.getUsers();

                                    for(User user: dbList){

                                        Platform.runLater(()->{
                                            users.getChildren().add(new UserBarView(user.getName()));
                                        });
                                    }
                                });

                                //Hander when the user logout
                                server.onUserDisconnected(event1 -> {
                                    User user = ((UserDisconnectedEvent)event1).getUser();
                                    UserBarView bar = findUserBarFromUser(user);
                                    addMessageToTheLog(" "+user.getName()+" have disconnected.");
                                    bar.setConnected(false);
                                    bar.setIP("");
                                    bar.setPort("");


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
            status.setText("Running on Address: 127.0.0.1 Port: "+port.getText());
        }else{

            mainServer.interrupt();
            Server.deleteAllInterfaces();
            users.getChildren().clear();
            log.getChildren().clear();
            listen.setText("Start listening");
            status.setText("Stopped");
            System.out.println("Disconnected");

        }
    }

}
