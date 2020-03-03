package com.jy.sociallibrary.listener;

/**
 * @Author Administrator
 * @Date 2019/9/29-18:07
 * @TODO
 */
public interface OnSocialSdkPayListener {

    /**
     * 支付成功
     *
     * @param type    支付类型
     * @param orderId 订单id
     */
    void paySuccess(int type, String orderId);

    /**
     * 支付错误
     *
     * @param type  支付类型
     * @param error 错误信息
     */
    void payFail(int type, String error);

    /**
     * 取消支付
     *
     * @param type
     */
    void payCancel(int type);


}
