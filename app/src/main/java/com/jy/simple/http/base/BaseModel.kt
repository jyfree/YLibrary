package com.jy.simple.http.base

import com.jy.baselibrary.base.model.SuperBaseModel
import com.jy.commonlibrary.http.ApiServiceManager

/**

 * @Author Administrator
 * @Date 2019/9/27-12:01
 * @TODO
 */
open class BaseModel<T>(service: Class<T>) : SuperBaseModel<T>(service) {
    val serviceManager = ApiServiceManager.create(service)
}