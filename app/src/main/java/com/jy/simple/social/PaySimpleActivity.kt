package com.jy.simple.social

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseAppCompatActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.simple.R
import com.jy.sociallibrary.ext.pay.SDKPay
import com.jy.sociallibrary.ext.pay.SDKPayManager
import com.jy.sociallibrary.listener.OnSocialSdkPayListener
import com.jy.sociallibrary.wx.WXListener
import com.jy.sociallibrary.wx.WXPayBean


/**

 * @Author Administrator
 * @Date 2019/11/12-14:40
 * @TODO
 */
class PaySimpleActivity : BaseAppCompatActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, PaySimpleActivity::class.java)
        }
    }

    private var sdkPayManager: SDKPayManager? = null

    override fun initLayoutID(): Int = R.layout.simple_pay_activity


    override fun initUI(savedInstanceState: Bundle?) {

    }

    fun onClickPay(view: View) {
        when (view.id) {
            R.id.pay_2ali -> {
                val payInfo =
                    "alipay_sdk=alipay-sdk-java-3.4.49.ALL&app_id=2019010962861643&biz_content=%7B%22body%22%3A%22WOWO%E8%AF%AD%E9%9F%B3%E9%87%91%E5%B8%81%22%2C%22out_trade_no%22%3A%221699%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22WOWO%E8%AF%AD%E9%9F%B3%E9%87%91%E5%B8%81%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%2250.0%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2Ftestservice.wowolive99.com%2F%2Fpay%2Fnotify%2Falipay.do&sign=wLmxxtlfRUvypLM3y1eoGyggkFZH5ZQHuMH1e1r2rET%2BtKRmhE8gJFI89cctCeGMB%2Bns3bdw9Ay%2BLrMxcnroEPTbRD69M0i80jcG84t%2BjA%2BO1PlAstfyKAr4tnw5n4GkkGQGuTx61wZyYWDJ56fIacoJDVlXGm2PnJvRJZYyHApJGE%2BbORE0GVTfzHvWxH%2BMLklARgLViDPkGvGXXmKL%2Fif%2By3o9C8HfnHYINPaLENBY0BCjm%2FkxlDD7nO65uX4tBlCbDYDKl3196xk2lvDd8nLtM5222Y5tDjL3900rOwgAHVA81qirBk4IQwBH8aavr%2F6ZhlF%2FUQGJ619IKXVXwA%3D%3D&sign_type=RSA2×tamp=2019-10-08+11%3A07%3A30&version=1.0"
                getSdkPayManager().requestALiPay(this, "1699", payInfo)
            }
            R.id.pay_2wx -> {
                val wxPayVo = WXPayBean()
                wxPayVo.partnerId = "1313981201"
                wxPayVo.prepayId = "wx0817275229598459db05841f1027564800"
                wxPayVo.packageValue = "Sign=WXPay"
                wxPayVo.nonceStr = "jx4ia8vddbc9h646fu8ggjm6cmdk4y2k"
                wxPayVo.timeStamp = "1570526872"
                wxPayVo.sign = "0B47048F589697A9D1A467B8B2EBF80A"
                getSdkPayManager().requestWXPay(this, wxPayVo)
            }
        }
    }

    private fun getSdkPayManager(): SDKPayManager {
        if (sdkPayManager == null) {
            sdkPayManager =
                SDKPay.instance.sdkPayManager.setPayListener(object : OnSocialSdkPayListener {
                    override fun paySuccess(type: Int, orderId: String?) {
                        YLogUtils.i("支付成功--类型：", type, "orderId", orderId)
                    }

                    override fun payFail(type: Int, error: String?) {
                        YLogUtils.e("支付失败--类型：", type, "error", error)
                    }

                    override fun payCancel(type: Int) {
                        YLogUtils.i("取消支付--类型：", type)
                    }

                }).setWXListener(object : WXListener {
                    override fun startWX(isSucceed: Boolean) {
                        YLogUtils.e("启动微信成功？", isSucceed)
                    }

                    override fun installWXAPP() {
                        YLogUtils.e("未安装微信")
                    }

                }).registerObserve(this)
        }
        return sdkPayManager!!
    }
}