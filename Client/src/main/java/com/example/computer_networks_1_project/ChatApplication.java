package com.example.computer_networks_1_project;

import com.example.computer_networks_1_project.controllers.ClientController;
import com.example.computer_networks_1_project.controllers.LoginController;
import com.example.computer_networks_1_project.requests.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;


public class ChatApplication extends Application {
    public static boolean status = false;
    public static FXMLLoader fxmlLoader;
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        System.out.println("fxml loaded");
//        ((ClientController)fxmlLoader.getController()).setUDPSocket(mySocket);

        ((LoginController)fxmlLoader.getController()).setUDPSocket(mySocket);
//        scene.getStylesheets().add(String.valueOf(ChatApplication.class.getResource("style.css")));
        stage.setResizable(false);
        stage.setTitle("Chatty");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        try {
            mySocket = new CustomDatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public static SharedBuffer peersBuffer = new SharedBuffer();

    public static String signup(String username, int clientPort, String password, InetSocketAddress serverAddress) {
        Request r = new SignUpRequest.SignUpRequestBuilder()
                .setUsername(username)
                .setPort(clientPort)
                .setPassword(password)
                .build();

        try (Socket clientSocket = new Socket()) {
            clientSocket.connect(serverAddress);

            Formatter out = new Formatter(clientSocket.getOutputStream());

            out.format(r.toString());
            out.flush();

            Scanner input = new Scanner(clientSocket.getInputStream());

            String returnValue = "no response";

            if (input.hasNext()) {
                returnValue = input.next();
            }
            clientSocket.close();
            input.close();
            out.close();

            return returnValue;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void sendMessage(CustomDatagramSocket mySocket, String message, String destinationIP, int destinationPort) throws IOException {

        InetSocketAddress a = new InetSocketAddress(destinationIP, destinationPort);
        Peer peerToSend = peersBuffer.getPeer(a);
        peersBuffer.setPeerMessage(peerToSend, message.replaceAll("^A-z|^0-9", ""), false, LocalDateTime.now());


        PeerRequest request = new SendMessageRequest.SendMessageRequestBuilder()
                .setHeader("m")
                .setContent(message)
                .setDestinationAddress(a)
                .build();

        mySocket.send(request);
    }


    public static String logout() {

        try (Socket logoutSocket = new Socket()) {
            logoutSocket.connect(new InetSocketAddress(IPServer, portServer));

            Scanner fromServer = new Scanner(logoutSocket.getInputStream());
            Formatter toServer = new Formatter(logoutSocket.getOutputStream());

            Request r = new LogOutRequest.LogOutRequestBuilder()
                    .setPort(mySocket.getLocalPort())
                    .setPassword(password)
                    .setUsername(username)
                    .build();

            toServer.format(r.toString());
            toServer.flush();

            if(fromServer.hasNext()) {
                status = false;
                return ("ok".equals(fromServer.next())) ? "ok" : "wrong credentials";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String username;
    public static String password;

    public static String login(InetAddress serverIP, int serverPort, String username, String password) throws IOException {

        Socket clientSocket = new Socket();
        ChatApplication.username = username;
        ChatApplication.password = password;

//        System.out.println("connecting to server...");
        clientSocket.connect(new InetSocketAddress(serverIP, serverPort));

        Formatter output = new Formatter(clientSocket.getOutputStream());

        Request r = new SignInRequest.SignInRequestBuilder()
                .setUsername(username)
                .setPassword(password)
                .setPort(mySocket.getLocalPort())
                .build();

        output.format(r.toString()); // change port registered at server
        output.flush();

        Scanner input = new Scanner(clientSocket.getInputStream());

        Thread processIncomingMessages = new Thread(processIncomingMessagesCallback, "process incoming messages");
        Thread pingServer = new Thread(pingServerCallback, "ping server");

        String response = "no response";

        if(input.hasNext()) {
            response = input.next();
            status = true;
            IPServer = serverIP;
            portServer = serverPort;
            processIncomingMessages.start();
            pingServer.start(); // this should be called after the user has logged in.
        }

        clientSocket.close();
        input.close();
        output.close();

        return response;
    }

    public static void deleteMessage(int index, Peer peerToSend){

        byte[] sendBuffer;

        peerToSend.deleteMessage(index);

        String message = "d," + index + ",";
        sendBuffer = message.getBytes();

        DatagramPacket deletePacket = new DatagramPacket(sendBuffer, sendBuffer.length, peerToSend.getAddress());
        try {
            mySocket.send(deletePacket);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static CustomDatagramSocket mySocket;
    /*public static Runnable sendMessagesCallback = () -> {
        Scanner in = new Scanner(System.in);
        // create buffer of message size
        byte[] sendBuffer;

        while (status) {
            // read message from scanner

            System.out.println("d or m");
            String selection = in.next();

            System.out.println("Choose which peer (start from 1): ");
            int peerNumber = in.nextInt();

            Peer peerToSend = peersBuffer.getPeer(peerNumber - 1);
            if ("m".equals(selection)) {

            System.out.println("Type your message: ");

            String message = in.nextLine();
            if("".equals(message))
            {
                System.out.println("Type your message: ");
                message = in.nextLine();
            }

            try {

                peersBuffer.setPeerMessage(peerToSend, message.replaceAll("^A-z|^0-9", ""), false, LocalDateTime.now());

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

                // TODO connect this code to UI to display results
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
    };*/

    List<Peer> list = Collections.synchronizedList(new ArrayList<>());
    public static Runnable processIncomingMessagesCallback = () -> {
        try {

            byte[] dataRecv = new byte[MAX_MESSAGE_SIZE];
            System.out.println("listening for messages...");
            while(status) {

                DatagramPacket incomingPacket = new DatagramPacket(dataRecv, MAX_MESSAGE_SIZE);
                mySocket.receive(incomingPacket);
                String messageReceived = new String(incomingPacket.getData());

                String[] header = messageReceived.split(",");
                if ("m".equals(header[0])) {
                    messageReceived = messageReceived.substring(2);
                    String incomingIP = incomingPacket.getAddress().toString().split("/")[1];
                    Peer p = peersBuffer.getPeer(new InetSocketAddress(incomingIP, incomingPacket.getPort()));
                    peersBuffer.setPeerMessage(p, messageReceived.replaceAll("^A-z|^0-9", ""), true, LocalDateTime.now());
//                    peersBuffer.setPeerMessage(incomingIP, incomingPacket.getPort(), messageReceived.replaceAll("^A-z|^0-9", ""), true);

                    System.out.println(incomingPacket.getPort());
                    System.out.println(messageReceived);

                    ((ClientController)fxmlLoader.getController()).updateMessages();
                } else if ("d".equals(header[0])) {
                    int index = Integer.parseInt(header[1]);
                    String incomingIP = incomingPacket.getAddress().toString().split("/")[1];
                    peersBuffer.deletePeerMessage(incomingIP, incomingPacket.getPort(), index);

                    ((ClientController)fxmlLoader.getController()).updateMessages();
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

    public static CountDownLatch waitToLoad = new CountDownLatch(1);
    public static Runnable pingServerCallback = () -> {
        while(status) {
            // send a "retrieve-list" message to server to get online users

            try (Socket pingSocket = new Socket()){

                Thread.sleep(1000);

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
                waitToLoad.await(); // blocks this thread until counter is zero, counter is zero when UI is visible.

                ((ClientController)fxmlLoader.getController()).updatePeersList();

//                peersBuffer.printActivePeers();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    };
}