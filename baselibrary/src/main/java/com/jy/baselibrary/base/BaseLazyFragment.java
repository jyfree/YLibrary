package com.jy.baselibrary.base;

/**
 * @Author Administrator
 * @Date 2019/10/31-11:02
 * @TODO 懒加载
 */
public abstract class BaseLazyFragment extends BaseFragment {

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
