package com.jy.baselibrary.pic;

import android.net.Uri;

public interface PicScanListener {
    void onScanFinish(String path, Uri uri, boolean isCrop);
}
