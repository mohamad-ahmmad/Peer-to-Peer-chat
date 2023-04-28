package com.example.server_.app;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

public class TestClient {

    public static void main(String[] args) throws IOException {


                            try {

                                    Scanner req = new Scanner(System.in);

                                    Socket client = new Socket("localhost", 8001);
                                    DataOutputStream out = new DataOutputStream(client.getOutputStream());
                                    out.writeBytes("log-out,moha,password2,6061" + "\r");
                                    out.flush();

                                    Scanner scan = new Scanner(client.getInputStream());
                                    while(scan.hasNext()) System.out.println(scan.next());

                                    out.close();
                                    client.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }



    }

}
