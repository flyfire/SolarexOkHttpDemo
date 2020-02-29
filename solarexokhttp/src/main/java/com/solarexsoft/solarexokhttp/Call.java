package com.solarexsoft.solarexokhttp;

import com.solarexsoft.solarexokhttp.chain.Interceptor;
import com.solarexsoft.solarexokhttp.chain.InterceptorChain;
import com.solarexsoft.solarexokhttp.chain.RetryInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 15:25/2020-02-28
 *    Desc:
 * </pre>
 */

public class Call {

    Request request;
    SolarexHttpClient client;
    boolean executed;
    private volatile boolean canceled;

    public Call(Request request, SolarexHttpClient client) {
        this.request = request;
        this.client = client;
    }

    public Request request() {
        return request;
    }

    public SolarexHttpClient client() {
        return client;
    }

    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void enqueue(Callback callback) {
        synchronized (this) {
            if (executed) {
                throw new IllegalStateException("already executed!");
            }
            executed = true;
        }
        client.dispatcher().enqueue(new AsyncCall(callback));
    }

    class AsyncCall implements Runnable {
        private final Callback callback;

        public AsyncCall(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            boolean signalCallbacked = false;
            try {
                Response response = getResponse();
                if (isCanceled()) {
                    signalCallbacked = true;
                    callback.onFailure(Call.this, new IOException("Canceled"));
                } else {
                    signalCallbacked = true;
                    callback.onResponse(Call.this, response);
                }
            } catch (Exception ex) {
                if (!signalCallbacked) {
                    callback.onFailure(Call.this, ex);
                }
            } finally {
                client.dispatcher().finished(this);
            }
        }

        public String host() {
            return request.url().getHost().toLowerCase();
        }
    }

    public Response getResponse() throws IOException {
        List<Interceptor> interceptors = new ArrayList<>();
        if (client.interceptors() != null) {
            interceptors.addAll(client.interceptors());
        }
        interceptors.add(new RetryInterceptor());

        InterceptorChain chain = new InterceptorChain(interceptors, 0, this, null);
        return chain.proceed();
    }
}
