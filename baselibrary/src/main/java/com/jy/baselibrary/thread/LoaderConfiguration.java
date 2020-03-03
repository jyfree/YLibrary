package com.jy.baselibrary.thread;

import java.util.concurrent.Executor;

/**
 * @Author Administrator
 * @Date 2019/9/17-17:51
 * @TODO 加载器配置
 */
public final class LoaderConfiguration {
    final Executor taskExecutor;
    final int threadPoolSize;
    final int threadPriority;
    final QueueProcessingType tasksProcessingType;

    private LoaderConfiguration(final Builder builder) {
        taskExecutor = builder.taskExecutor;
        threadPoolSize = builder.threadPoolSize;
        threadPriority = builder.threadPriority;
        tasksProcessingType = builder.tasksProcessingType;
    }

    public static Builder beginBuilder() {
        return new Builder();
    }

    public static class Builder {
        public static final QueueProcessingType DEFAULT_TASK_PROCESSING_TYPE = QueueProcessingType.FIFO;
        public static final int DEFAULT_THREAD_POOL_SIZE = 3;
        public static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY - 2;

        private Executor taskExecutor = null;
        private int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
        private int threadPriority = DEFAULT_THREAD_PRIORITY;
        private QueueProcessingType tasksProcessingType = DEFAULT_TASK_PROCESSING_TYPE;


        public Builder threadPoolSize(int threadPoolSize) {
            this.threadPoolSize = threadPoolSize;
            return this;
        }

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

        public Builder tasksProcessingOrder(QueueProcessingType tasksProcessingType) {
            this.tasksProcessingType = tasksProcessingType;
            return this;
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (taskExecutor == null) {
                taskExecutor = ThreadPoolFactory.createExecutor(threadPoolSize, threadPriority, tasksProcessingType);
            }
        }

        public LoaderConfiguration build() {
            initEmptyFieldsWithDefaultValues();
            return new LoaderConfiguration(this);
        }


    }
}
