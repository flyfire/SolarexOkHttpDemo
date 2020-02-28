package com.solarexsoft.solarexokhttp;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 14:16/2020-02-28
 *    Desc:
 * </pre>
 */

public class Request {
    private Map<String, String> headers;
    private String method;
    private HttpUrl url;
    private RequestBody requestBody;

    public Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.requestBody = builder.body;
    }

    public String method() {
        return method;
    }

    public HttpUrl url() {
        return url;
    }

    public RequestBody body() {
        return requestBody;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public static final class Builder {
        HttpUrl url;
        Map<String, String> headers = new HashMap<>();
        String method;
        RequestBody body;

        public Builder url(String urlStr) {
            try {
                url = new HttpUrl(urlStr);
                return this;
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("unexpected url", e);
            }
        }

        public Builder addHeader(String key, String value) {
            headers.put(key,value);
            return this;
        }

        public Builder removeHeader(String key) {
            headers.remove(key);
            return this;
        }

        public Builder get() {
            method = "GET";
            return this;
        }

        public Builder post(RequestBody body) {
            this.body = body;
            method = "POST";
            return this;
        }

        public Request build() {
            if (url == null) {
                throw new NullPointerException("url cant be null");
            }

            if (TextUtils.isEmpty(method)) {
                method = "GET";
            }

            return new Request(this);
        }
    }
}
