package com.example.server_;

import com.example.server_.events.EventHandler;
import com.example.server_.events.UserConnectedEvent;


import java.io.*;
import java.net.Socket;
import java.util.*;

import static com.example.server_.Server.users;


class HandleConnection implements Runnable {

    private Socket con ;
    public HandleConnection (Socket con){
        this.con=con;
    }




    public void onUserDisconnected(){

    }

    public String signIn(String username, String password, String port){
        for(User user : users){

            if(Objects.equals(username, user.getName())&&Objects.equals(password, user.getPassword()))
            {
                user.setActive(true);

                //The format of the address /ip:port,
                // so I did some manipulation to the string and save it in proper way.
                user.setIp(con.getRemoteSocketAddress().toString().split(":")[0].replace("/",""));
                user.setPort(port);

                //Checking for the handler of the sign-in event the handlers interfaces in the server
                //but the handler implementation in the Controller- Class
                if(Server.userCon != null) Server.userCon.handle(new UserConnectedEvent(user));
                return "ok";
            }
        }

        return "failed";
    }
    public String signUp(String username, String password,String port){
        synchronized (users){
            User newUser = new User(username,password);
            for(User user : users)
                if(user.equals(newUser)){
                    return "failed";
                }
            newUser.setPort(port);
            newUser.setIp(con.getRemoteSocketAddress().toString());
            users.add(newUser);
            return "ok";
        }
    }
    public String retrieveList(){
        StringBuilder res = new StringBuilder("");

        for(User user : users)
            if(user.isActive())
                res.append(user.retrieve()).append("\n");

        return res.toString();
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
