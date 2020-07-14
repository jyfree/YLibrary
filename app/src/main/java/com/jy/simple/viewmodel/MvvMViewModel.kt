package com.jy.simple.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.broker.OkViewModel
import com.jy.simple.repository.BannerRepository


/**

 * @Author Administrator
 * @Date 2019/11/4-15:41
 * @TODO
 */
class MvvMViewModel constructor(private val repository: BannerRepository) : OkViewModel() {

    private val message = MutableLiveData<String>()

    private fun setMessage(msg: String) {
        message.value = msg
    }

    fun getMessage(): LiveData<String> {
        return message
    }

    fun getBanner(showPlace: Int) {
        YLogUtils.i("获取banner--showPlace", showPlace)
        call(request = {
            repository.getBanner(showPlace)
        }, success = {
            YLogUtils.i("获取banner--成功", it)
            setMessage(it.toString())
        }, failed = {
            setMessage("请求失败")
        })


    }

    fun testHttpParam(showPlace: Int) {
        YLogUtils.i("testHttpParam--showPlace", showPlace)
        call(request = {
            repository.testHttpParam(showPlace)
        }, success = {
            YLogUtils.i("testHttpParam--成功", it)
            setMessage(it.toString())
        }, failed = {
            setMessage("请求失败")
        })


    }
}