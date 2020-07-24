package com.jy.commonlibrary.http.download.sys

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Message
import com.jy.baselibrary.sp.SharedPreferencesConfigUtils
import com.jy.baselibrary.utils.FileUtils
import com.jy.baselibrary.utils.YLogUtils
import java.io.File
import java.lang.ref.WeakReference

/**
 * @Author Administrator
 * @Date 2019/9/25-21:59
 * @TODO
 */
class DownloadPresenterIml(private val context: Context, private val downloadManager: DownloadManager, private val downloadView: DownloadView) : DownloadPresenter {
    private val DEFAULT_DIR = FileUtils.getSdcardPath()
    private var DOWNLOAD_DIR = DEFAULT_DIR
    private val HANDLE_DOWNLOAD = 0x001
    private val handler = invoke(context)
    private lateinit var downloadIds: Array<Long?>
    private lateinit var downloadObserver: DownloadChangeObserver
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                val downloadStatus = getDownloadStatus(referenceId)
                when (downloadStatus) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        downloadView.downloadSuccess(File(getFilePath(referenceId)))
                    }
                    DownloadManager.STATUS_FAILED -> downloadView.downloadFailed("download failed,download manager error code is $downloadStatus")
                }
            }
        }
    }

    companion object DownHandler : Handler() {
        private lateinit var weakReference: WeakReference<Context>
        operator fun invoke(context: Context): DownHandler {
            weakReference = WeakReference(context)
            return this
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.arg1 >= 0 && msg.arg2 > 0) {
                YLogUtils.iTag("DownloadManager", "current progress is ${msg.arg1 / msg.arg2.toFloat()}")
            }
        }
    }

    init {
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun registerContentObserver() {
        context.contentResolver.registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, downloadObserver)
    }

    override fun requestDownload(urls: Array<String>, fileDir: String) {
        DOWNLOAD_DIR = fileDir
        requestDownload(urls, false)
    }

    override fun requestDownload(urls: Array<String>) {
        DOWNLOAD_DIR = DEFAULT_DIR
        requestDownload(urls, false)
    }


    override fun requestDownload(urls: Array<String>, isSavePoint: Boolean) {
        downloadIds = arrayOfNulls(urls.size)

        for (index in urls.indices) {
            val url = urls[index]
            val request = DownloadManager.Request(Uri.parse(url)).apply {
                val parent = File(DOWNLOAD_DIR)
                if (!parent.exists()) parent.mkdirs()
                val file = File(DOWNLOAD_DIR, url.substringAfterLast("/"))
                setDestinationUri(Uri.fromFile(file))
                //设置是否显示通知栏
//                setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            }

            if (isSavePoint) {
                val downloadId = SharedPreferencesConfigUtils.getInstance().getLong(url)
                if (downloadId != 0L) {
                    when (getDownloadStatus(downloadId)) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            val file = File(getFilePath(downloadId))
                            if (file.exists())
                                downloadView.downloadSuccess(File(getFilePath(downloadId)))
                            else
                                removeAndReDownloadFile(downloadId, index, request, url)
                        }
                        DownloadManager.STATUS_RUNNING -> YLogUtils.eTag("DownloadManager", "download is running")
                        else -> removeAndReDownloadFile(downloadId, index, request, url)
                    }
                } else {
                    addDownloadEnqueueAndSaveId(index, request, url)
                }
            } else {
                downloadIds[index] = downloadManager.enqueue(request)
            }
        }
    }

    private fun removeAndReDownloadFile(downloadId: Long, index: Int, request: DownloadManager.Request, url: String) {
        downloadManager.remove(downloadId)
        addDownloadEnqueueAndSaveId(index, request, url)
    }

    private fun addDownloadEnqueueAndSaveId(index: Int, request: DownloadManager.Request, url: String) {
        downloadIds[index] = downloadManager.enqueue(request)
        YLogUtils.iTag("DownloadManager", "put download id", "$url :: ${downloadIds[index]}")
        SharedPreferencesConfigUtils.getInstance().setLong(url, downloadIds[index]!!)
    }

    private fun getDownloadStatus(downloadId: Long): Int {
        val bytesAndStatus = getBytesAndStatus(downloadId)
        return bytesAndStatus[2]
    }

    override fun unregisterReceiver() {
        context.unregisterReceiver(broadcastReceiver)
    }

    private inner class DownloadChangeObserver(val downloadIds: Array<Long?>) : ContentObserver(handler) {
        private val progressRunnable = Runnable { updateProgress() }

        override fun onChange(selfChange: Boolean) {
            progressRunnable.run()
        }

        private fun updateProgress() {
            downloadIds.forEach {
                val bytesAndStatus = getBytesAndStatus(it!!)
                handler.sendMessage(handler.obtainMessage(HANDLE_DOWNLOAD, bytesAndStatus[0], bytesAndStatus[1], bytesAndStatus[2]))
            }
        }
    }

    private fun getBytesAndStatus(downloadId: Long): Array<Int> {
        val bytesAndStatus = arrayOf(-1, -1, 0)
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor: Cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            //已经下载文件大小
            bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            //下载文件的总大小
            bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            //下载状态
            bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
        }
        cursor.close()
        return bytesAndStatus
    }

    private fun getFilePath(downloadId: Long): String{
        var path=""
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor: Cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            //下载路径
            path = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
            path = DOWNLOAD_DIR + path.substringAfterLast("/")
        }
        cursor.close()
        return path
    }

}