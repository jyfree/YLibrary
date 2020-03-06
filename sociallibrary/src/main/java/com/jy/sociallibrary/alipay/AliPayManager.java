package com.jy.sociallibrary.alipay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.jy.sociallibrary.manager.SDKThreadManager;
import com.jy.sociallibrary.constant.SDKPayType;
import com.jy.sociallibrary.listener.OnSocialSdkPayListener;
import com.jy.sociallibrary.utils.SDKLogUtils;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


public class AliPayManager {

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Context context;

    private PayBean payBean;//支付方式1

    private OnSocialSdkPayListener payListener;

    private PayResultHandler payResultHandler;

    public AliPayManager(Context context, OnSocialSdkPayListener payListener) {
        this.context = context;
        this.payListener = payListener;
        payResultHandler = new PayResultHandler(this);
    }

    private static class PayResultHandler extends Handler {

        WeakReference<AliPayManager> mAliPay;

        PayResultHandler(AliPayManager aliPay) {
            mAliPay = new WeakReference<>(aliPay);
        }


        @Override
        public void handleMessage(Message msg) {
            AliPayManager mPay = mAliPay.get();
            if (mPay == null) return;

            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
//                    String resultInfo = payResult.getResult();
//                    String tmpStr = mPay.getOrderInfo();
//                    boolean isSucceed = SignUtils.verify(tmpStr, payResult.getSign(), RSA_PUBLIC);
//                    YLogUtil.logD(isSucceed);

                    String resultStatus = payResult.getResultStatus();


                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        SDKLogUtils.i("支付宝支付--成功--状态码", resultStatus, "响应结果", payResult);
                        mPay.payListener.paySuccess(SDKPayType.TYPE_ALI, "");
                    } else {
                        //取消支付
                        if (TextUtils.equals(resultStatus, "6001")) {
                            SDKLogUtils.i("支付宝支付--取消--状态码", resultStatus, "响应结果", payResult);
                            mPay.payListener.payCancel(SDKPayType.TYPE_ALI);
                        }
                        //支付失败
                        else {
                            SDKLogUtils.e("支付宝支付--失败--状态码", resultStatus, "响应结果", payResult);
                            mPay.payListener.payFail(SDKPayType.TYPE_ALI, resultStatus);
                        }


//                        // 判断resultStatus 为非“9000”则代表可能支付失败
//                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//                        if (TextUtils.equals(resultStatus, "8000")) {
//                            showToast(mPay.context, R.string.social_sdk_toast_pay_inConfirmation);
//                        }
//                        //网络连接出错
//                        else if (TextUtils.equals(resultStatus, "6002")) {
//                            showToast(mPay.context, R.string.social_sdk_toast_pay_networkFail);
//                        }
//                        //用户中途取消
//                        else if (TextUtils.equals(resultStatus, "6001")) {
//                            showToast(mPay.context, R.string.social_sdk_toast_pay_cancel);
//                        } else {
//                            // 其他值就可以判断为支付失败(resultStatus=4000)，包括用户主动取消支付，或者系统返回的错误
//                            if (mPay.context == null) return;
//                            showToast(mPay.context, mPay.context.getString(R.string.social_sdk_toast_pay_fail) + payResult.getMemo());
//                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    SDKLogUtils.i("检查结果为：", msg.obj);
                    break;
                }
                default:
                    break;
            }
        }
    }


    /**
     * call aliPay sdk pay. 调用SDK支付（客户端装载支付连接）
     */
    public void pay(PayBean bean) {
        this.payBean = bean;
        // 订单
        String orderInfo = getOrderInfo();

        // 对订单做RSA 签名
        String sign = payBean.getSign();//sign(orderInfo);

        SDKLogUtils.i("服务器sign", payBean.getSign());
//        SDKLogUtil.i("客户端sign=" + sign(orderInfo));
        SDKLogUtils.i("orderInfo", orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        start(payInfo);

    }

    /**
     * call aliPay sdk pay. 调用SDK支付（服务端装载支付连接）
     */
    public void pay(String payInfo) {
        start(payInfo);
        SDKLogUtils.i("payInfo", payInfo);
    }

    private void start(final String dataInfo) {
        SDKThreadManager.executeOnNetWorkThread(new Runnable() {
            @Override
            public void run() {
                if (context == null) return;
                // 构造PayTask 对象
                PayTask aliPay = new PayTask((Activity) context);
                // 调用支付接口，获取支付结果
                Map<String, String> result = aliPay.payV2(dataInfo, true);
                SDKLogUtils.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                payResultHandler.sendMessage(msg);
            }
        });
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check() {
        SDKThreadManager.executeOnNetWorkThread(new Runnable() {
            @Override
            public void run() {
                if (context == null) return;
                // 构造PayTask 对象
                PayTask payTask = new PayTask((Activity) context);
                // 调用查询接口，获取查询结果
//                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = false;
                payResultHandler.sendMessage(msg);
            }
        });

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        if (context == null) return;
        PayTask payTask = new PayTask((Activity) context);
        String version = payTask.getVersion();
        Toast.makeText(context, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo() {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + payBean.getPartner() + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + payBean.getSellerId() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + payBean.getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + payBean.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + payBean.getBody() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + payBean.getTotalFee() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + payBean.getNotifyUrl() + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=" + "\"" + payBean.getService() + "\"";//\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=" + "\"" + payBean.getPaymentType() + "\"";//\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=" + "\"" + payBean.getInputCharset() + "\"";//\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=" + "\"" + payBean.getItBPay() + "\"";//\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        //orderInfo += "&return_url="+ "\"" + payBean.getReturnUrl()+ "\"";//\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    private String outTradeNo = "";//订单号

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);

        this.outTradeNo = key;

        return outTradeNo;
    }

    /**
     * 设置订单号
     *
     * @param outTradeNo
     */
    private void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
//    private String sign(String content) {
//        return SignUtils.sign(content, RSA_PRIVATE);
//    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
