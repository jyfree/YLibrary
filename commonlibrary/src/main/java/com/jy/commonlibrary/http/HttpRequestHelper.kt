package com.jy.commonlibrary.http

import com.jy.baselibrary.utils.ObjectUtils


object HttpRequestHelper {

    fun obtainHttpRequestFiled(body: MutableMap<String, Any>, vararg filed: Map.Entry<String, Any?>) {
        filed.forEach {
            if (it.value != null)
                body[it.key] = it.value!!
        }
    }

    fun obtainHttpRequestAny(body: MutableMap<String, Any>, any: Any) {
        any::class.java.declaredFields.forEach {
            val value = ObjectUtils.getValueByFieldName(it.name, any)
            if (value != null && "null" != value && "" != value)
                body[it.name] = value
        }
    }
}