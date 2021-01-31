package com.smarttoni.react.modules;


public class UdpBroadcast {
//
//    public static final int PORT = 6565;
//    private DatagramSocket ds = null;
//    private DatagramSocket dss = null;
//    private static UdpBroadcast instance = null;
//    private boolean shouldBroadcast = true;
//    private boolean shouldRequest = true;
//    private Timer timer, requestTimer;
//
//    private UdpBroadcast() {
//        try {
//            ds = new DatagramSocket();
//            dss = new DatagramSocket(5555);
//            dss.setReuseAddress(true);
//            ds.setReuseAddress(true);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static UdpBroadcast getInstance() {
//        if (instance == null) {
//            instance = new UdpBroadcast();
//        }
//        return instance;
//
//    }
//
//    public void stopUdpBroadcast(){
//        shouldBroadcast = false;
//    }

//    public static InetAddress getBroadcastAddress() {
//        InetAddress broadcastAddress = null;
//        try {
//            Enumeration<NetworkInterface> networkInterface = NetworkInterface
//                    .getNetworkInterfaces();
//            while (broadcastAddress == null
//                    && networkInterface.hasMoreElements()) {
//                NetworkInterface singleInterface = networkInterface
//                        .nextElement();
//                for (InterfaceAddress interfaceAddress : singleInterface.getInterfaceAddresses()) {
//                    broadcastAddress = interfaceAddress.getBroadcast();
//                    if (broadcastAddress != null) {
//                        break;
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        return broadcastAddress;
//
//    }
//
//
//    public void stopIpRequest(){
//        shouldRequest = false;
//    }

//    public void requestIpContinously(){
//        final Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                String ip = "";
//
//                DatagramPacket dp;
//                final InetAddress serverAddr = getBroadcastAddress();
//
//
//                if (serverAddr != null) {
//                    String message = "need-ip-for"+serverAddr.toString();
//                    dp = new DatagramPacket(message.getBytes(), message.length(), serverAddr, 6565);
//                    TimerTask repeatedTask = new TimerTask() {
//                        public void run() {
//                            if(!shouldRequest){
//                                requestTimer.cancel();
//                            }
//                            try {
//                                ds.send(dp);
//                            } catch (IOException e) {
//
//                                if(requestTimer != null){
//                                    requestTimer.cancel();
//                                }
//
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    requestTimer = new Timer("Timer");
//
//                    long delay = 1000;
//                    long period = 3000L;
//                    requestTimer.scheduleAtFixedRate(repeatedTask, delay, period);
//                }
//            }
//        });
//        thread.start();
//    }

//    public void broadcastIpContinously(UdpResponse udpResponse){
//        final Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                DatagramPacket dp;
//                final InetAddress serverAddr = getBroadcastAddress();
//                if (serverAddr != null) {
//                    udpResponse.setIp(serverAddr.toString());
//                    String message = new Gson().toJson(udpResponse);
//                    dp = new DatagramPacket(message.getBytes(), message.length(), serverAddr, 6565);
//                    TimerTask repeatedTask = new TimerTask() {
//                        public void run() {
//                            System.out.println("Task performed on " + new Date());
//                                if(!shouldBroadcast){
//                                    timer.cancel();
//                                }
//                            try {
//                                ds.send(dp);
//                            } catch (IOException e) {
//                                if(timer != null) {
//                                    timer.cancel();
//                                }
//
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    timer = new Timer("Timer");
//
//                    long delay = 1000;
//                    long period = 3000L;
//                    timer.scheduleAtFixedRate(repeatedTask, delay, period);
//                }
//            }
//        });
//        thread.start();
//
//    }

//    public void sendMessage(final Promise promise) {
//        final Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String message = "{}";
//                String ip = "";
//
//                    DatagramPacket dp;
//                    final InetAddress serverAddr = getBroadcastAddress();
//                    if (serverAddr != null) {
//                        dp = new DatagramPacket(message.getBytes(), message.length(), serverAddr, 6565);
//                            try {
//                                ds.send(dp);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
////                        byte[] lMsg = new byte[1000];
////                        dp = new DatagramPacket(lMsg, lMsg.length);
////                        dss.receive(dp);
////                        ip = new String(lMsg, 0, dp.getLength());
//                    }
//
//                promise.resolve(ip);
//            }
//        });
//        thread.start();
//    }
}
