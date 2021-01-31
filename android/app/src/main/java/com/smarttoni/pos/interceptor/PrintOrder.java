package com.smarttoni.pos.interceptor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PrintOrder {
    public PrintOrder(String realMessage) {

        try {
            Socket clientSocket = new Socket("192.168.1.189", 9100);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeChars(realMessage);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
