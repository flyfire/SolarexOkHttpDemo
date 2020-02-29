package com.solarexsoft.solarexokhttp.chain;

import android.util.Log;

import com.solarexsoft.solarexokhttp.HttpConnection;
import com.solarexsoft.solarexokhttp.Response;
import com.solarexsoft.solarexokhttp.codec.Http1Codec;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 21:31/2020/2/29
 *    Desc:
 * </pre>
 */

public class CallServerInterceptor implements Interceptor {
    private static final String TAG = "CallServerInterceptor";
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.d(TAG, "intercept");
        Http1Codec http1Codec = new Http1Codec();
        HttpConnection connection = chain.connection;

        InputStream inputStream = connection.call(http1Codec);

        String statusLine = http1Codec.readLine(inputStream);

        Map<String, String> headers = http1Codec.readHeaders(inputStream);

        boolean isKeepAlive = false;
        if (headers.containsKey(Http1Codec.HEAD_CONNECTION)) {
            isKeepAlive = headers.get(Http1Codec.HEAD_CONNECTION).equalsIgnoreCase(Http1Codec.HEAD_VALUE_KEEP_ALIVE);
        }

        int contentLength = -1;
        if (headers.containsKey(Http1Codec.HEAD_CONTENT_LENGTH)) {
            contentLength = Integer.valueOf(headers.get(Http1Codec.HEAD_CONTENT_LENGTH));
        }

        boolean chunked = false;
        if (headers.containsKey(Http1Codec.HEAD_TRANSFER_ENCODING)) {
            chunked = headers.get(Http1Codec.HEAD_TRANSFER_ENCODING).equalsIgnoreCase(Http1Codec.HEAD_VALUE_CHUNKED);
        }

        String body = null;
        if (contentLength > 0) {
            byte[] bytes = http1Codec.readBytes(inputStream, contentLength);
            body = new String(bytes);
        } else if (chunked) {
            body = http1Codec.readChunked(inputStream);
        }
        String[] status = statusLine.split(Http1Codec.SPACE);
        connection.updateLastUseTime();
        return new Response(Integer.valueOf(status[1]), contentLength, headers, body, isKeepAlive);
    }
}
