package com.smarttoni.network;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class BroadcastAddress {

    public static final int PORT = 6565;

    public static InetAddress getBroadcastAddress() {
        InetAddress broadcastAddress = null;
        try {
            Enumeration<NetworkInterface> networkInterface = NetworkInterface
                    .getNetworkInterfaces();
            while (broadcastAddress == null
                    && networkInterface.hasMoreElements()) {
                NetworkInterface singleInterface = networkInterface
                        .nextElement();
                for (InterfaceAddress infaceAddress : singleInterface.getInterfaceAddresses()) {
                    broadcastAddress = infaceAddress.getBroadcast();
                    if (broadcastAddress != null) {
                        break;
                    }

                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return broadcastAddress;

    }
}
