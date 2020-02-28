package com.solarexsoft.solarexokhttp;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 15:28/2020-02-28
 *    Desc:
 * </pre>
 */

public interface Callback {
    void onFailure(Call call, Throwable throwable);

    void onResponse(Call call, Response response);
}
