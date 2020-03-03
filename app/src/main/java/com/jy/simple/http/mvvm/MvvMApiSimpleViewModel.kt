package com.jy.simple.http.mvvm

import androidx.databinding.ObservableField
import com.jy.baselibrary.base.mvvm.vm.BaseViewModel
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.http.RxDoError

/**

 * @Author Administrator
 * @Date 2019/11/1-15:49
 * @TODO
 */
class MvvMApiSimpleViewModel : BaseViewModel<MvvMApiSimpleModel>() {

    val message = ObservableField<String>()

    fun getBanner(showPlace: Int) {
        YLogUtils.i("获取banner--showPlace", showPlace, mMode)
        loading.value = true
        val disposable = mMode.getBanner(1)
                .subscribe({ it ->
                    YLogUtils.i("获取banner--成功", it)
                    loading.value = false
                    message.set(it.toString())
                }, {
                    RxDoError.onError(it)
                    loading.value = false
                    message.set("请求失败")
                })
        addDisposable(disposable)

    }
}