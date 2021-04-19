package com.jy.baselibrary.base.mvvm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import com.jy.baselibrary.base.BaseFragment;
import com.jy.baselibrary.base.broker.BaseViewModel;


/**
 * @Author Administrator
 * @Date 2019/11/1-10:36
 * @TODO
 */
public abstract class MvvMBaseFragment<DBinding extends ViewDataBinding> extends BaseFragment {

    protected DBinding dataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, initLayoutID(), container, false);
        dataBinding.setLifecycleOwner(this);
        mView = dataBinding.getRoot();
        return mView;
    }


    /**
     * 挂载ViewModel
     * 注意：若需要实现数据共享，of则需要传activity
     * ViewModelProviders.of(getActivity())作用：Fragment之间返回的是同一个ViewModel对象，进而实现数据共享
     */
    public void attachViewModel(BaseViewModel viewModel) {
        viewModel.setLifeCycleProvide(this);
        viewModel.setLifecycleOwner(this);
        viewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean show) {
                showPopWindowLoading(show);
            }
        });
    }
}
