package com.jy.simple.repository

import com.jy.commonlibrary.http.bean.BaseBean
import com.jy.commonlibrary.http.bean.SingleBaseBean
import com.jy.simple.bean.SendGiftVo
import com.jy.simple.network.Api
import com.jy.simple.network.bean.HttpRequest
import io.reactivex.Observable


/**

 * @Author Administrator
 * @Date 2019/11/4-15:42
 * @TODO 用户资源
 */
class UserRepository {

    fun sendGift(sendGiftVo: SendGiftVo): Observable<SingleBaseBean> {
        return Api.simpleInstance.sendGift(HttpRequest.obtainHttpRequest(sendGiftVo))
    }

    fun getUserRelationId(): Observable<BaseBean<String>> {
        return Api.simpleInstance.getUserRelationId()
    }
}