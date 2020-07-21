package com.jy.baselibrary.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * @Author Administrator
 * @Date 2019/9/17-17:28
 * @TODO 加载器
 */
public class LoaderEngine {

    private Executor taskExecutor;


    public void init(LoaderConfiguration configuration) {
        taskExecutor = configuration.taskExecutor;

    }

    public void submit(Thread task) {
        taskExecutor.execute(task);
    }

    public void submit(Runnable task) {
        taskExecutor.execute(task);
    }

    /**
     * 立即停止所有线程
     */
    public void shutdownNow() {
        ((ExecutorService) taskExecutor).shutdownNow();
    }

    /**
     * 停止所有线程
     */
    public void shutdown() {
        ((ExecutorService) taskExecutor).shutdown();
    }

}
