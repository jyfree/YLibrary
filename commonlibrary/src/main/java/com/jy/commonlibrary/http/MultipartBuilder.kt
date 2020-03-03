package com.jy.commonlibrary.http

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object MultipartBuilder {

    fun fileToMultipartBody(file: File, requestBody: RequestBody): MultipartBody {
        val builder = MultipartBody.Builder()
        val jsonObject = JsonObject()
        builder.addFormDataPart("file", file.name, requestBody)
        builder.addFormDataPart("params", jsonObject.toString())
        builder.setType(MultipartBody.FORM)
        return builder.build()
    }

    fun filesToMultipartBody(files: List<File>, fileUploadObserver: RxFileUploadObserver<List<String>>): MultipartBody {
        var totalProgress: Long = 0
        files.forEach {
            totalProgress += it.length()
        }
        fileUploadObserver.setTotalLength(totalProgress)
        val builder = MultipartBody.Builder()
        files.forEach {
            val uploadFileRequestBody = UploadFileRequestBody(it, fileUploadObserver)
            builder.addFormDataPart("files", it.name, uploadFileRequestBody)
        }
        builder.setType(MultipartBody.FORM)
        return builder.build()
    }
}