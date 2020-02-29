package com.solarexsoft.solarexokhttp.chain;

import android.util.Log;

import com.solarexsoft.solarexokhttp.HttpConnection;
import com.solarexsoft.solarexokhttp.HttpUrl;
import com.solarexsoft.solarexokhttp.Request;
import com.solarexsoft.solarexokhttp.Response;
import com.solarexsoft.solarexokhttp.SolarexHttpClient;

import java.io.IOException;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 21:21/2020/2/29
 *    Desc:
 * </pre>
 */

public class ConnectionInterceptor implements Interceptor {
    private static final String TAG = "ConnectionInterceptor";
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.d(TAG, "intercept");
        Request request = chain.call.request();
        SolarexHttpClient client = chain.call.client();
        HttpUrl url = request.url();
        String host = url.getHost();
        int port = url.getPort();
        HttpConnection connection = client.connectionPool().get(host, port);
        if (connection == null) {
            connection = new HttpConnection(client.sslSocketFactory());
        } else {
            Log.d(TAG, "connection reuse, " + connection);
        }
        connection.setRequest(request);
        try {
            Response response = chain.proceed(connection);
            if (response.isKeepAlive()) {
                client.connectionPool().put(connection);
            }
            return response;
        } catch (IOException ex) {
            throw ex;
        }
    }
}
