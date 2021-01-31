package com.smarttoni.pos.interceptor;

import android.content.Context;

import com.smarttoni.grenade.Event;
import com.smarttoni.grenade.EventManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    private Context context;
    private ServerSocket serverSocket;
    private String message = "";
    private int socketServerPORT;


    public TCPServer(Context context, int port) {
        this.context = context;
        this.socketServerPORT = port;
    }

    public void serverStart() {
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                // create ServerSocket using specified port
                serverSocket = new ServerSocket(socketServerPORT);

                while (true) {
                    // block the call until connection is created and return
                    // Socket object
                    Socket socket = serverSocket.accept();
                    count++;
                    BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    // Get incoming message
                    String incomingMessage = r.readLine() + System.getProperty("line.separator");
                    r.close();
                    message = "#" + count + " from "
                            + socket.getInetAddress() + ":"
                            + socket.getPort() + "\n" + incomingMessage;
                    new PrinterMessageHandler(context, incomingMessage, socket.getInetAddress().toString()).saveMessage();

                    updateClient();


                    SocketServerReplyThread socketServerReplyThread =
                            new SocketServerReplyThread(socket, count);
                    socketServerReplyThread.run();
                    serverSocket.close();
                    Thread socketServerThread = new Thread(new SocketServerThread());
                    socketServerThread.start();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void updateClient() {
        EventManager.getInstance().emit(Event.ORDER_RECEIVED);
    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket, int c) {
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            String msgReply = "response from server";
            try {
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }
        }
    }
}
