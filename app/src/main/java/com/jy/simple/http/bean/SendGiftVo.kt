package com.jy.simple.http.bean

/**

 * @Author Administrator
 * @Date 2019/9/28-11:35
 * @TODO
 */
data class SendGiftVo(
        var roomId: String?,
        var toUserId: String?,
        var giftId: Int?,
        var num: Int?,
        var giftSource: Int?
)