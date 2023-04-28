package com.example.computer_networks_1_project;

import java.util.ArrayList;
import java.util.List;

public class Peer {
    private String IP;
    private String name;
    private int port;

    public Peer(String name, String IP, int port) {
        this.name = name;
        this.IP = IP;
        this.port = port;
    }

    private List<String> messagesReceived = new ArrayList<>();
    private List<String> messagesSent = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<String> getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(List<String> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public List<String> getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(List<String> messagesSent) {
        this.messagesSent = messagesSent;
    }



}
