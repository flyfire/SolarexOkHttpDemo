package com.solarexsoft.solarexokhttp;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 14:53/2020-02-28
 *    Desc:
 * </pre>
 */

public class HttpConnection {
    Socket socket;

    long lastUseTime;
    private Request request;
    private InputStream inputStream;
    private OutputStream outputStream;

    public boolean isSameAddress(String host, int port) {
        if (socket == null) {
            return false;
        }
        return TextUtils.equals(socket.getInetAddress().getHostName(), host) && port == socket.getPort();
    }

    public InputStream call() {
        // todo
        return null;
    }

    private void createSocket() throws IOException {
        if (socket == null || socket.isClosed()) {
            HttpUrl url = request.url();
            if (url.protocol.equalsIgnoreCase("https")) {
                // todo
            } else {
                socket = new Socket();
            }
            socket.connect(new InetSocketAddress(url.host, url.port));
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        }
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void updateLastUseTime() {
        this.lastUseTime = System.currentTimeMillis();
    }
}
