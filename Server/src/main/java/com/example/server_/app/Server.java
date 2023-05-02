package com.example.server_.app;

import com.example.server_.events.DBLoadedEvent;
import com.example.server_.events.EventHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends ServerSocket {

    private static Server server = null;
    private int port;
    public static final List<User> users = new ArrayList<>();

    private Server(int port) throws IOException {
        super(port);
        this.port = port;

    }

    public static Server getInstance(int port) throws IOException {
        if(server == null)
            server = new Server(port);

        return server;
    }

    //Events the server offer:
    public static EventHandler userCon = null;
    //Fired when the data is loaded from db.txt
    private static EventHandler dbLoaded = null;

    //Fired when the user disconnected
    public static EventHandler userDiscon = null;


    public void onUserDisconnected(EventHandler handler){userDiscon=handler;}
    public void onUserConnected(EventHandler handler){
        userCon=handler;
    }
    public void onDBLoaded(EventHandler handler){
        dbLoaded=handler;
    }


    public void handleConnection(Socket con){
        HandleConnection threadHandlerConnection = new HandleConnection(con);
        Thread th = new Thread(threadHandlerConnection);
        th.start();
    }

    private void initDB() throws FileNotFoundException {
        Scanner scan = new Scanner(new File("db.txt"));
        users.clear();
        while (scan.hasNext()){
            String line = scan.next();
            String[] data = line.split(",");
            User temp = new User(data[0], data[1]);
            users.add(temp);
        }
        if(dbLoaded != null) dbLoaded.handle(new DBLoadedEvent(users));
        scan.close();
    }

    public void start(){
        try{
            initDB();
            System.out.println("Server started at port: "+port);
            Socket client;
            while(true){
                client = this.accept();
                System.out.println("Client connected");
                handleConnection(client);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteAllInterfaces()  {

            Server.userCon = null;
            Server.userDiscon = null;
            Server.dbLoaded = null;


    }
}
