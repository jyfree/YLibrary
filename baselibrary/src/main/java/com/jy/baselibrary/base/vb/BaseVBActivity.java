package com.jy.baselibrary.base.vb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.jy.baselibrary.base.broker.BaseContract;
import com.jy.baselibrary.helper.LoadSirHelper;
import com.jy.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.jetbrains.annotations.NotNull;

/**
 * @description viewBinding activity基类
 * @date: 2021/4/16 10:19
 * @author: jy
 */
public abstract class BaseVBActivity<VB extends ViewBinding> extends RxAppCompatActivity implements BaseContract.BaseView {

    public VB viewBinding;

    private LoadSirHelper loadSirHelper = new LoadSirHelper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        viewBinding = ViewBindingCreator.INSTANCE.createViewBinding(this, getLayoutInflater());
        setContentView(viewBinding.getRoot());
        initUI(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtils.cancelToast();
    }

    /**
     * 初始化UI
     */
    protected abstract void initUI(@Nullable Bundle savedInstanceState);

    @NonNull
    @Override
    public LifecycleProvider getLifecycleProvider() {
        return this;
    }

    @Override
    public void showPopWindowLoading(boolean flag) {
        loadSirHelper.showPopWindowLoading(this, flag);
    }

    @Override
    public void showLoading(@NotNull Object target, boolean flag) {
        loadSirHelper.showLoading(target, flag);
    }
}
