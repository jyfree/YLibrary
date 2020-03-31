package com.jy.commonlibrary.http.download.sys

import android.app.DownloadManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.jy.baselibrary.utils.AppUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.BaseConstants
import java.io.File


/**
 * @Author Administrator
 * @Date 2019/9/25-21:59
 * @TODO 资源下载服务
 */
class DownloadServer : Service(), DownloadView {
    lateinit var downloadPresenter: DownloadPresenter

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(BaseConstants.ActionKey.ACTION_KEY_URL)
        val path = intent?.getStringExtra(BaseConstants.ActionKey.ACTION_KEY_PATH)
        url?.let { startDownload(it, path) }
        return START_REDELIVER_INTENT
    }

    private fun startDownload(url: String, path: String?) {
        downloadPresenter = DownloadPresenterIml(this, getSystemService(DOWNLOAD_SERVICE) as DownloadManager, this)
        if (path.isNullOrEmpty()) {
            downloadPresenter.requestDownload(arrayOf(url))
        } else {
            downloadPresenter.requestDownload(arrayOf(url), path)
        }
    }

    override fun downloadSuccess(file: File) {
        YLogUtils.iTag("DownloadManager", "downloadSuccess", file)
        if (file.exists())
            AppUtils.installApp(file, "$packageName.FileProvider")
    }

    override fun downloadFailed(error: String) {
        super.downloadFailed(error)
        YLogUtils.eTag("DownloadManager", "downloadFailed", error)

    }
}