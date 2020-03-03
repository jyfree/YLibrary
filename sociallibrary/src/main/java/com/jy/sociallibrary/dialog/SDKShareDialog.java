package com.jy.sociallibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.jy.sociallibrary.R;
import com.jy.sociallibrary.adapter.SDKAdapter2Share;
import com.jy.sociallibrary.bean.SDKShareChannel;
import com.jy.sociallibrary.utils.SDKScreenUtils;

import java.util.List;

/**
 * Administrator
 * created at 2015/12/14 14:54
 * TODO:分享对话框
 */
public class SDKShareDialog extends Dialog {

    private List<SDKShareChannel> dataList;
    private OnSDKShareListener sdkShareListener;


    public SDKShareDialog(Context context, int themeResId, List<SDKShareChannel> dataList, OnSDKShareListener sdkShareListener) {
        super(context, themeResId);
        this.dataList = dataList;
        this.sdkShareListener = sdkShareListener;
        initContentView(context);
        initUI();
    }

    private void initContentView(Context context) {

        setContentView(R.layout.social_sdk_dialog_share);

        if (getWindow() == null) return;
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        Pair<Integer, Integer> res = SDKScreenUtils.getResolution(context);
        lp.width = res.first;
        getWindow().setAttributes(lp);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
    }

    /**
     * 解决虚拟按键遮挡，show之前调用
     */
    public void initSystemUI() {
        if (getWindow() == null) return;
        setNavBarVisibility(getWindow(), false);
    }

    private void setNavBarVisibility(final Window window, boolean isVisible) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        final ViewGroup decorView = (ViewGroup) window.getDecorView();
        for (int i = 0, count = decorView.getChildCount(); i < count; i++) {
            final View child = decorView.getChildAt(i);
            final int id = child.getId();
            if (id != View.NO_ID) {
                String resourceEntryName = getContext()
                        .getResources()
                        .getResourceEntryName(id);
                if ("navigationBarBackground".equals(resourceEntryName)) {
                    child.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
                }
            }
        }
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (isVisible) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~uiOptions);
        } else {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOptions);
        }
    }


    private void initUI() {
        final ImageView iv_close = findViewById(R.id.social_sdk_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        final GridView mGridView = findViewById(R.id.social_sdk_gridView);

        mGridView.setAdapter(new SDKAdapter2Share(getContext(), dataList));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SDKShareDialog.this.dismiss();
                if (sdkShareListener != null) {
                    sdkShareListener.onSelectShare(dataList.get(position));
                }
            }
        });
    }


    public interface OnSDKShareListener {
        void onSelectShare(SDKShareChannel sdkShareChannel);
    }
}
