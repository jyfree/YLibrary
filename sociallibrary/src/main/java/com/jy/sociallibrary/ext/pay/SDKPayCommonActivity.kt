package com.jy.sociallibrary.ext.pay

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jy.sociallibrary.constant.SDKPayType
import com.jy.sociallibrary.ext.SDKConstants
import com.jy.sociallibrary.ext.data.StatusBean
import com.jy.sociallibrary.ext.data.StatusLiveData
import com.jy.sociallibrary.helper.PayHelper
import com.jy.sociallibrary.listener.OnSocialSdkPayListener
import com.jy.sociallibrary.utils.SDKLogUtils
import com.jy.sociallibrary.wx.WXListener
import com.jy.sociallibrary.wx.WXPayBean

/**
 * @description 实现支付的activity，若继承此activity，则无需在manifest注册透明的SDKPayActivity
 * @date: 2021/4/6 14:25
 * @author: jy
 */
abstract class SDKPayCommonActivity : AppCompatActivity(), OnSocialSdkPayListener, WXListener {

    private var payHelper: PayHelper? = null
    var progressView: View? = null
    var orderId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initSdkPay()
            registerObserve(this)
        }
    }

    private fun initSdkPay() {
        payHelper = PayHelper(this, this)
        payHelper?.setWxListener(this)
    }

    /**
     * 必需注册LiveData，用于微信回调
     * todo 注意：从任务管理器切换回app是无法收到微信回调的
     */
    private fun registerObserve(owner: LifecycleOwner) {
        StatusLiveData.getInstance().observe(owner, Observer<StatusBean?> {
            it?.let {
                when (it.status) {
                    SDKConstants.PayStatus.WX_PAY_SUCCESS -> {
                        SDKLogUtils.i("接收到MutableLiveData--微信支付--成功")
                        paySuccess(SDKPayType.TYPE_WX, orderId)
                    }
                    SDKConstants.PayStatus.WX_PAY_FAIL -> {
                        SDKLogUtils.e("接收到MutableLiveData--微信支付--失败--errCode", it.errCode)
                        payFail(SDKPayType.TYPE_WX, "错误码：$it.errCode")
                    }
                    SDKConstants.PayStatus.WX_PAY_CANCEL -> {
                        SDKLogUtils.i("接收到MutableLiveData--微信支付--取消")
                        payCancel(SDKPayType.TYPE_WX)
                    }
                    else -> {

                    }
                }
            }
        })
    }

    /**
     * 下单成功，调起支付宝
     */
    fun aliPlayOrderSuccess(orderId: String, payInfo: String) {
        this.orderId = orderId
        showProgress(true)
        payHelper?.aliPay(orderId, payInfo)
    }

    /**
     * 下单成功，调起微信
     */
    fun wxPayOrderSuccess(wxPayBean: WXPayBean) {
        this.orderId = wxPayBean.orderId
        showProgress(true)
        payHelper?.wxPay(wxPayBean)
    }


    override fun paySuccess(type: Int, orderId: String?) {
        reset()
        onPaySuccess(type, orderId)
    }

    override fun payFail(type: Int, error: String?) {
        reset()
        onPayFail(type, error)
    }

    override fun payCancel(type: Int) {
        reset()
        onPayCancel(type)
    }

    override fun startWX(isSucceed: Boolean) {
        if (isSucceed) {
            showProgress(false)
        } else {
            reset()
        }
        onStartWXApp(isSucceed)
    }

    override fun installWXAPP() {
        SDKLogUtils.e("未安装微信")
        reset()
        onUnInstallWXApp()
    }

    private fun showProgress(show: Boolean) {
        if (progressView == null) {
            payHelper?.showProgressDialog(show)
        } else {
            progressView?.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    private fun reset() {
        showProgress(false)
        //因为StatusLiveData是单例，所以必须置空
        StatusLiveData.getInstance().value = null
    }

    abstract fun onPaySuccess(type: Int, orderId: String?)
    abstract fun onPayFail(type: Int, error: String?)
    abstract fun onPayCancel(type: Int)
    abstract fun onStartWXApp(isSucceed: Boolean)
    abstract fun onUnInstallWXApp()
}