package com.solarexsoft.solarexokhttp.chain;

import com.solarexsoft.solarexokhttp.Response;

import java.io.IOException;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 16:29/2020-02-28
 *    Desc:
 * </pre>
 */

public interface Interceptor {
    Response intercept(InterceptorChain chain) throws IOException;
}
