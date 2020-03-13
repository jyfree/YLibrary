package com.jy.sociallibrary.ext.pay

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jy.sociallibrary.ext.SDKConstants
import com.jy.sociallibrary.ext.data.StatusBean
import com.jy.sociallibrary.ext.data.StatusLiveData
import com.jy.sociallibrary.manager.SDKThreadManager
import com.jy.sociallibrary.utils.SDKLogUtils


/**

 * @Author Administrator
 * @Date 2019/12/26-16:52
 * @TODO
 */
class SDKPayActivity : AppCompatActivity(), Observer<StatusBean> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //不接受触摸屏事件
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        StatusLiveData.getInstance().observe(this, this);
        ExtPay.instance.sdkPayManager.behavior(this, savedInstanceState)
        initCompleteTime()
    }

    private fun initCompleteTime() {
        window.decorView.post {
            SDKThreadManager.getMainHandler().post {
                ExtPay.instance.sdkPayManager.checkPay(this@SDKPayActivity, intent)
            }
        }
    }

    override fun onChanged(t: StatusBean?) {
        t?.let {
            when (t.status) {
                SDKConstants.PayStatus.WX_PAY_SUCCESS -> {
                    SDKLogUtils.i("接收到MutableLiveData--微信支付--成功")
                    val orderId =
                        intent.getStringExtra(ExtPay.instance.sdkPayManager.ORDER_ID) ?: ""
                    ExtPay.instance.sdkPayManager.onResultToWXPaySuccess(this, orderId)
                }
                SDKConstants.PayStatus.WX_PAY_FAIL -> {
                    SDKLogUtils.e("接收到MutableLiveData--微信支付--失败--errCode", it.errCode)
                    ExtPay.instance.sdkPayManager.onResultToWXPayFail(this, it.errCode)
                }
                SDKConstants.PayStatus.WX_PAY_CANCEL -> {
                    SDKLogUtils.i("接收到MutableLiveData--微信支付--取消")
                    ExtPay.instance.sdkPayManager.onResultToWXPayCancel(this)
                }
            }
        }
    }

}