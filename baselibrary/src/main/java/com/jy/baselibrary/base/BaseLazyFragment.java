package com.jy.baselibrary.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @Author Administrator
 * @Date 2019/10/31-11:02
 * @TODO 懒加载
 */
public abstract class BaseLazyFragment extends BaseFragment {

    /**
     * 懒加载
     */
    private boolean createView, activityCreated, lazy;
    /**
     * 这个Fragment是不是tab页面的第一个页面
     */
    private boolean isFirstTab;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createView = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activityCreated = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && createView && activityCreated && !lazy) {
            //不是第一个Tab的Fragment 进行懒加载请求数据
            lazy = true;
            lazyLoad();
        } else if (isVisibleToUser && !createView && !activityCreated && !lazy) {
            //这个Fragment是多个Tab中的第一个
            isFirstTab = true;
        } else {
            //对用户可见时，是否需要重新刷新数据
            if (isVisibleToUser) {
                visibleToUser();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //第一个Tab页面懒加载
        if (isFirstTab && !lazy) {
            lazy = true;
            lazyLoad();
        }
    }

    /**
     * 懒加载，只有在Fragment第一次创建且第一次对用户可见
     */
    protected abstract void lazyLoad();

    /**
     * 每次在Fragment与用户可见状态且不是第一次对用户可见
     */
    protected abstract void visibleToUser();
}
