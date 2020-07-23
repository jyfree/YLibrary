package com.jy.baselibrary.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author Administrator
 * @Date 2019/9/17-17:51
 * @TODO 加载器配置
 */
public final class LoaderConfiguration {
    final ExecutorService taskExecutor;

    private LoaderConfiguration(final Builder builder) {
        taskExecutor = builder.taskExecutor;
    }

    public static Builder beginBuilder() {
        return new Builder();
    }

    public static class Builder {
        private int threadPriority = Thread.NORM_PRIORITY - 2;//线程优先级
        private int corePoolSize = 3;//核心线程大小
        private int maximumPoolSize = corePoolSize * 3;//最大线程大小，默认为核心线程大小的3倍
        private long keepAliveTime = 60L;//线程默认存活时间60秒
        private BlockingQueue<Runnable> workQueue;//若为null，则默认创建容量为10000的ArrayBlockingQueue
        private ExecutorService taskExecutor = null;


        public Builder threadPriority(int threadPriority) {
            if (threadPriority < Thread.MIN_PRIORITY) {
                this.threadPriority = Thread.MIN_PRIORITY;
            } else {
                if (threadPriority > Thread.MAX_PRIORITY) {
                    this.threadPriority = Thread.MAX_PRIORITY;
                } else {
                    this.threadPriority = threadPriority;
                }
            }
            return this;
        }

        public Builder setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }

        public Builder setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
            return this;
        }

        public Builder setThreadPriority(int threadPriority) {
            this.threadPriority = threadPriority;
            return this;
        }

        public Builder setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
            return this;
        }

        public Builder setWorkQueue(BlockingQueue<Runnable> workQueue) {
            this.workQueue = workQueue;
            return this;
        }

        public Builder setTaskExecutor(ExecutorService taskExecutor) {
            this.taskExecutor = taskExecutor;
            return this;
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (taskExecutor == null) {
                if (workQueue == null) {
                    workQueue = new ArrayBlockingQueue<>(10000);
                }
                taskExecutor = ThreadPoolFactory.createExecutor(corePoolSize, maximumPoolSize, threadPriority, keepAliveTime, TimeUnit.SECONDS, workQueue);
            }
        }

        public LoaderConfiguration build() {
            initEmptyFieldsWithDefaultValues();
            return new LoaderConfiguration(this);
        }


    }
}
