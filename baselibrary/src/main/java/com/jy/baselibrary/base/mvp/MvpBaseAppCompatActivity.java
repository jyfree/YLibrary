package com.jy.baselibrary.base.mvp;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.jy.baselibrary.base.BaseAppCompatActivity;
import com.jy.baselibrary.base.broker.BaseContract;
import com.jy.baselibrary.base.broker.BaseViewModel;


/**
 * Administrator
 * created at 2018/11/7 14:53
 * TODO:AppCompatActivity  mvp模式基类
 */
public abstract class MvpBaseAppCompatActivity<P extends BaseContract.BasePresenter> extends BaseAppCompatActivity {

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
