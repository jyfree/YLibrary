package com.jy.baselibrary.base.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.jy.baselibrary.base.BaseAppcompatDialogFragment;
import com.jy.baselibrary.base.broker.BaseContract;
import com.jy.baselibrary.base.broker.BaseViewModel;


/**
 * Administrator
 * created at 2019/9/26 15:43
 * TODO:DialogFragment mvp模式基类
 */
public abstract class MvpBaseAppcompatDialogFragment<P extends BaseContract.BasePresenter> extends BaseAppcompatDialogFragment {

    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        attachView();
        attachViewModel();
    }

    @Override
    public void onDestroy() {
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
