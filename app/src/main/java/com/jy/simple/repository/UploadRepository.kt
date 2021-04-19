package com.jy.simple.repository

import com.jy.commonlibrary.http.MultipartBuilder
import com.jy.commonlibrary.http.RxFileUploadObserver
import com.jy.commonlibrary.http.RxHelper
import com.jy.commonlibrary.http.UploadFileRequestBody
import com.jy.simple.network.Api
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

/**

 * @Author Administrator
 * @Date 2019/9/27-14:59
 * @TODO 上传文件
 */
class UploadRepository{

    fun uploadHeadImage(file: File, fileUploadObserver: RxFileUploadObserver<String>) {
        val uploadFileRequestBody = UploadFileRequestBody(file, fileUploadObserver)
        Api.uploadInstance.uploadHeadImage(MultipartBuilder.fileToMultipartBody(file, uploadFileRequestBody))
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.handleResult())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver)
    }
}