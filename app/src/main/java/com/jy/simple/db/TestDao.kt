package com.jy.simple.db

import com.jy.simple.db.bean.TestInfo


/**
 * @Author Administrator
 * @Date 2019/10/28-13:59
 * @TODO
 */
object TestDao {

    private var dbWrapper: DBWrapper<TestInfo>? = null

    init {
        dbWrapper = DBWrapper(TestInfo::class.java)
    }

    fun getInstance(): DBWrapper<TestInfo> {
        return dbWrapper!!
    }
}
