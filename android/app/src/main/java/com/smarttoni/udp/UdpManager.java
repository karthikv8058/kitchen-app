package com.smarttoni.udp;

import android.content.Context;
import android.os.PowerManager;

import com.smarttoni.entities.Restaurant;
import com.smarttoni.grenade.Event;
import com.smarttoni.grenade.EventCallback;
import com.smarttoni.grenade.EventManager;
import com.smarttoni.network.BroadcastAddress;
import com.smarttoni.utils.LocalStorage;
import com.smarttoni.utils.WifiUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class UdpManager {

    private boolean receiveUDP;
    private Thread thread;

    public void initUDP(Context context) {

        EventManager.getInstance().addEventListner(Event.SERVER_START, new EventCallback() {
            @Override
            public void onEvent(Object data) {
                receiveUDP = true;
                receiveUDPBroadcast(context);
            }
        });

        EventManager.getInstance().addEventListner(Event.SERVER_STOP, new EventCallback() {
            @Override
            public void onEvent(Object data) {
                if (thread != null) {
                    receiveUDP = false;
                }
            }
        });

    }

    private void receiveUDPBroadcast(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock;
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SKKI:WakeLock");
            wakeLock.acquire();
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setName(new LocalStorage(context).getRestaurantName());
        restaurant.setIp(WifiUtils.getLocalIpAddress());
        thread = new Thread(() -> {
            byte[] msg = new byte[1000];
            DatagramPacket dp = new DatagramPacket(msg, msg.length);
            DatagramSocket ds = null;
            try {
                ds = new DatagramSocket(BroadcastAddress.PORT);
                ds.receive(dp);
                if (receiveUDP) {
                    String ip = dp.getAddress().getHostAddress();
                    new com.smarttoni.utils.UdpManager().sendUdpResponse(ip, restaurant);
                    receiveUDPBroadcast(context);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (ds != null) {
                    ds.close();
                }
            }
        });
        thread.start();
    }

    public void sendSSR(int version) {

        String request = "ST(" + version + ")";

        DatagramSocket ds = null;

        final InetAddress broadcastAddress = getBroadcastAddress();

        if (broadcastAddress == null) {
            return;
        }

        final DatagramPacket dp = new DatagramPacket(request.getBytes(), request.length(), broadcastAddress, 6565);

        try {
            ds = new DatagramSocket();
        } catch (SocketException e) {
            return;
        }

        final DatagramSocket finalDs = ds;
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    finalDs.send(dp);
                    finalDs.send(dp);
                    finalDs.send(dp);

                } catch (IOException e) {

                }
                long delay = 1000;
                long period = 3000L;
            }
        });
        thread.start();
    }

    public static InetAddress getBroadcastAddress() {
        InetAddress broadcastAddress = null;
        try {
            Enumeration<NetworkInterface> networkInterface = NetworkInterface
                    .getNetworkInterfaces();
            while (broadcastAddress == null
                    && networkInterface.hasMoreElements()) {
                NetworkInterface singleInterface = networkInterface
                        .nextElement();
                for (InterfaceAddress interfaceAddress : singleInterface.getInterfaceAddresses()) {
                    broadcastAddress = interfaceAddress.getBroadcast();
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
