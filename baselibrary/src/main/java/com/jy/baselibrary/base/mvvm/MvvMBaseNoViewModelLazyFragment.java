package com.jy.baselibrary.base.mvvm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.jy.baselibrary.base.BaseLazyFragment;


/**
 * @Author Administrator
 * @Date 2019/11/1-10:36
 * @TODO
 */
public abstract class MvvMBaseNoViewModelLazyFragment<DBinding extends ViewDataBinding> extends BaseLazyFragment {

    protected DBinding dataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, initLayoutID(), container, false);
        mView = dataBinding.getRoot();
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dataBinding != null) {
            dataBinding.unbind();
        }
    }

}
