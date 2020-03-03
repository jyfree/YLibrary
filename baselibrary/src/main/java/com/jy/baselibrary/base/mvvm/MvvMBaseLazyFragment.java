package com.jy.baselibrary.base.mvvm;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import com.jy.baselibrary.base.contract.BaseContract;
import com.jy.baselibrary.base.mvvm.vm.BaseViewModel;


/**
 * @Author Administrator
 * @Date 2019/11/1-10:36
 * @TODO
 */
public abstract class MvvMBaseLazyFragment<VM extends BaseViewModel, DBinding extends ViewDataBinding> extends MvvMBaseNoViewModelLazyFragment<DBinding> {

    protected VM viewModel;

    @Override
    protected void initUI(View view, @Nullable Bundle savedInstanceState) {

        //初始化viewModel，判断是否为fragment数据共享
        //of(getActivity())作用：Fragment之间返回的是同一个ViewModel对象，进而实现数据共享
        if (isShareData() && getActivity() != null) {
            viewModel = ViewModelProviders.of(getActivity()).get(initViewModelClass());
        } else {
            viewModel = ViewModelProviders.of(this).get(initViewModelClass());
        }
        viewModel.attachMode(initModel());

        dataBinding.setLifecycleOwner(this);
        dataBinding.setVariable(initViewModelId(), viewModel);

        initView(savedInstanceState);
    }

    protected abstract void initView(@Nullable Bundle savedInstanceState);

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
     * 是否为数据共享
     *
     * @return
     */
    protected abstract boolean isShareData();
}
