package com.jy.baselibrary.base.broker

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trello.rxlifecycle2.LifecycleProvider

/**

 * @Author Administrator
 * @Date 2019/11/1-15:38
 * @TODO
 */
open class BaseViewModel : ViewModel() {

    /**
     * 菊花
     */
    val loading = MutableLiveData<Boolean>()

    private var lifecycle: LifecycleProvider<*>? = null

    private var lifecycleOwner: LifecycleOwner? = null


    fun setLifeCycleProvide(lifecycleProvider: LifecycleProvider<*>) {
        this.lifecycle = lifecycleProvider
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }

    /**
     * 绑定生命周期--方法之一，如RxJava的bindToLifecycle
     */
    fun getLifeCycleProvide(): LifecycleProvider<*> = lifecycle!!

    /**
     * 绑定生命周期--方法之二，如RxJava的autoDispose（AutoDispose2的扩展方法）
     */
    fun getLifecycleOwner(): LifecycleOwner = lifecycleOwner!!

}