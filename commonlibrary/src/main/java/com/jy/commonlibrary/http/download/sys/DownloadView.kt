package com.jy.commonlibrary.http.download.sys

import java.io.File

/**
 * @Author Administrator
 * @Date 2019/9/25-21:59
 * @TODO
 */
interface DownloadView {
    fun downloadSuccess(file: File)
    fun downloadFailed(error: String) {}
}