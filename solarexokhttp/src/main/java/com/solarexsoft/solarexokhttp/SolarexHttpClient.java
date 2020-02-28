package com.solarexsoft.solarexokhttp;


import javax.net.ssl.SSLSocketFactory;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 15:24/2020-02-28
 *    Desc:
 * </pre>
 */

public class SolarexHttpClient {

    private final Dispatcher dispatcher;
    private final int retrys;
    private final ConnectionPool connectionPool;
    private final SSLSocketFactory sslSocketFactory;

    public SolarexHttpClient(Builder builder) {
        this.dispatcher = builder.dispatcher;
        this.retrys = builder.retrys;
        this.connectionPool = builder.connectionPool;
        this.sslSocketFactory = builder.sslSocketFactory;
    }

    public Dispatcher dispatcher() {
        return dispatcher;
    }

    public int retrys() {
        return retrys;
    }

    public ConnectionPool connectionPool() {
        return connectionPool;
    }

    public Call newCall(Request request) {
        return new Call(request, this);
    }

    public static final class Builder {
        Dispatcher dispatcher;
        int retrys = 3;
        ConnectionPool connectionPool;
        SSLSocketFactory sslSocketFactory;

        public Builder dispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public Builder retrys(int retrys) {
            this.retrys = retrys;
            return this;
        }

        public Builder connectionPool(ConnectionPool connectionPool) {
            this.connectionPool = connectionPool;
            return this;
        }

        public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        public SolarexHttpClient build() {
            if (dispatcher == null) {
                dispatcher = new Dispatcher();
            }
            if (retrys < 0) {
                retrys = 3;
            }
            if (connectionPool == null) {
                connectionPool = new ConnectionPool();
            }

            return new SolarexHttpClient(this);
        }
    }
}
