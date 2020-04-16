package com.jy.baselibrary.base.mvvm.vm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jy.baselibrary.base.contract.BaseContract
import com.trello.rxlifecycle2.LifecycleProvider

/**

 * @Author Administrator
 * @Date 2019/11/1-15:38
 * @TODO
 */
open class BaseViewModel<M : BaseContract.BaseModel> : ViewModel() {

    /**
     * 菊花
     */
    val loading = MutableLiveData<Boolean>()

    /**
     * mode
     */
    lateinit var mMode: M

    private var lifecycle: LifecycleProvider<*>? = null

    private var lifecycleOwner: LifecycleOwner? = null

    fun attachMode(mode: M) {
        mMode = mode
    }

    fun setLifeCycleProvide(lifecycleProvider: LifecycleProvider<*>) {
        this.lifecycle = lifecycleProvider
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }

    fun getLifeCycleProvide(): LifecycleProvider<*> = lifecycle!!

    fun getLifecycleOwner(): LifecycleOwner = lifecycleOwner!!

}