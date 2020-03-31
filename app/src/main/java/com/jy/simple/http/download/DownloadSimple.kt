package com.jy.simple.http.download

import android.content.Context
import android.content.Intent
import com.jy.baselibrary.utils.AppUtils
import com.jy.baselibrary.utils.BaseUtils
import com.jy.baselibrary.utils.FileUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.BaseConstants
import com.jy.commonlibrary.db.DownloadDao
import com.jy.commonlibrary.http.bean.DownInfo
import com.jy.commonlibrary.http.download.local.HttpDownManager
import com.jy.commonlibrary.http.download.local.listener.HttpDownOnNextListener
import com.jy.commonlibrary.http.download.sys.DownloadServer
import com.jy.simple.RequestDomainConfig
import java.io.File

/**

 * @Author Administrator
 * @Date 2019/9/27-15:55
 * @TODO 下载示例 ，注意：需要有访问sdcard权限 和 rc_file_path.xml配置
 */
object DownloadSimple {

    private val baseUrl = RequestDomainConfig.getBaseUrl()

    /**
     * 系统下载方式： DownloadManager
     */
    fun startSysDownload(context: Context?, url: String) {
        val intent = Intent(context, DownloadServer::class.java)
        intent.putExtra(BaseConstants.ActionKey.ACTION_KEY_URL, url)
        context?.startService(intent)
    }

    /**
     * 自定义下载方式
     */
    fun startLocalDownload(url: String) {
        var downInfo = DownloadDao.queryDownloadInfoByPath(url)
        if (downInfo == null) {
            downInfo = DownInfo(url)
            downInfo.updateProgress = true
            downInfo.savePath = FileUtils.getSdcardPath()
            DownloadDao.insertOrUpdate(downInfo)
        }

        val listener = object : HttpDownOnNextListener<DownInfo>() {
            override fun onNext(t: DownInfo) {
                YLogUtils.i("onNext", t)
            }

            override fun onStart() {
                YLogUtils.i("onStart")
            }

            override fun onComplete(downInfo: DownInfo) {
                YLogUtils.i("onComplete", downInfo)

                val fileName = downInfo.url!!.substring(downInfo.url!!.lastIndexOf("/") + 1)
                val file = File(downInfo.savePath + fileName)
                AppUtils.installApp(file, "${BaseUtils.getApp().packageName}.FileProvider")
            }

            override fun updateProgress(readLength: Long, countLength: Long) {
                val progress = ((readLength.toFloat() / countLength.toFloat()) * 100).toInt()
                YLogUtils.i("updateProgress", "$progress%")

            }

        }
        downInfo.listener = listener
        HttpDownManager.startDownload(baseUrl,downInfo)
    }
}