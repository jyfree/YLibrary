package com.jy.simple.http

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.contract.BaseContract
import com.jy.baselibrary.base.mvp.MvpBaseActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.commonlibrary.glide.setImageDefaultLoadIconUrl
import com.jy.simple.R
import com.jy.simple.http.mvp.ApiSimpleContract
import com.jy.simple.http.mvp.ApiSimpleModel
import com.jy.simple.http.mvp.ApiSimplePresenter
import kotlinx.android.synthetic.main.simple_api_test_activity.*

/**

 * @Author Administrator
 * @Date 2019/9/27-9:43
 * @TODO mvp 请求
 */
class MvpApiSimpleActivity : MvpBaseActivity<ApiSimplePresenter>(), ApiSimpleContract.View {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, MvpApiSimpleActivity::class.java)
        }
    }

    override fun initPresenter(): ApiSimplePresenter = ApiSimplePresenter()

    override fun initModel(): BaseContract.BaseModel = ApiSimpleModel()

    override fun initLayoutID(): Int = R.layout.simple_api_test_activity

    override fun initView(savedInstanceState: Bundle?) {
        imageView.setImageDefaultLoadIconUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569672004826&di=3bc2d7d26f6b57726a8ac377e1aeb982&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20130416%2FImg372885486.jpg")
    }

    fun onRequest(view: View) {
        when (view.id) {
            R.id.requestData -> mPresenter.getBanner(1)
        }
    }

    override fun updateInfo(msg: String?) {
        tv_msg.text = msg
    }
}