package com.jy.sociallibrary.listener;

/**
 * @Author Administrator
 * @Date 2019/9/29-17:45
 * @TODO
 */
public interface OnSocialSdkLoginListener {

    /**
     * 微博登录回调
     *
     * @param token
     * @param info
     */
    void loginAuthSuccess(int type, String token, String info);

    /**
     * 登录失败
     *
     * @param type
     * @param error
     */
    void loginFail(int type, String error);

    /**
     * 取消登录
     *
     * @param type
     */
    void loginCancel(int type);


}
