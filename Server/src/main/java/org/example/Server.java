package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class HandleConnection implements Runnable {

    private Socket con ;
    public HandleConnection (Socket con){
        this.con=con;
    }


    public void signIn(String username, String password){

            Server.count++;
            System.out.println(Server.count);

    }
    public void signUp(String username, String password){
        synchronized (Server.users){

        }
    }
    public String retrieveList(){
            StringBuilder res = new StringBuilder("");

            for(User user : Server.users){
                if(user.isActive())
                    res.append(user.retrieve()).append("\n");
            }
            return res.toString();
    }
    public String handleRequest(String[] prompts ){
        String res = null;
        if(Objects.equals(prompts[0],"sign-in")){
            System.out.println("Sign-in process");
        }else if(Objects.equals(prompts[0],"sign-up")){
            System.out.println("Sign-up process");
        }else if(Objects.equals(prompts[0],"retrieve-list")){
            System.out.println("retrieve process");

        }else{
            res="failed";
        }

        return res;
    }
    @Override
    public void run() {
        try {
            Scanner scan = new Scanner(con.getInputStream());
            String str = scan.next();

            String[] prompts = str.split(",");
            String response = handleRequest(prompts);

            OutputStream out = con.getOutputStream();
            out.write("Successfully.\r".getBytes());
            out.flush();

            out.close();
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                con.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

public class Server extends ServerSocket {

    private static Server server = null;
    private int port;
    public static final List<User> users = new ArrayList<>();
    public static Integer count =0;

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



}
