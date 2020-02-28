package com.solarexsoft.solarexokhttp.chain;

import com.solarexsoft.solarexokhttp.Call;
import com.solarexsoft.solarexokhttp.HttpConnection;
import com.solarexsoft.solarexokhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 16:29/2020-02-28
 *    Desc:
 * </pre>
 */

public class InterceptorChain {

    final List<Interceptor> interceptors;
    final int index;
    final Call call;
    final HttpConnection connection;

    public InterceptorChain(List<Interceptor> interceptors, int index, Call call, HttpConnection connection) {
        this.interceptors = interceptors;
        this.index = index;
        this.call = call;
        this.connection = connection;
    }

    public Response proceed() throws IOException {
        return proceed(connection);
    }

    public Response proceed(HttpConnection connection) throws IOException {
        Interceptor interceptor = interceptors.get(index);
        InterceptorChain next = new InterceptorChain(interceptors, index + 1, call, connection);
        return interceptor.intercept(next);
    }
}
