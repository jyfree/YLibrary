package com.jy.simple.http.bean

import com.jy.commonlibrary.http.bean.SingleBaseBean


/**

 * @Author Administrator
 * @Date 2019/9/26-18:07
 * @TODO
 */
/**
 * Administrator
 * created at 2018/11/23 13:32
 * TODO:banner
 */
data class BannerInfoVo(
        var id: Int?,//id
        var title: String?,//标题
        var url: String?,//跳转地址
        var showImg: String?,//显示的图片
        var status: Int?,//	状态：0：停用 1：启用
        var beginTime: Long?,//启用开始时间
        var endTime: Long?,//结束时间
        var createTime: Long?,//创建时间
        var showPlace: Int?,//展示位置，1-首页，2-发现，3-充值中心
        var type: Int = 1//1.大弹窗，2.下弹窗
)

data class BannerInfoListVo(var data: ArrayList<BannerInfoVo>?) : SingleBaseBean()
