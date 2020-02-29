package com.solarexsoft.solarexokhttp;

import android.text.TextUtils;

import com.solarexsoft.solarexokhttp.codec.Http1Codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

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

    private SSLSocketFactory sslSocketFactory;

    public HttpConnection(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public boolean isSameAddress(String host, int port) {
        if (socket == null) {
            return false;
        }
        return TextUtils.equals(socket.getInetAddress().getHostName(), host) && port == socket.getPort();
    }

    public InputStream call(Http1Codec codec) throws IOException {
        try {
            createSocket();
            codec.writeRequest(outputStream, request);
            return inputStream;
        } catch (IOException e) {
            closeSocketQuietly();
            throw new IOException(e);
        }
    }

    private void closeSocketQuietly() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createSocket() throws IOException {
        if (socket == null || socket.isClosed()) {
            HttpUrl url = request.url();
            if (url.protocol.equalsIgnoreCase("https")) {
                if (sslSocketFactory == null) {
                    socket = SSLSocketFactory.getDefault().createSocket();
                } else {
                    socket = sslSocketFactory.createSocket();
                }
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
