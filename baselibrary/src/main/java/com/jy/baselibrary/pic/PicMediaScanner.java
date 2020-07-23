package com.jy.baselibrary.pic;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

public class PicMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mMs;
    private String path;
    private String mimeType;
    private boolean isCrop;
    private PicScanListener listener;

    public PicMediaScanner(Context context, String path, String mimeType, boolean isCrop, PicScanListener listener) {
        this.path = path;
        this.mimeType = mimeType;
        this.isCrop = isCrop;
        this.listener = listener;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(path, mimeType);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mMs.disconnect();
        if (listener != null) {
            listener.onScanFinish(path, uri, isCrop);
        }
        listener = null;
    }
}
