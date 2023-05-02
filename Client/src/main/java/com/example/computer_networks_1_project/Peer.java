package com.example.computer_networks_1_project;

import java.net.InetAddress;
import java.net.InetSocketAddress;
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

    // messages that were received from this peer when looked at from application point of view.
    private List<String> messagesReceived = new ArrayList<>();

    // messages that are sent to this peer when looked at from application point of view.
    private List<String> messagesSent = new ArrayList<>();

    public void update(String name, String IP, int port) {
        this.name = new String(name);
        this.IP = new String(IP);
        this.port = port;
    }

    public void deleteMessage(int index, boolean direction) {
        if(!direction) {
            this.messagesSent.remove(index);
        } else {
            this.messagesReceived.remove(index);
        }
    }
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

    public InetSocketAddress getAddress() {
        return new InetSocketAddress(this.getIP(), this.getPort());
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

    @Override
    public boolean equals(Object peer){
        if (peer instanceof Peer peer1) {
            return peer1.getName().equals(this.getName());
        }
        return false;
    }

}
