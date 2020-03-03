package com.jy.baselibrary.pic;

import android.net.Uri;

/**
 * @Author Administrator
 * @Date 2019/10/18-9:59
 * @TODO
 */
public interface PicListener {

    void onTakePicSuccess(Uri uri);

    void onTakePicFail();
}
