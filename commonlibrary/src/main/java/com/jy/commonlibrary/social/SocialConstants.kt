package com.jy.commonlibrary.social

/**

 * @Author Administrator
 * @Date 2019/9/27-15:39
 * @TODO 常量
 */
object SocialConstants {

    object RxBus {
        const val CODE_WX_LOGIN_AUTH_SUCCEED = 666//微信登录授权--成功
        const val CODE_WX_LOGIN_AUTH_CANCEL = 667//微信登录授权--取消
        const val CODE_WX_LOGIN_AUTH_FAIL = 668//微信登录授权--失败

        const val CODE_WX_SHARE_SUCCESS = 669//微信分享--成功
        const val CODE_WX_SHARE_CANCEL = 670//微信分享--取消
        const val CODE_WX_SHARE_FAIL = 671//微信分享--失败

        const val CODE_WX_PAY_SUCCESS = 672//微信支付--成功
        const val CODE_WX_PAY_CANCEL = 673//微信支付--取消
        const val CODE_WX_PAY_FAIL = 674//微信支付--失败

    }
}