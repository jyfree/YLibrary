package com.jy.baselibrary.base.mvvm;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import com.jy.baselibrary.base.broker.BaseViewModel;


/**
 * @Author Administrator
 * @Date 2019/11/1-10:36
 * @TODO
 */
public abstract class MvvMBaseLazyFragment<VM extends BaseViewModel, DBinding extends ViewDataBinding> extends MvvMBaseNoViewModelLazyFragment<DBinding> {

    protected VM viewModel;

    @Override
    protected void initUI(View view, @Nullable Bundle savedInstanceState) {

        viewModel = initViewModel();
        viewModel.setLifeCycleProvide(this);
        viewModel.setLifecycleOwner(this);

        dataBinding.setLifecycleOwner(this);

        initObserve();
        initView(savedInstanceState);
    }

    protected abstract void initView(@Nullable Bundle savedInstanceState);

    /**
     * 初始化ViewModel
     * 注意：若需要实现数据共享，of则需要传activity
     * ViewModelProviders.of(getActivity())作用：Fragment之间返回的是同一个ViewModel对象，进而实现数据共享
     *
     * @return
     */
    protected abstract VM initViewModel();


    /**
     * 监听菊花
     */
    private void initObserve() {
        viewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                showPopWindowLoading(show);
            }
        });
    }
}
