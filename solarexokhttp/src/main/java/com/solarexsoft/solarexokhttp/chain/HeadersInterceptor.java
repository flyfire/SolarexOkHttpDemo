package com.solarexsoft.solarexokhttp.chain;

import android.util.Log;

import com.solarexsoft.solarexokhttp.Request;
import com.solarexsoft.solarexokhttp.Response;
import com.solarexsoft.solarexokhttp.codec.Http1Codec;

import java.io.IOException;
import java.util.Map;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 21:06/2020/2/29
 *    Desc:
 * </pre>
 */

public class HeadersInterceptor implements Interceptor {
    private static final String TAG = "HeadersInterceptor";
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.d(TAG, "intercept");
        Request request = chain.call.request();
        Map<String, String> headers = request.headers();
        headers.put(Http1Codec.HEAD_HOST, request.url().getHost());
        headers.put(Http1Codec.HEAD_CONNECTION, Http1Codec.HEAD_VALUE_KEEP_ALIVE);
        if (request.body() != null) {
            String contentType = request.body().contentType();
            if (contentType != null) {
                headers.put(Http1Codec.HEAD_CONTENT_TYPE, contentType);
            }
            long contentLength = request.body().contentLength();
            if (contentLength != -1) {
                headers.put(Http1Codec.HEAD_CONTENT_LENGTH, String.valueOf(contentLength));
            }
        }
        return chain.proceed();
    }
}
