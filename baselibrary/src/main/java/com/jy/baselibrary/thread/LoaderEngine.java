package com.jy.baselibrary.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * @Author Administrator
 * @Date 2019/9/17-17:28
 * @TODO 加载器
 */
public class LoaderEngine {

    private LoaderConfiguration configuration;
    private Executor taskExecutor;


    public void init(LoaderConfiguration configuration) {
        this.configuration = configuration;
        taskExecutor = this.configuration.taskExecutor;

    }

    public void submit(Thread task) {
        proof();
        taskExecutor.execute(task);
    }

    public void submit(Runnable task) {
        proof();
        taskExecutor.execute(task);
    }

    private void proof() {
        if (((ExecutorService) taskExecutor).isShutdown()) {
            taskExecutor = ThreadPoolFactory.createExecutor(configuration.threadPoolSize, configuration.threadPriority, configuration.tasksProcessingType);
        }
    }

    /**
     * 停止所有线程
     */
    public void stop() {
        ((ExecutorService) taskExecutor).shutdownNow();
    }

}
