package com.jy.baselibrary.loadsir.core;


import androidx.annotation.NonNull;

import com.jy.baselibrary.loadsir.LoadSirUtil;
import com.jy.baselibrary.loadsir.callback.Callback;

import java.util.ArrayList;
import java.util.List;


/**
 * Administrator
 * created at 2019/9/26 10:38
 * TODO:
 * <p>
 * 使用方式：
 * application初始化
 * <p>
 * LoadSir.beginBuilder()
 * .addCallbackList(callbackList)
 * .setDefaultCallback(SuccessCallback.class)
 * .setLoadingCallback(loadingCallback)
 * .setResLayoutIdOfPopWindow(resLayoutId)
 * .commit();
 */
public class LoadSir {
    private static volatile LoadSir loadSir;
    public static final int EMPTY_LAYOUT = -1;
    private Builder builder;

    public static LoadSir getDefault() {
        if (loadSir == null) {
            synchronized (LoadSir.class) {
                if (loadSir == null) {
                    loadSir = new LoadSir();
                }
            }
        }
        return loadSir;
    }

    private LoadSir() {
        this.builder = new Builder();
    }

    private void setBuilder(@NonNull Builder builder) {
        this.builder = builder;
    }

    private LoadSir(Builder builder) {
        this.builder = builder;
    }

    public LoadService register(@NonNull Object target) {
        return register(target, null, null);
    }

    public LoadService register(Object target, Callback.OnReloadListener onReloadListener) {
        return register(target, onReloadListener, null);
    }

    public <T> LoadService register(Object target, Callback.OnReloadListener onReloadListener, Convertor<T>
            convertor) {
        TargetContext targetContext = LoadSirUtil.getTargetContext(target);
        return new LoadService<>(convertor, targetContext, onReloadListener, builder);
    }

    public static Builder beginBuilder() {
        return new Builder();
    }

    public Class<? extends Callback> getLoadingCallbackClass() {
        return builder.getLoadingCallback();
    }

    public int getResLayoutIdOfPopWindow() {
        return builder.getResLayoutIdOfPopWindow();
    }

    public static class Builder {
        private List<Callback> callbacks = new ArrayList<>();
        private Class<? extends Callback> defaultCallback;
        private Class<? extends Callback> loadingCallback;
        private int resLayoutIdOfPopWindow = EMPTY_LAYOUT;

        public Builder addCallback(@NonNull Callback callback) {
            callbacks.add(callback);
            return this;
        }

        public Builder addCallbackList(@NonNull List<Callback> list) {
            callbacks.addAll(list);
            return this;
        }

        public Builder setDefaultCallback(@NonNull Class<? extends Callback> defaultCallback) {
            this.defaultCallback = defaultCallback;
            return this;
        }

        public Builder setLoadingCallback(@NonNull Class<? extends Callback> loadingCallback) {
            this.loadingCallback = loadingCallback;
            return this;
        }

        public Builder setResLayoutIdOfPopWindow(@NonNull int resLayoutId) {
            this.resLayoutIdOfPopWindow = resLayoutId;
            return this;
        }

        public int getResLayoutIdOfPopWindow() {
            return resLayoutIdOfPopWindow;
        }

        public List<Callback> getCallbacks() {
            return callbacks;
        }

        Class<? extends Callback> getDefaultCallback() {
            return defaultCallback;
        }

        Class<? extends Callback> getLoadingCallback() {
            return loadingCallback;
        }

        public void commit() {
            getDefault().setBuilder(this);
        }

        public LoadSir build() {
            return new LoadSir(this);
        }

    }
}
