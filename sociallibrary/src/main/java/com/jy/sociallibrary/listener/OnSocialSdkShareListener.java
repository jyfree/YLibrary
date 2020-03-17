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
     * @param sharePlatform 分享平台
     */
    void shareSuccess(int sharePlatform);

    /**
     * 分享失败
     *
     * @param sharePlatform 分享平台
     */
    void shareFail(int sharePlatform, String error);

    /**
     * 取消分享
     *
     * @param sharePlatform 分享平台
     */
    void shareCancel(int sharePlatform);


}
