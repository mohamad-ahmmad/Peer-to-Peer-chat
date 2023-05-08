package com.example.computer_networks_1_project;

import com.example.computer_networks_1_project.requests.PeerRequest;

import java.io.IOException;
import java.net.*;

public class CustomDatagramSocket extends DatagramSocket {
    public CustomDatagramSocket() throws SocketException {
    }

    protected CustomDatagramSocket(DatagramSocketImpl impl) {
        super(impl);
    }

    public CustomDatagramSocket(SocketAddress bindaddr) throws SocketException {
        super(bindaddr);
    }

    public CustomDatagramSocket(int port) throws SocketException {
        super(port);
    }

    public CustomDatagramSocket(int port, InetAddress laddr) throws SocketException {
        super(port, laddr);
    }

    public void send(PeerRequest request) throws IOException {
        byte[] sendBuffer;
        sendBuffer = request.toString().getBytes();

        DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, request.getDestinationAddress());
        send(packet);
    }
}
