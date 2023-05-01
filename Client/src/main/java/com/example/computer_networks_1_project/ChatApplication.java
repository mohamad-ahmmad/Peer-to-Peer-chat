package com.example.computer_networks_1_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;
import java.util.*;


public class ChatApplication extends Application {


    public static Socket clientSocket = new Socket();
    public static boolean status = true;


    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(String.valueOf(ChatApplication.class.getResource("style.css")));
        stage.setResizable(false);
        stage.setTitle("Chat System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // TODO start thread 1 ( listening for incoming messages )
        Thread processIncomingMessages = new Thread(processIncomingMessagesCallback, "process incoming messages");



        // TODO start thread 2 ( pinging the server for new online users )
        Thread pingServer = new Thread(pingServerCallback, "ping server");
//        clientSocket.connect(new InetSocketAddress(IPServer, portServer));

        // TODO start thread 3 ( for sending messages )
        Thread sendMessages = new Thread(sendMessagesCallback, "send messages");

        try {
            login(IPServer, portServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        processIncomingMessages.start();
        pingServer.start(); // this should be called after the user has logged in.
        sendMessages.start();

        launch();

    }

    public static InetAddress IPServer;

    static {
        try {
            IPServer = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static int portServer = 8001;

    public static final int MAX_MESSAGE_SIZE = 2048;
    public static ArrayList<Peer> activePeers = new ArrayList<>();
    public static SharedBuffer peersBuffer = new SharedBuffer();



    public static String login(InetAddress serverIP, int serverPort) throws IOException {
        // TODO sends the server a request to log in with username and password.
        // TODO the message is sent using TCP.

        System.out.println("connecting to server...");
        clientSocket.connect(new InetSocketAddress(serverIP, serverPort));

        Formatter output = new Formatter(clientSocket.getOutputStream());
        System.out.println("Enter username and password: ");
        Scanner keyStream = new Scanner(System.in);

        String username = keyStream.next();
        String password = keyStream.next();
        output.format("sign-in," + username + "," + password + "," + clientSocket.getLocalPort() + "\n"); // change port registered at server
        output.flush();

        Scanner input = new Scanner(clientSocket.getInputStream());


        if(input.hasNext()) {
            String response = input.next();
            clientSocket.close();
            System.out.println(response);
            mySocket = new DatagramSocket(clientSocket.getLocalPort());
            input.close();
            output.close();
            return response;

        }
        clientSocket.close();
        mySocket = new DatagramSocket(clientSocket.getLocalPort());
            input.close();
            output.close();
        return "no response";

    }

    public static DatagramSocket mySocket;
    public static Runnable sendMessagesCallback = () -> {
        Scanner in = new Scanner(System.in);
        // create buffer of message size
        byte[] sendBuffer;

        while (true) {
            // read message from scanner

            System.out.println("d or m");
            String selection = in.next();

            System.out.println("Choose which peer (start from 1): ");
            int peerNumber = in.nextInt();

            Peer peerToSend = peersBuffer.getPeerByIndex(peerNumber - 1);
            if ("m".equals(selection)) {

            System.out.println("Type your message: ");

            String message = in.nextLine();
            if("".equals(message))
            {
                System.out.println("Type your message: ");
                message = in.nextLine();
            }

            try {

                peersBuffer.setPeerMessage(peerToSend, message.replaceAll("^A-z|^0-9", ""), false);

                // add message prefix
                message = "m," + message;
                sendBuffer = message.getBytes();
                DatagramPacket packetSent = new DatagramPacket(sendBuffer, sendBuffer.length, peerToSend.getAddress());
                mySocket.send(packetSent);

            } catch (SocketException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            } else if ("d".equals(selection)) {
                System.out.println("select message number: ");
                int index = Integer.parseInt(in.next());

                peerToSend.getMessagesSent().remove(index);
                String message = "d," + index;
                sendBuffer = message.getBytes();

                DatagramPacket deletePacket = new DatagramPacket(sendBuffer, sendBuffer.length, peerToSend.getAddress());
                try {
                    mySocket.send(deletePacket);

                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }


        }
    };

    List<Peer> list = Collections.synchronizedList(new ArrayList<>());
    public static Runnable processIncomingMessagesCallback = () -> {
        try {

            byte[] dataRecv = new byte[MAX_MESSAGE_SIZE];
            System.out.println("listening for messages...");
            while(true) {

                DatagramPacket incomingPacket = new DatagramPacket(dataRecv, MAX_MESSAGE_SIZE);
                mySocket.receive(incomingPacket);
                String messageReceived = new String(incomingPacket.getData());

                String[] header = messageReceived.split(",");
                if ("m".equals(header[0])) {
                    messageReceived = messageReceived.substring(2);
                    String incomingIP = incomingPacket.getAddress().toString().split("/")[1];
                    peersBuffer.setPeerMessage(incomingIP, incomingPacket.getPort(), messageReceived.replaceAll("^A-z|^0-9", ""), true);

                    System.out.println(incomingPacket.getPort());
                    System.out.println(messageReceived);

                } else if ("d".equals(header[0])) {
                    int index = Integer.parseInt(header[1]);
                    String incomingIP = incomingPacket.getAddress().toString().split("/")[1];
                    peersBuffer.deletePeerMessage(incomingIP, incomingPacket.getPort(), index, true);
                }

            }
        } catch (SocketException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    };

    private static Peer stringToPeer(String peerInfo) {
        String[] tokens = peerInfo.split(",");
        return new Peer(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
    }

    public static Runnable pingServerCallback = () -> {
        while(true) {
            // send a "retrieve-list" message to server to get online users

            try (Socket pingSocket = new Socket()){

                Thread.sleep(1000);
                System.out.println("pinging server");

                pingSocket.connect(new InetSocketAddress(IPServer, portServer));

                Formatter output = new Formatter(pingSocket.getOutputStream());
                Scanner input = new Scanner(pingSocket.getInputStream());
                output.format("retrieve-list,\n");

                output.flush();
                int i = 1;

                List<Peer> onlinePeers = new ArrayList<>();
                while(input.hasNext()){
                    String peerInfo = input.next();
                    onlinePeers.add(stringToPeer(peerInfo));
                }
                peersBuffer.addPeers(onlinePeers);
                peersBuffer.printActivePeers();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    };
}