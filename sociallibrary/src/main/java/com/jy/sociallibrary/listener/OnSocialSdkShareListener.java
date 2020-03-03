package com.jy.sociallibrary.listener;

/**
 * @Author Administrator
 * @Date 2019/9/29-18:02
 * @TODO
 */
public interface OnSocialSdkShareListener {

    /**
     * 分享成功
     *
     * @param type 分享类型
     */
    void shareSuccess(int type);

    /**
     * 分享失败
     *
     * @param type 分享类型
     */
    void shareFail(int type, String error);

    /**
     * 取消分享
     *
     * @param type 分享类型
     */
    void shareCancel(int type);


}
