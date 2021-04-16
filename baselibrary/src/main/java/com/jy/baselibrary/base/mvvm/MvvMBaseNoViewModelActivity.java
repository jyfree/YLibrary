package com.jy.baselibrary.base.mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.jy.baselibrary.base.BaseActivity;


/**
 * @Author Administrator
 * @Date 2019/11/1-10:36
 * @TODO
 */
public abstract class MvvMBaseNoViewModelActivity<DBinding extends ViewDataBinding> extends BaseActivity {

    protected DBinding dataBinding;

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.setContentView(this, initLayoutID());
        initView(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataBinding != null) {
            dataBinding.unbind();
        }
    }

    protected abstract void initView(@Nullable Bundle savedInstanceState);
}
