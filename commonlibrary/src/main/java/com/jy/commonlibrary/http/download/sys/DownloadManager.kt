package com.jy.commonlibrary.http.download.sys

import android.content.Context
import android.content.Intent

object DownloadManager {

    fun startSysDownload(context: Context?, url: String, path: String?) {
        val intent = Intent(context, DownloadServer::class.java)
        intent.putExtra(DownloadConstants.ActionKey.ACTION_KEY_URL, url)
        if (path != null) {
            intent.putExtra(DownloadConstants.ActionKey.ACTION_KEY_PATH, path)
        }
        context?.startService(intent)
    }
}