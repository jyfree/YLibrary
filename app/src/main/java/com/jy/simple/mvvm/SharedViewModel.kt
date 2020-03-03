package com.jy.simple.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jy.baselibrary.base.mvvm.vm.BaseViewModel
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.http.RxDoError


/**

 * @Author Administrator
 * @Date 2019/11/4-15:41
 * @TODO
 */
class SharedViewModel : BaseViewModel<SharedModel>() {

    private val message = MutableLiveData<String>()

    fun setMessage(msg: String) {
        message.value = msg
    }

    fun getMessage(): LiveData<String> {
        return message
    }

    fun getBanner(showPlace: Int) {
        YLogUtils.i("获取banner--showPlace", showPlace, mMode)
        loading.value = true
        val disposable = mMode.getBanner(1)
                .subscribe({ it ->
                    YLogUtils.i("获取banner--成功", it)
                    loading.value = false
                    setMessage(it.toString())
                }, {
                    RxDoError.onError(it)
                    loading.value = false
                    setMessage("请求失败")
                })
        addDisposable(disposable)

    }
}