package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TestClient {

    public static void main(String[] args) throws IOException {

        for(int i =0 ;i < 10 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        try {
                            Socket client = new Socket("localhost", 8001);
                            DataOutputStream out = new DataOutputStream(client.getOutputStream());
                            out.writeBytes("sign-in,\r");
                            out.flush();

                            Scanner scan = new Scanner(client.getInputStream());
                            String res = scan.next();
                            System.out.println(res);
                            out.close();

                            client.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

    }

}
