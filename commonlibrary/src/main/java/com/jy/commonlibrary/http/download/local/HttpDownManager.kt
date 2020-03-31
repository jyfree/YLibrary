package com.jy.commonlibrary.http.download.local

import android.os.Handler
import android.os.Looper
import com.jy.baselibrary.utils.FileUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.db.DownloadDao
import com.jy.commonlibrary.http.bean.DownInfo
import com.jy.commonlibrary.http.download.local.listener.DownloadInterceptor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.*
import java.util.concurrent.TimeUnit

object HttpDownManager {
    private var TAG = HttpDownManager::class.java.simpleName
    /*记录下载数据*/
    private val downInfos: MutableSet<DownInfo>
    /*回调sub队列*/
    private val subMap: HashMap<String, ProgressDownSubscriber<*>>
    /*下载进度回掉主线程*/
    private val handler: Handler

    /*可用内存*/
    private var sdcardAvailableSize = 0L

    init {
        downInfos = HashSet()
        subMap = HashMap()
        handler = Handler(Looper.getMainLooper())
    }

    /**
     * 开始下载
     */
    fun startDownload(baseUrl: String, info: DownInfo) {
        /*正在下载不处理*/
        if (subMap[info.url] != null) {
            subMap[info.url]?.setDownInfo(info)
            return
        }
        /*添加回调处理类*/
        val subscriber = ProgressDownSubscriber<DownInfo>(info, handler)
        /*记录回调sub*/
        subMap[info.url!!] = subscriber
        /*下载完成检测文件是否存在*/
        if (checkHasDownload(info)) {
            YLogUtils.iTag(TAG, "文件已经存在，不需要二次下载")
            return
        }
        /*检测内存大小*/
        if (sdcardAvailableSize == 0L) {
            sdcardAvailableSize = FileUtils.getSDAvailableSize()
        }
        if (sdcardAvailableSize <= 10 * 1024 * 1024) {
            YLogUtils.eTag(TAG, "内存空间不足，无法下载...")
            return
        }

        /*获取service，多次请求公用一个sercie*/
        val httpService: HttpDownService
        if (downInfos.contains(info)) {
            httpService = info.service!!
        } else {
            val interceptor = DownloadInterceptor(subscriber)
            val builder = OkHttpClient.Builder()
            //手动创建一个OkHttpClient并设置超时时间
            builder.connectTimeout(info.connectionTime.toLong(), TimeUnit.SECONDS)
            builder.addInterceptor(interceptor)

            val retrofit = Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
            httpService = retrofit.create(HttpDownService::class.java)
            info.service = httpService
            downInfos.add(info)
        }

        /*得到rx对象-上一次下載的位置開始下載*/
        httpService.download("bytes=" + info.readLength + "-", info.url!!)
            /*指定线程*/
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            /*失败后的retry配置*/
            .retryWhen(RetryWhenNetworkException())
            /*读取下载写入文件*/
            .map {
                val fileName = info.url!!.substring(info.url!!.lastIndexOf("/"))
                writeCaches(it, File(info.savePath + "/$fileName"), info)
                info
            }
            /*回调线程*/
            .observeOn(AndroidSchedulers.mainThread())
            /*数据回调*/
            .subscribe(subscriber)
    }

    private fun checkHasDownload(downInfo: DownInfo): Boolean {
        if (downInfo.getState() == DownState.FINISH || downInfo.getState() == DownState.ERROR) {
            val fileName = downInfo.url!!.substring(downInfo.url!!.lastIndexOf("/"))
            val file = File(downInfo.savePath + fileName)
            val isDownload = file.exists() && file.length() == downInfo.countLength
            if (!isDownload) {
                file.deleteOnExit()
                downInfo.readLength = 0
            } else {
                subMap[downInfo.url!!]?.onComplete()
                remove(downInfo)
                downInfo.setState(DownState.FINISH)
            }
            DownloadDao.insertOrUpdate(downInfo)
            return isDownload
        }
        return false
    }

    /**
     * 写入文件
     */
    private fun writeCaches(responseBody: ResponseBody, file: File, info: DownInfo) {
        try {
            var randomAccessFile: RandomAccessFile? = null
            var channelOut: FileChannel? = null
            var inputStream: InputStream? = null
            try {
                if (file.parentFile?.exists() == false)
                    file.parentFile?.mkdirs()
                val allLength = if (0L == info.countLength)
                    responseBody.contentLength()
                else
                    info.readLength + responseBody
                        .contentLength()

                inputStream = responseBody.byteStream()
                randomAccessFile = RandomAccessFile(file, "rwd")
                channelOut = randomAccessFile.channel
                val mappedBuffer = channelOut!!.map(
                    FileChannel.MapMode.READ_WRITE,
                    info.readLength, allLength - info.readLength
                )
                val buffer = ByteArray(1024)
                var len: Int
                while (inputStream.read(buffer).also { len = it } != -1) {
                    mappedBuffer.put(buffer, 0, len)
                }
            } catch (e: IOException) {
                throw HttpTimeException(e.message)
            } finally {
                inputStream?.close()
                channelOut?.close()
                randomAccessFile?.close()
            }
        } catch (e: IOException) {
            throw HttpTimeException(e.message)
        }

    }

    /**
     * 移除下载数据
     *
     * @param info
     */
    fun remove(info: DownInfo) {
        subMap.remove(info.url)
        downInfos.remove(info)
    }
}