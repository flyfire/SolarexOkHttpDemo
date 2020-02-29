package com.solarexsoft.solarexokhttp.chain;

import android.util.Log;

import com.solarexsoft.solarexokhttp.Call;
import com.solarexsoft.solarexokhttp.Response;

import java.io.IOException;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 20:39/2020-02-28
 *    Desc:
 * </pre>
 */

public class RetryInterceptor implements Interceptor {
    private static final String TAG = "RetryInterceptor";
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.d(TAG, "intercept");
        Call call = chain.call;
        IOException exception = null;
        for (int i = 0; i < call.client().retrys(); i++) {
            if (call.isCanceled()) {
                throw new IOException("Cancelled");
            }
            try {
                Response response = chain.proceed();
                return response;
            } catch (IOException ex) {
                exception = ex;
            }
        }
        throw exception;
    }
}
