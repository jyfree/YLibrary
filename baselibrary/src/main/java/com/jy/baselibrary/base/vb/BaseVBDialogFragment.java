package com.jy.baselibrary.base.vb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.jy.baselibrary.base.broker.BaseContract;
import com.jy.baselibrary.helper.LoadSirHelper;
import com.jy.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;


/**
 * @description viewBinding DialogFragment基类
 * @date: 2021/4/16 10:19
 * @author: jy
 */
public abstract class BaseVBDialogFragment<VB extends ViewBinding> extends RxAppCompatDialogFragment implements BaseContract.BaseView {

    public VB viewBinding;
    private LoadSirHelper loadSirHelper = new LoadSirHelper();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = ViewBindingCreator.INSTANCE.createViewBinding(this, inflater, container);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ToastUtils.cancelToast();
    }


    /**
     * 初始化UI
     */
    protected abstract void initUI(View view, @Nullable Bundle savedInstanceState);


    @NonNull
    @Override
    public LifecycleProvider getLifecycleProvider() {
        return this;
    }

    @Override
    public void showPopWindowLoading(boolean flag) {
        loadSirHelper.showPopWindowLoading(getContext(), getView(), flag);
    }

    @Override
    public void showLoading(@NotNull Object target, boolean flag) {
        loadSirHelper.showLoading(target, flag);
    }
}
