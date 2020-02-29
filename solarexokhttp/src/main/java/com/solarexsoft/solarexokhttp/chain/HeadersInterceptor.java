package com.solarexsoft.solarexokhttp.chain;

import android.util.Log;

import com.solarexsoft.solarexokhttp.Request;
import com.solarexsoft.solarexokhttp.Response;

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
        return null;
    }
}
