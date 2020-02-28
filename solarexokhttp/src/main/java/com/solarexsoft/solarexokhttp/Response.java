package com.solarexsoft.solarexokhttp;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 14:45/2020-02-28
 *    Desc:
 * </pre>
 */

public class Response {
    int code;
    long contentLength = -1;
    Map<String, String> headers = new HashMap<>();
    String body;
    boolean isKeepAlive;

    public Response() {
    }

    public Response(int code, int contentLength, Map<String, String> headers, String body, boolean isKeepAlive) {
        this.code = code;
        this.contentLength = contentLength;
        this.headers = headers;
        this.body = body;
        this.isKeepAlive = isKeepAlive;
    }

    public int code() {
        return code;
    }

    public long contentLength() {
        return contentLength;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public String body() {
        return body;
    }

    public boolean isKeepAlive() {
        return isKeepAlive;
    }
}
