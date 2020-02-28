package com.solarexsoft.solarexokhttp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 15:44/2020-02-28
 *    Desc:
 * </pre>
 */

public class Dispatcher {
    private int maxRequests;
    private int maxRequestsPerHost;

    private Deque<Call.AsyncCall> runningAsyncCalls = new ArrayDeque<>();
    private Deque<Call.AsyncCall> readyAsyncCalls = new ArrayDeque<>();

    private ExecutorService executorService;

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            ThreadFactory threadFactory = new ThreadFactory() {
                AtomicInteger atomicInteger = new AtomicInteger();
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "solarex-http-" + atomicInteger.getAndIncrement());
                }
            };
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory);
        }
        return executorService;
    }

    public Dispatcher() {
        this(64, 5);
    }

    public Dispatcher(int maxRequests, int maxRequestsPerHost) {
        this.maxRequests = maxRequests;
        this.maxRequestsPerHost = maxRequestsPerHost;
    }

    public void enqueue(Call.AsyncCall call) {
        if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        } else {
            readyAsyncCalls.add(call);
        }
    }

    private int runningCallsForHost(Call.AsyncCall call) {
        int result = 0;
        for (Call.AsyncCall runningAsyncCall : runningAsyncCalls) {
            if (runningAsyncCall.host().equals(call.host())) {
                result++;
            }
        }
        return result;
    }

    public void finished(Call.AsyncCall call) {
        synchronized (this) {
            runningAsyncCalls.remove(call);
            checkReady();
        }
    }

    private void checkReady() {
        if (runningAsyncCalls.size() >= maxRequests || readyAsyncCalls.isEmpty()) {
            return;
        }

        Iterator<Call.AsyncCall> iterator = readyAsyncCalls.iterator();
        while (iterator.hasNext()) {
            Call.AsyncCall call = iterator.next();
            if (runningCallsForHost(call) < maxRequestsPerHost) {
                iterator.remove();
                runningAsyncCalls.add(call);
                executorService().execute(call);
            }
            if (runningAsyncCalls.size() >= maxRequests) {
                return;
            }
        }
    }
}
