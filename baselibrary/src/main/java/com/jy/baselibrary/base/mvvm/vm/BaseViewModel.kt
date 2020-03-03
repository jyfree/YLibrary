package com.jy.baselibrary.base.mvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jy.baselibrary.base.contract.BaseContract
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**

 * @Author Administrator
 * @Date 2019/11/1-15:38
 * @TODO
 */
open class BaseViewModel<M : BaseContract.BaseModel> : ViewModel() {

    /**
     * 管理RxJava请求
     */
    private var compositeDisposable: CompositeDisposable? = null
    /**
     * 菊花
     */
    val loading = MutableLiveData<Boolean>()

    lateinit var mMode: M

    fun attachMode(mode: M) {
        mMode = mode
    }

    /**
     * 添加 rxJava 发出的请求
     */
    protected fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null || compositeDisposable?.isDisposed == true) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.dispose()
        compositeDisposable = null
    }
}