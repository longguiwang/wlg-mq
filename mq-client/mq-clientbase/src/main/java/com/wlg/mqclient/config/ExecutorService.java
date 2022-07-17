package com.wlg.mqclient.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Longgui Wang
 * @Date: 7/16/22
 * @Description: ExecutorService
 */

public class ExecutorService {

    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(20, 100,
                30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "ConsumerExecutor");
            }
        });
    }

    public static ThreadPoolExecutor getExecutor(){
        return threadPoolExecutor;
    }

}
