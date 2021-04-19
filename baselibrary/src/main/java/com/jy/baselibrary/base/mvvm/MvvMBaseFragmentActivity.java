package com.jy.baselibrary.base.mvvm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import com.jy.baselibrary.base.broker.BaseContract;
import com.jy.baselibrary.base.broker.BaseViewModel;
import com.jy.baselibrary.helper.LoadSirHelper;
import com.jy.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.components.support.RxFragmentActivity;

import org.jetbrains.annotations.NotNull;


/**
 * @Author Administrator
 * @Date 2019/11/1-11:06
 * @TODO
 */
public abstract class MvvMBaseFragmentActivity<DBinding extends ViewDataBinding> extends RxFragmentActivity implements BaseContract.BaseView {

    protected DBinding dataBinding;
    private LoadSirHelper loadSirHelper = new LoadSirHelper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, initLayoutID());
        dataBinding.setLifecycleOwner(this);
        initUI(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtils.cancelToast();
    }


    /**
     * 挂载ViewModel
     */
    public void attachViewModel(BaseViewModel viewModel) {
        viewModel.setLifeCycleProvide(this);
        viewModel.setLifecycleOwner(this);
        viewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                showPopWindowLoading(show);
            }
        });
    }

    /**
     * 初始化布局ID
     */
    protected abstract int initLayoutID();

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
