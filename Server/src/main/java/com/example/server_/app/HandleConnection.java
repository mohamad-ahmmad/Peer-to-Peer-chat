package com.example.server_.app;

import com.example.server_.events.EventHandler;
import com.example.server_.events.UserConnectedEvent;
import com.example.server_.events.UserDisconnectedEvent;


import java.io.*;
import java.net.Socket;
import java.util.*;

import static com.example.server_.app.Server.*;


class HandleConnection implements Runnable {

    private Socket con ;
    public HandleConnection (Socket con){
        this.con=con;
    }

    public User authUser(String username, String password){
        for(User user : Server.users)
            if(Objects.equals(username, user.getName())&&Objects.equals(password, user.getPassword()))
                return user;

        return null;
    }

    public String signIn(String username, String password, String port){
        User user = authUser(username, password);

        if(!Objects.equals(user,null)) {
            user.setActive(true);

            //The format of the address /ip:port,
            //Filter the ip
            user.setIp(con.getRemoteSocketAddress().toString().split(":")[0].replace("/", ""));
            user.setPort(port);

            //Checking for the handler of the sign-in event the handlers interfaces in the server
            //but the handler implementation in the Controller- Class
            if (Server.userCon != null) Server.userCon.handle(new UserConnectedEvent(user));
            return "ok";
        }
        return "failed";
    }
    public String signUp(String username, String password,String port){
        synchronized (Server.users){
            User newUser = new User(username,password);
            for(User user : Server.users)
                if(user.equals(newUser)){
                    return "failed";
                }
            newUser.setPort(port);
            newUser.setIp(con.getRemoteSocketAddress().toString());
            Server.users.add(newUser);
            //Execute the event handler for connecting new user.
            if(Server.userCon != null) userCon.handle(new UserConnectedEvent(newUser));
            return "ok";
        }
    }
    public String retrieveList(){
        StringBuilder res = new StringBuilder("");

        for(User user : Server.users)
            if(user.isActive())
                res.append(user.retrieve()).append("\n");

        return res.toString();
    }
    private String logout(String username, String password) {
        User user = authUser(username, password);
        if(user != null){
            //Fire up the handler for ui
            if(userDiscon != null) userDiscon.handle(new UserDisconnectedEvent(user));
            return "ok";
        }
        return "failed";
    }
    public String handleRequest(String[] prompts ){
        String res = null;
        /*
        =>prompts[0] => request-type
        =>prompts[1] => username
        =>prompts[2] => password
        =>prompts[3] => port
         */
        if(Objects.equals(prompts[0],"sign-in"))
            res =signIn(prompts[1], prompts[2], prompts[3]);
        else if(Objects.equals(prompts[0],"sign-up"))
            res =signUp(prompts[1], prompts[2], prompts[3]);
        else if(Objects.equals(prompts[0],"retrieve-list"))
            res= retrieveList();
        else if(Objects.equals(prompts[0], "log-out"))
            res= logout(prompts[1], prompts[2]);
        else
            res="failed";


        return res;
    }



    @Override
    public void run() {
        try {
            //Get the request from the client.
            Scanner scan = new Scanner(con.getInputStream());
            String str = scan.next();

            String[] prompts = str.split(",");
            //Handling the request
            System.out.println(prompts[0]);
            String response = handleRequest(prompts);
            //Send back the response to the client
            Formatter formatter = new Formatter(con.getOutputStream());
            formatter.format(response+"\r");
            formatter.flush();

            //close the connections
            formatter.close();
            scan.close();
            System.out.println("Client disconnected");

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
