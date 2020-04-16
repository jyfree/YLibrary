package com.jy.baselibrary.base.mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jy.baselibrary.base.contract.BaseContract;
import com.jy.baselibrary.base.mvvm.vm.BaseViewModel;


/**
 * @Author Administrator
 * @Date 2019/11/1-11:06
 * @TODO
 */
public abstract class MvvMBaseActivity<VM extends BaseViewModel, DBinding extends ViewDataBinding> extends MvvMBaseNoViewModelActivity<DBinding> {

    protected VM viewModel;

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {

        //初始化viewModel
        viewModel = ViewModelProviders.of(this).get(initViewModelClass());
        viewModel.attachMode(initModel());
        viewModel.setLifeCycleProvide(this);
        viewModel.setLifecycleOwner(this);
        initObserve();

        //初始化dataBinding
        dataBinding = DataBindingUtil.setContentView(this, initLayoutID());
        dataBinding.setLifecycleOwner(this);
        dataBinding.setVariable(initViewModelId(), viewModel);

        initView(savedInstanceState);
    }

    /**
     * 初始化ViewModel
     *
     * @return
     */
    protected abstract Class<VM> initViewModelClass();

    /**
     * 初始化ViewModel的variableId
     *
     * @return
     */
    protected abstract int initViewModelId();

    /**
     * 初始化mode
     *
     * @return
     */
    protected abstract BaseContract.BaseModel initModel();

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
