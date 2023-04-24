package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TestClient {

    public static void main(String[] args) throws IOException {

        Socket client = new Socket("localhost", 8001);
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        out.writeBytes("Hello iam a client");
        out.flush();
        out.close();
        client.close();
    }

}
