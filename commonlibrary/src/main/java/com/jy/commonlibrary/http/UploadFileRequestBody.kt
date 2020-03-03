package com.jy.commonlibrary.http

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.buffer
import java.io.File


class UploadFileRequestBody<T>(file: File, val fileUploadObserver: RxFileUploadObserver<T>) : RequestBody() {
    private val mRequestBody: RequestBody = create("application/octet-stream".toMediaTypeOrNull(), file)

    override fun contentType(): MediaType? = mRequestBody.contentType()

    override fun contentLength(): Long = mRequestBody.contentLength()

    override fun writeTo(sink: BufferedSink) {
        val countingSink = CountingSink(sink)
        val bufferedSink = countingSink.buffer()
        mRequestBody.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    inner class CountingSink(delegate: BufferedSink) : ForwardingSink(delegate) {
        private var bytesWritten: Long = 0

        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            bytesWritten += byteCount
            fileUploadObserver.onProgressChange(bytesWritten, contentLength())
        }
    }
}