package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class HandleConnection implements Runnable {

    private Socket con ;
    public HandleConnection (Socket con){
        this.con=con;
    }

    @Override
    public void run() {
        try {
            Scanner scan = new Scanner(con.getInputStream());
            String str = scan.next();
            scan.close();
            System.out.println(str);
            con.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

public class Server extends ServerSocket {

    private static Server server = null;
    private int port;
    List<User> users = new ArrayList<>();
    private Server(int port) throws IOException {
        super(port);
        this.port = port;

    }


    public static Server getInstance(int port) throws IOException {
        if(server == null)
            server = new Server(port);

        return server;
    }


    public void handleConnection(Socket con){
        HandleConnection threadHandlerConnection = new HandleConnection(con);
        Thread th = new Thread(threadHandlerConnection);
        th.start();
    }




    private void initDB() throws FileNotFoundException {
        Scanner scan = new Scanner(new File("db.txt"));

        while (scan.hasNext()){
            String line = scan.next();
            String[] data = line.split(",");
            users.add(new User(data[0], data[1]));
        }
        scan.close();
    }

    public void start(){
        try{
            initDB();
            System.out.println("Server started at port: "+port);
            while(true){
                Socket client = this.accept();
                System.out.println("Client connected");
                handleConnection(client);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



}
