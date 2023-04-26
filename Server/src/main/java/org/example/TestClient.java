package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class TestClient {

    public static void main(String[] args) throws IOException {


                            try {

                                    Scanner req = new Scanner(System.in);

                                    Socket client = new Socket("localhost", 8001);
                                    DataOutputStream out = new DataOutputStream(client.getOutputStream());
                                    out.writeBytes("retrieve-list" + "\r");
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
