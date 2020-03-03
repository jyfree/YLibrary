package com.jy.baselibrary.helper

import android.Manifest
import android.content.Context
import com.jy.baselibrary.acp.Acp
import com.jy.baselibrary.acp.AcpListener
import com.jy.baselibrary.acp.AcpOptions
import com.jy.baselibrary.pic.Pic
import com.jy.baselibrary.pic.PicListener
import com.jy.baselibrary.pic.PicOptions
import com.jy.baselibrary.utils.YLogUtils


/**
 * @Author Administrator
 * @Date 2019/10/18-15:03
 * @TODO 拍照|相册辅助类
 */
object PicHelper {

    const val TAKE_CAMERA = 1//拍照
    const val TAKE_PICTURE = 2//相册

    fun takePic(context: Context, type: Int, picOptions: PicOptions, listener: PicListener) {

        val array = if (type == TAKE_CAMERA) {
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        } else {
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        Acp.getInstance().acpManager
            .setShowRational(false)
            .setAcPermissionOptions(AcpOptions.beginBuilder().setPermissions(*array).build())
            .setAcPermissionListener(object : AcpListener {
                override fun onDenied(permissions: MutableList<String>?) {
                    YLogUtils.e("权限申请--拒绝", permissions?.toString())
                }

                override fun onGranted() {
                    YLogUtils.i("权限申请--同意")
                    if (type == TAKE_CAMERA) {
                        Pic.getInstance().picManager.setPicOptions(picOptions).setListener(listener)
                            .takeCamera(context)

                    } else if (type == TAKE_PICTURE) {
                        Pic.getInstance().picManager.setPicOptions(picOptions).setListener(listener)
                            .takePhotoAlbum(context)
                    }
                }
            })
            .request(context)

    }

}
