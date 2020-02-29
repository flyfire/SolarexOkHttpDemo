package com.solarexsoft.solarexokhttp.codec;

import com.solarexsoft.solarexokhttp.Request;
import com.solarexsoft.solarexokhttp.RequestBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 21:08/2020/2/29
 *    Desc:
 * </pre>
 */

public class Http1Codec {
    public static final String CRLF = "\r\n";
    public static final int CR = 13;
    public static final int LF = 10;
    public static final String SPACE = " ";
    public static final String VERSION = "HTTP/1.1";
    public static final String COLON = ":";

    public static final String HEAD_HOST = "Host";
    public static final String HEAD_CONNECTION = "Connection";
    public static final String HEAD_CONTENT_TYPE = "Content-Type";
    public static final String HEAD_CONTENT_LENGTH = "Content-Length";
    public static final String HEAD_TRANSFER_ENCODING = "Transfer-Encoding";

    public static final String HEAD_VALUE_KEEP_ALIVE = "Keep-Alive";
    public static final String HEAD_VALUE_CHUNKED = "chunked";

    ByteBuffer byteBuffer;

    public Http1Codec() {
        byteBuffer = ByteBuffer.allocate(10 * 1024);
    }

    public void writeRequest(OutputStream os, Request request) throws IOException {
        StringBuffer protocol = new StringBuffer();
        protocol.append(request.method());
        protocol.append(SPACE);
        protocol.append(request.url().getFile());
        protocol.append(SPACE);
        protocol.append(VERSION);
        protocol.append(CRLF);

        Map<String, String> headers = request.headers();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            protocol.append(entry.getKey());
            protocol.append(COLON);
            protocol.append(SPACE);
            protocol.append(entry.getValue());
            protocol.append(CRLF);
        }
        protocol.append(CRLF);

        RequestBody body = request.body();
        if (body != null) {
            protocol.append(body.body());
        }

        os.write(protocol.toString().getBytes());
        os.flush();
    }

    public Map<String, String> readHeaders(InputStream is) throws IOException {
        Map<String, String> headers = new HashMap<>();
        while (true) {
            String line = readLine(is);

            if (isEmptyLine(line)) {
                break;
            }

            int index = line.indexOf(COLON);
            if (index > 0) {
                String key = line.substring(0, index);
                String value = line.substring(index + 2, line.length() - 2);
                headers.put(key, value);
            }
        }
        return headers;
    }

    public String readLine(InputStream is) throws IOException {
        try {
            byte b;
            boolean isMaybeEOF = false;

            byteBuffer.clear();
            byteBuffer.mark();

            while ((b = (byte)is.read()) != -1) {
                byteBuffer.put(b);
                if (b == CR) {
                    isMaybeEOF = true;
                } else if (isMaybeEOF) {
                    if (b == LF) {
                        byte[] lineBytes = new byte[byteBuffer.position()];
                        byteBuffer.reset();
                        byteBuffer.get(lineBytes);
                        byteBuffer.clear();
                        byteBuffer.mark();
                        String line = new String(lineBytes);
                        return line;
                    } else {
                        isMaybeEOF = false;
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
        throw new IOException("Error reading response line.");
    }

    public boolean isEmptyLine(String line) {
        return line.equals(CRLF);
    }

    public byte[] readBytes(InputStream is, int len) throws IOException {
        byte[] bytes = new byte[len];
        int readNum = 0;
        while (true) {
            readNum += is.read(bytes, readNum, len - readNum);
            if (readNum == len) {
                return bytes;
            }
        }
    }

    public String readChunked(InputStream is) throws IOException {
        int len = -1;
        boolean isEmptyData = false;
        StringBuffer chunked = new StringBuffer();
        while (true) {
            if (len < 0) {
                String line = readLine(is);
                line = line.substring(0, line.length() - 2);
                len = Integer.valueOf(line);
                isEmptyData = len == 0;
            } else {
                byte[] bytes = readBytes(is, len + 2);
                chunked.append(new String(bytes));
                len = -1;
                if (isEmptyData) {
                    return chunked.toString();
                }
            }
        }
    }
}
