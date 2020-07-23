package com.jy.baselibrary.thread;

import java.util.concurrent.ExecutorService;

/**
 * @Author Administrator
 * @Date 2019/9/17-17:28
 * @TODO 加载器
 */
public class LoaderEngine {

    private ExecutorService taskExecutor;


    public void init(LoaderConfiguration configuration) {
        taskExecutor = configuration.taskExecutor;

    }

    public ExecutorService getTaskExecutor() {
        return taskExecutor;
    }


    public void submit(Thread task) {
        taskExecutor.execute(task);
    }

    public void submit(Runnable task) {
        taskExecutor.execute(task);
    }


}
