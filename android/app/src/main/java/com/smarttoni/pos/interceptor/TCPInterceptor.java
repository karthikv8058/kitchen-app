package com.smarttoni.pos.interceptor;

import android.content.Context;

import com.smarttoni.database.DbOpenHelper;
import com.smarttoni.entities.Printer;
import com.smarttoni.pos.parser.PosOrderParser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class TCPInterceptor {

    private boolean isIpAvailable = true;

    public void start(@NotNull Context context, @Nullable String printerIp, @Nullable int port) {
        ServerSocket ss = null;
        Socket clientSocket = null;
        DataOutputStream outToServer = null;

        try {

            ss = new ServerSocket(9100);
            if (printerIp != null && port != 0) {
                clientSocket = new Socket(printerIp, port);
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
            }


            boolean isTimerSet = false;
            //accept connections
            Socket s = ss.accept();
            InputStream inputStream = s.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            //receive a message
            String message = "";
            String realMessage = "";
            String line;


            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] byteArray = buffer.toByteArray();
            if (outToServer != null) {
                outToServer.write(byteArray);
            }

            message += new String(byteArray);
            if (clientSocket != null) {
                clientSocket.close();
            }
            List<Printer> printers = DbOpenHelper.Companion.getDaoSession(context).getPrinterDao().loadAll();
            String template = null;
            if (printers.size() > 0) {
                template = printers.get(0).getFormat();
            }

            String orderMessage = message;

            PosOrderParser parser = new PosOrderParser(context);
            parser.parse(template, orderMessage, s.getInetAddress().toString(), realMessage);
            out.flush();
            s.close();

        } catch (IOException e1) {
            isIpAvailable = false;
            e1.printStackTrace();
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                    if (isIpAvailable) {
                        start(context, printerIp, port);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private static final String[] hexes = new String[]{
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F",
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F",
            "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F",
            "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F",
            "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
            "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF",
            "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF",
            "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF",
            "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF",
            "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF",
            "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF"
    };

    public static String byteToHex(byte b) {
        return hexes[b & 0xFF];
    }
}
