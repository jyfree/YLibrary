package com.jy.baselibrary.base;

import android.os.Bundle;

import androidx.annotation.NonNull;

/**
 * @Author Administrator
 * @Date 2019/9/26-15:47
 * @TODO 保存fragment数据基类
 */
public abstract class BaseStateFragment extends BaseFragment {

    Bundle savedState;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 恢复数据
        if (!restoreStateFromArguments()) {
            // 首次加载
            onFirstTimeLaunched();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存数据
        saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //保存数据
        saveStateToArguments();
    }

    private void saveStateToArguments() {
        if (getView() != null) {
            savedState = saveState();
        }
        if (savedState != null) {
            Bundle b = getArguments();
            if (null != b) {
                b.putBundle("savedState", savedState);
            }
        }
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (null != b) {
            savedState = b.getBundle("savedState");
        }
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    private void restoreState() {
        if (savedState != null) {
            onRestoreState(savedState);
        }
    }

    /**
     * 第一次启动时调用
     */
    protected abstract void onFirstTimeLaunched();

    /**
     * 保存状态数据
     * 用例：
     * state.putString(text, tv1.getText().toString());
     *
     * @param outState
     */
    protected abstract void onSaveState(Bundle outState);

    /**
     * 获取状态数据
     * 用例
     * tv1.setText(savedState.getString(text));
     *
     * @param savedInstanceState
     */
    protected abstract void onRestoreState(Bundle savedInstanceState);
}
