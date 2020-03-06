package com.jy.sociallibrary.ext

/**

 * @Author Administrator
 * @Date 2019/9/27-15:39
 * @TODO 常量
 */
object SDKConstants {

    object LoginStatus {
        const val WX_LOGIN_SUCCEED = 1000//微信登录授权--成功
        const val WX_LOGIN_CANCEL = 1001//微信登录授权--取消
        const val WX_LOGIN_FAIL = 1002//微信登录授权--失败
    }

    object ShareStatus {
        const val WX_SHARE_SUCCESS = 2000//微信分享--成功
        const val WX_SHARE_CANCEL = 2001//微信分享--取消
        const val WX_SHARE_FAIL = 2002//微信分享--失败
    }

    object PayStatus {
        const val WX_PAY_SUCCESS = 3000//微信支付--成功
        const val WX_PAY_CANCEL = 3001//微信支付--取消
        const val WX_PAY_FAIL = 3002//微信支付--失败
    }
}