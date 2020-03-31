package com.jy.simple.http.upload

import com.jy.baselibrary.base.model.BaseModel
import com.jy.commonlibrary.http.MultipartBuilder
import com.jy.commonlibrary.http.RxFileUploadObserver
import com.jy.commonlibrary.http.RxHelper
import com.jy.commonlibrary.http.UploadFileRequestBody
import com.jy.simple.http.network.Api
import com.jy.simple.http.network.api.ApiUploadSimpleService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**

 * @Author Administrator
 * @Date 2019/9/27-14:59
 * @TODO 上传文件
 */
class UploadModel : BaseModel<ApiUploadSimpleService>(ApiUploadSimpleService::class.java) {

    fun uploadHeadImage(file: File, fileUploadObserver: RxFileUploadObserver<String>) {
        val uploadFileRequestBody = UploadFileRequestBody(file, fileUploadObserver)
        Api.uploadInstance.uploadHeadImage(MultipartBuilder.fileToMultipartBody(file, uploadFileRequestBody))
                .subscribeOn(Schedulers.io())
                .compose(RxHelper.handleResult())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver)
    }
}