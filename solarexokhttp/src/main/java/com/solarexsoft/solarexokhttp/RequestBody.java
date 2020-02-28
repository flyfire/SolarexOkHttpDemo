package com.solarexsoft.solarexokhttp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 13:36/2020-02-28
 *    Desc:
 * </pre>
 */

public class RequestBody {
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String CHARSET = "utf-8";

    Map<String, String> encodedBodys = new HashMap<>();

    public String contentType() {
        return CONTENT_TYPE;
    }

    public long contentLength() {
        return body().getBytes().length;
    }

    public String body() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : encodedBodys.entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        if (sb.length() != 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public RequestBody add(String key, String value) {
        try {
            encodedBodys.put(URLEncoder.encode(key, CHARSET), URLEncoder.encode(value, CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }
}
