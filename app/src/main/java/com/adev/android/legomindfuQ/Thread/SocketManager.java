package com.adev.android.legomindfuQ.Thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class SocketManager {

    private Socket socket = null;
    private Boolean socketReady = false;
    private String ip;

    public SocketManager(String ip){
        setIp(ip);
    }

    public void connect() {
        Thread opener = new Thread(){

            @Override
            public void run() {
                SocketAddress address = null;
                try {
                    address = new InetSocketAddress(InetAddress.getByName(ip), 8888);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                socket = new Socket();
                try {
                    socket.connect(address);
                    socketReady = true;
                    receive();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        opener.start();
    }

    private void receive() {

        Thread receiver = new Thread() {

            @Override
            public void run() {
                while(!socketReady) {}
                while (socketReady) {
                    try {
                        int read;
                        char[] buffer = new char[2048];
                        String message;
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        while ((read = in.read(buffer)) != -1) {
                            message = new String(buffer, 0, read);
                            DeserializerThread deserializerThread = new DeserializerThread(message);
                            deserializerThread.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                connect();
            }
        };

        receiver.start();
    }

    public synchronized void sendMessage(String message) {
        Thread sender = new Thread() {
            @Override
            public void run() {
                    while (!socketReady) {}
                    try {
                        PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                        outToServer.print(message);
                        outToServer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        };
        sender.start();
        notifyAll();
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public boolean isSocketReady() {
        return socketReady;
    }

}