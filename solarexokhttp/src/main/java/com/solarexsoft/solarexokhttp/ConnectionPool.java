package com.solarexsoft.solarexokhttp;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 14:51/2020-02-28
 *    Desc:
 * </pre>
 */

public class ConnectionPool {
    private static final String TAG = "ConnectionPool";

    private long keepAliveDuration;
    private volatile boolean cleanupRunning;
    private Deque<HttpConnection> connections = new ArrayDeque<>();
    private ExecutorService cleanupExecutor = Executors.newFixedThreadPool(1);

    public ConnectionPool() {
        this(1, TimeUnit.MINUTES);
    }

    public ConnectionPool(long keepAliveTime, TimeUnit unit) {
        this.keepAliveDuration = unit.toMillis(keepAliveTime);
    }
    private Runnable cleanupRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                long waitDuration = cleanup(System.currentTimeMillis());
                if (waitDuration == -1) {
                    return;
                }
                if (waitDuration > 0) {
                    synchronized (ConnectionPool.this) {
                        try {
                            ConnectionPool.this.wait(waitDuration);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    private long cleanup(long now) {
        long longestIdleDuration = -1;
        Iterator<HttpConnection> iterator = connections.iterator();
        while (iterator.hasNext()) {
            HttpConnection connection = iterator.next();
            long idleDuration = now - connection.lastUseTime;
            if (idleDuration > keepAliveDuration) {
                iterator.remove();
                connection.close();
                Log.d(TAG, connection + " idle too long,removed from pool");
                continue;
            }
            if (longestIdleDuration < idleDuration) {
                longestIdleDuration = idleDuration;
            }
        }
        if (longestIdleDuration > 0) {
            long lessWaitDuration = keepAliveDuration - longestIdleDuration;
            return lessWaitDuration;
        }
        cleanupRunning = false;
        return longestIdleDuration;
    }

    public synchronized void put(HttpConnection connection) {
        if (!cleanupRunning) {
            cleanupRunning = true;
            cleanupExecutor.submit(cleanupRunnable);
        }
    }

    public synchronized HttpConnection get(String host, int port) {
        Iterator<HttpConnection> iterator = connections.iterator();
        while (iterator.hasNext()) {
            HttpConnection connection = iterator.next();
            if (connection.isSameAddress(host, port)) {
                iterator.remove();
                return connection;
            }
        }
        return null;
    }
}
