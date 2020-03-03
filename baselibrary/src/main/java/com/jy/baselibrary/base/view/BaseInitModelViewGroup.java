package com.jy.baselibrary.base.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.jy.baselibrary.base.contract.BaseContract;
import com.trello.rxlifecycle2.LifecycleProvider;


/**
 * @Author Administrator
 * @Date 2019/11/13-11:29
 * @TODO
 */
public abstract class BaseInitModelViewGroup<M extends BaseContract.BaseModel> extends BaseViewGroup {

    public M model;
    public LifecycleProvider lifecycleProvider;

    public BaseInitModelViewGroup(Context context) {
        super(context);
    }

    public BaseInitModelViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseInitModelViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        model = initModel();
    }

    public abstract M initModel();

    public void setLifecycleProvider(LifecycleProvider lifecycleProvider) {
        this.lifecycleProvider = lifecycleProvider;
    }
}
