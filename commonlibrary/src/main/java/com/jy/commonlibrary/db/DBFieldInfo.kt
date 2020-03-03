package com.jy.commonlibrary.db

import java.lang.reflect.Field

/**

 * @Author Administrator
 * @Date 2019/10/28-10:07
 * @TODO db反射字段信息
 */
data class DBFieldInfo(
        var target: Class<*>,
        var mField: Field,
        var isPrimaryKey: Boolean,
        var isAutoKey: Boolean,
        var isUpdateField: Boolean,
        var updateFieldVersion: Int = 1,
        var name: String,
        var type: Class<*>,
        var isFilter: Boolean = false,
        var isCompareField: Boolean = false
)