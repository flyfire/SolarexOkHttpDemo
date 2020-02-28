package com.solarexsoft.solarexokhttp;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 11:28/2020-02-28
 *    Desc:
 * </pre>
 */

public class HttpUrl {
    String protocol;
    String host;
    String file;
    int port;


    public HttpUrl(String urlStr) throws MalformedURLException {
        URL url = new URL(urlStr);
        host = url.getHost();
        file = url.getFile();
        file = TextUtils.isEmpty(file) ? "/" : file;
        protocol = url.getProtocol();
        port = url.getPort();
        port = port == -1 ? url.getDefaultPort() : port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getFile() {
        return file;
    }

    public int getPort() {
        return port;
    }
}
