package com.jy.baselibrary.base.vb;

import androidx.viewbinding.ViewBinding;

/**
 * @description ViewBinding 懒加载
 * @date: 2021/4/16 11:52
 * @author: jy
 */
public abstract class BaseVBLazyFragment<VB extends ViewBinding> extends BaseVBFragment<VB> {

    private boolean isFirstLoad = true;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            lazyLoad();
            isFirstLoad = false;
        }
        visibleToUser();
    }

    /**
     * 只有在Fragment第一次创建且第一次对用户可见
     */
    protected abstract void lazyLoad();

    /**
     * 每次在Fragment与用户可见
     */
    protected abstract void visibleToUser();
}
