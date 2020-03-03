package com.jy.commonlibrary.http.download.local.listener

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

class DownloadResponseBody(private val responseBody: ResponseBody?, val progressListener: DownloadProgressListener?) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentLength(): Long = responseBody?.contentLength() ?: 0L

    override fun contentType(): MediaType? = responseBody?.contentType()

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = responseBody?.source()?.let { source(it).buffer() }
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L
            var currentTime = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                if (System.currentTimeMillis() - currentTime > 1000) {
                    currentTime = System.currentTimeMillis()
                    responseBody?.contentLength()?.let { progressListener?.update(totalBytesRead, it, bytesRead == -1L) }
                }
                return bytesRead
            }
        }

    }

}