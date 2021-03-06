package com.jy.baselibrary.base.vb.mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.viewbinding.ViewBinding;

import com.jy.baselibrary.base.broker.BaseViewModel;
import com.jy.baselibrary.base.vb.BaseVBFragmentActivity;


/**
 * @Author Administrator
 * @Date 2019/11/1-11:06
 * @TODO
 */
public abstract class MvvMBaseVBFragmentActivity<VB extends ViewBinding, VM extends BaseViewModel> extends BaseVBFragmentActivity<VB> {

    protected VM viewModel;

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        attachViewModel();
        initView(savedInstanceState);
    }

    /**
     * 挂载ViewModel
     */
    private void attachViewModel() {
        //初始化viewModel
        viewModel = initViewModel();
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
     * 初始化ViewModel
     *
     * @return
     */
    protected abstract VM initViewModel();

    protected abstract void initView(@Nullable Bundle savedInstanceState);


}
