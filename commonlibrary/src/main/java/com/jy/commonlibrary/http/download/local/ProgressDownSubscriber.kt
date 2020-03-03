package com.jy.commonlibrary.http.download.local

import android.os.Handler
import com.jy.commonlibrary.db.DownloadDao
import com.jy.commonlibrary.http.bean.DownInfo
import com.jy.commonlibrary.http.download.local.listener.DownloadProgressListener
import com.jy.commonlibrary.http.download.local.listener.HttpDownOnNextListener
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.ref.SoftReference


class ProgressDownSubscriber<T>(var mDownInfo: DownInfo, val handler: Handler) : Observer<T>,
    DownloadProgressListener {
    //弱引用结果回调
    private var mSubscriberOnNextListener: SoftReference<HttpDownOnNextListener<T>>

    init {
        this.mSubscriberOnNextListener = SoftReference(mDownInfo.getHttpDownOnNextListener())
    }

    fun setDownInfo(downInfo: DownInfo) {
        this.mSubscriberOnNextListener = SoftReference(downInfo.getHttpDownOnNextListener())
        this.mDownInfo = downInfo
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    override fun onSubscribe(d: Disposable) {
        mSubscriberOnNextListener.get()?.onStart()
        mDownInfo.setState(DownState.START)
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    override fun onComplete() {
        mSubscriberOnNextListener.get()?.onComplete(mDownInfo)
        HttpDownManager.remove(mDownInfo)
        mDownInfo.setState(DownState.FINISH)
        DownloadDao.insertOrUpdate(mDownInfo)
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     */
    override fun onError(e: Throwable) {
        mSubscriberOnNextListener.get()?.onError(e)
        HttpDownManager.remove(mDownInfo)
        mDownInfo.setState(DownState.ERROR)
        DownloadDao.insertOrUpdate(mDownInfo)
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     * @param t 创建Subscriber时的泛型类型
     */
    override fun onNext(t: T) {
        mSubscriberOnNextListener.get()?.onNext(t)
    }

    override fun update(read: Long, count: Long, done: Boolean) {
        var readNew = read
        if (mDownInfo.countLength > count) {
            readNew = mDownInfo.countLength - count + read
        } else {
            mDownInfo.countLength = count
        }
        mDownInfo.readLength = readNew

        if (mSubscriberOnNextListener.get() == null || !mDownInfo.updateProgress) return
        handler.post(Runnable {
            /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
            if (mDownInfo.getState() === DownState.PAUSE || mDownInfo.getState() === DownState.STOP) return@Runnable
            mDownInfo.setState(DownState.DOWN)
            mSubscriberOnNextListener.get()
                ?.updateProgress(mDownInfo.readLength, mDownInfo.countLength)
        })
    }
}