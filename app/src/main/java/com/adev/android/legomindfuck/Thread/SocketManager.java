package com.adev.android.legomindfuck.Thread;

import java.net.Socket;

public class SocketManager {

    static Socket ev3Socket;
    static boolean isReady = false;
    static String ip = "192.168.1.6";
    static final Object accessSender = new Object();

    public void openSocket() {
        OpenerThread open = new OpenerThread();
        open.start();
        ReceiverThread rec = new ReceiverThread();
        rec.start();
    }

    public void closeSocket() {
        CloserThread close = new CloserThread();
        close.start();
    }

    public void sendMessage(String message) {
        SenderThread s = new SenderThread(message);
        s.start();
    }

    public void setIp(String mIp)   {
        ip = mIp;
    }

}
