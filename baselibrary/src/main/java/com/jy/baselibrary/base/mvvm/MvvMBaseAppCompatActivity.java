package com.jy.baselibrary.base.mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import com.jy.baselibrary.base.broker.BaseViewModel;


/**
 * @Author Administrator
 * @Date 2019/11/1-11:06
 * @TODO
 */
public abstract class MvvMBaseAppCompatActivity<VM extends BaseViewModel, DBinding extends ViewDataBinding> extends MvvMBaseNoViewModelActivity<DBinding> {

    protected VM viewModel;

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {

        //初始化viewModel
        viewModel = initViewModel();
        viewModel.setLifeCycleProvide(this);
        viewModel.setLifecycleOwner(this);

        //初始化dataBinding
        dataBinding = DataBindingUtil.setContentView(this, initLayoutID());
        dataBinding.setLifecycleOwner(this);

        initObserve();
        initView(savedInstanceState);
    }

    /**
     * 初始化ViewModel
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
