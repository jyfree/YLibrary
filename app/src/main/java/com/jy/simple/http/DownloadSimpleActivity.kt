package com.jy.simple.http

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.acp.Acp
import com.jy.baselibrary.acp.AcpListener
import com.jy.baselibrary.acp.AcpOptions
import com.jy.baselibrary.base.BaseAppCompatActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.ToastUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.simple.R
import com.jy.simple.http.download.DownloadSimple

/**

 * @Author Administrator
 * @Date 2019/9/27-16:06
 * @TODO 下载示例
 */
class DownloadSimpleActivity : BaseAppCompatActivity() {

    private val url = "https://cdn.9mitao.com/apk/android/9mitao_4.3.8.apk"

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, DownloadSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_download_activity

    override fun initUI(savedInstanceState: Bundle?) {

    }


    fun onDownload(view: View) {
        when (view.id) {
            R.id.sys_download -> checkDownload(true)
            R.id.local_download -> checkDownload(false)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun checkDownload(isSys: Boolean) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canInstallApp = packageManager.canRequestPackageInstalls()
            YLogUtils.i("canInstallApp:", canInstallApp)
        }
        Acp.getInstance().acpManager
            .setAcPermissionOptions(AcpOptions.beginBuilder().setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).build())
            .setAcPermissionListener(object : AcpListener {
                override fun onDenied(permissions: MutableList<String>?) {
                    YLogUtils.e("权限申请--拒绝", permissions?.toString())
                    ToastUtils.showToast(
                        this@DownloadSimpleActivity,
                        "权限申请--拒绝" + permissions?.toString()
                    )
                }

                override fun onGranted() {
                    YLogUtils.i("权限申请--同意")
                    if (isSys) {
                        DownloadSimple.startSysDownload(this@DownloadSimpleActivity, url)
                    } else {
                        DownloadSimple.startLocalDownload(url)
                    }
                }
            })
            .request(this)
    }
}