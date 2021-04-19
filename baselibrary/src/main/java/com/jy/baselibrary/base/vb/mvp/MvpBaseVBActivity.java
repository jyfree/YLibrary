package com.jy.baselibrary.base.vb.mvp;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.viewbinding.ViewBinding;

import com.jy.baselibrary.base.broker.BaseContract;
import com.jy.baselibrary.base.broker.BaseViewModel;
import com.jy.baselibrary.base.vb.BaseVBActivity;


/**
 * @description viewBinding activity mvp模式基类
 * @date: 2021/4/19 10:00
 * @author: jy
 */
public abstract class MvpBaseVBActivity<VB extends ViewBinding, P extends BaseContract.BasePresenter> extends BaseVBActivity<VB> {

    protected P mPresenter;

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        mPresenter = initPresenter();
        attachView();
        attachViewModel();
        initView(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachView();
    }

    /**
     * 初始化Presenter
     *
     * @return
     */
    protected abstract P initPresenter();

    /**
     * 初始化ViewModel
     *
     * @return
     */
    protected abstract BaseViewModel initViewModel();

    /**
     * 初始化View
     */
    protected abstract void initView(@Nullable Bundle savedInstanceState);


    /**
     * 挂载ViewModel
     */
    private void attachViewModel() {
        BaseViewModel viewModel = initViewModel();
        viewModel.setLifeCycleProvide(this);
        viewModel.setLifecycleOwner(this);
        viewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                showPopWindowLoading(show);
            }
        });
        if (mPresenter != null) {
            mPresenter.attachViewModel(viewModel);
        }
    }

    /**
     * 挂载view
     */
    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 卸载view
     */
    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
