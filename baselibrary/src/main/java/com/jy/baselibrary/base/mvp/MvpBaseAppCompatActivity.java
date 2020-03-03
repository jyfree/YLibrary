package com.jy.baselibrary.base.mvp;


import android.os.Bundle;

import androidx.annotation.Nullable;

import com.jy.baselibrary.base.BaseAppCompatActivity;
import com.jy.baselibrary.base.contract.BaseContract;


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
        attachMode();
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
     * 初始化View
     */
    protected abstract void initView(@Nullable Bundle savedInstanceState);

    /**
     * 初始化mode
     *
     * @return
     */
    protected abstract BaseContract.BaseModel initModel();


    /**
     * 挂载mode
     */
    private void attachMode() {
        if (mPresenter != null) {
            mPresenter.attachMode(initModel());
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
