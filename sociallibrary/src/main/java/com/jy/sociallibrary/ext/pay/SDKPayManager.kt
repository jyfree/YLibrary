package com.jy.sociallibrary.ext.pay

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jy.sociallibrary.constant.SDKPayType
import com.jy.sociallibrary.ext.data.StatusLiveData
import com.jy.sociallibrary.helper.PayHelper
import com.jy.sociallibrary.listener.OnSocialSdkPayListener
import com.jy.sociallibrary.utils.SDKLogUtils
import com.jy.sociallibrary.wx.WXListener
import com.jy.sociallibrary.wx.WXPayBean


/**
 * @Author Administrator
 * @Date 2019/12/26-16:54
 * @TODO
 */
class SDKPayManager {

    private var payHelper: PayHelper? = null
    private var payListener: OnSocialSdkPayListener? = null
    private var wxListener: WXListener? = null
    private var progressView: View? = null
    private val PAY_TYPE = "payType"
    val ORDER_ID = "orderId"
    private val ALI_PAY_INFO = "aliPayInfo"
    private val WX_PAY_INFO = "wxPayInfo"

    fun setPayListener(payListener: OnSocialSdkPayListener): SDKPayManager {
        this.payListener = payListener
        return this
    }

    fun setWXListener(wxListener: WXListener): SDKPayManager {
        this.wxListener = wxListener
        return this
    }

    fun setProgressView(view: View?): SDKPayManager {
        this.progressView = view
        return this
    }

    fun requestWXPay(context: Context, wxPayBean: WXPayBean?) {
        if (wxPayBean == null) return
        val intent = Intent(context, SDKPayActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(PAY_TYPE, SDKPayType.TYPE_WX)
        intent.putExtra(ORDER_ID, wxPayBean.orderId)
        intent.putExtra(WX_PAY_INFO, wxPayBean)
        context.startActivity(intent)
    }

    fun requestALiPay(context: Context, orderId: String, payInfo: String) {
        val intent = Intent(context, SDKPayActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(PAY_TYPE, SDKPayType.TYPE_ALI)
        intent.putExtra(ORDER_ID, orderId)
        intent.putExtra(ALI_PAY_INFO, payInfo)
        context.startActivity(intent)
    }


    fun behavior(activity: Activity, savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            initSdkPay(activity)
            showProgress(true)
        }
    }


    private fun initSdkPay(activity: Activity) {
        payHelper = PayHelper(
            activity,
            object : OnSocialSdkPayListener {
                override fun paySuccess(type: Int, orderId: String?) {
                    onDestroy(activity)
                    payListener?.paySuccess(type, orderId)
                }

                override fun payFail(type: Int, error: String?) {
                    onDestroy(activity)
                    payListener?.payFail(type, error)
                }

                override fun payCancel(type: Int) {
                    onDestroy(activity)
                    payListener?.payCancel(type)
                }
            })
        payHelper?.setWxListener {
            SDKLogUtils.e("未安装微信")
            onDestroy(activity)
            wxListener?.installWXAPP()
        }
    }


    fun checkPay(activity: Activity, intent: Intent?) {

        if (intent == null) {
            SDKLogUtils.e("checkPay intent is null")
            onDestroy(activity)
            return
        }
        if (intent.extras == null) {
            SDKLogUtils.e("checkPay extras is null")
            onDestroy(activity)
            return
        }

        when (intent.getIntExtra(PAY_TYPE, 0)) {
            SDKPayType.TYPE_WX -> {
                val wxPayBean: WXPayBean? = intent.getParcelableExtra(WX_PAY_INFO)
                if (wxPayBean == null) {
                    onDestroy(activity)
                    return
                }
                wxPayOrderSuccess(wxPayBean)
            }
            SDKPayType.TYPE_ALI -> {
                val orderId = intent.getStringExtra(ORDER_ID)
                val payInfo = intent.getStringExtra(ALI_PAY_INFO)
                if (orderId == null || payInfo == null) {
                    onDestroy(activity)
                    return
                }
                aliPlayOrderSuccess(orderId, payInfo)
            }
        }
    }

    /**
     * 下单成功，调起支付宝
     */
    private fun aliPlayOrderSuccess(orderId: String, payInfo: String) {
        payHelper?.aliPay(orderId, payInfo)
    }

    /**
     * 下单成功，调起微信
     */
    private fun wxPayOrderSuccess(wxPayBean: WXPayBean) {
        payHelper?.wxPay(wxPayBean)
    }


    fun onResultToWXPaySuccess(activity: Activity, orderId: String) {
        onDestroy(activity)
        payListener?.paySuccess(SDKPayType.TYPE_WX, orderId)
    }

    fun onResultToWXPayCancel(activity: Activity) {
        onDestroy(activity)
        payListener?.payCancel(SDKPayType.TYPE_WX)
    }

    fun onResultToWXPayFail(activity: Activity, errCode: Int) {
        onDestroy(activity)
        payListener?.payFail(SDKPayType.TYPE_WX, "错误码：$errCode")
    }


    private fun showProgress(show: Boolean) {
        if (progressView == null) {
            payHelper?.showProgressDialog(show)
        } else {
            progressView?.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    /**
     * 摧毁本库的 SDKPayActivity
     */
    private fun onDestroy(activity: Activity?) {
        showProgress(false)
        activity?.finish()
        //因为StatusLiveData是单例，所以必须置空
        StatusLiveData.getInstance().value = null
    }
}
