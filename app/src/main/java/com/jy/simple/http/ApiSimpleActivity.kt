package com.jy.simple.http

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.jy.baselibrary.base.BaseActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.commonlibrary.glide.setImageDefaultLoadIconUrl
import com.jy.commonlibrary.http.RxHelper
import com.jy.commonlibrary.http.RxObserver
import com.jy.simple.R
import com.jy.simple.bean.SendGiftVo
import com.jy.simple.repository.BannerRepository
import com.jy.simple.repository.UserRepository
import com.jy.simple.viewmodel.MvpViewModel
import com.jy.simple.viewmodel.MvpViewModelFactory
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.simple_api_test_activity.*


/**
 * @Author Administrator
 * @Date 2019/9/26-11:38
 * @TODO mvc 请求
 */
class ApiSimpleActivity : BaseActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, ApiSimpleActivity::class.java)
        }
    }

    private lateinit var viewModel: MvpViewModel

    override fun initLayoutID(): Int = R.layout.simple_api_test_activity

    override fun initUI(savedInstanceState: Bundle?) {

        imageView.setImageDefaultLoadIconUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571114721744&di=8f0504a49b71a543956088d64914acf7&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fitbbs%2F1211%2F14%2Fc8%2F15758099_1352884051397_1024x1024it.jpg")
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            MvpViewModelFactory(BannerRepository(), UserRepository())
        ).get(MvpViewModel::class.java)
        viewModel.setLifeCycleProvide(this)
        viewModel.setLifecycleOwner(this)
    }

    fun onRequest(view: View) {
        when (view.id) {
            R.id.requestData -> sendGift(
                SendGiftVo(
                    "123",
                    "456",
                    1,
                    100,
                    0
                )
            )
        }
    }


    private fun sendGift(sendGiftVo: SendGiftVo) {
        showPopWindowLoading(true)
        viewModel.userRepository.sendGift(sendGiftVo)
            .compose(RxHelper.handleSingleResult())
            .bindToLifecycle(lifecycleProvider)
            .subscribe(RxObserver(doNext = {
                showPopWindowLoading(false)
                tv_msg.setText(it.msg)
            }, doError = { _, _ ->
                showPopWindowLoading(false)
            }))
    }
}
