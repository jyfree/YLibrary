package com.jy.simple.db

import com.jy.litedb.annotation.Dao
import com.jy.litedb.api.IDao

/**
 * @description db操作接口
 * @date: 2020/6/28 14:32
 * @author: jy
 */
@Dao(entities = TestInfo::class)
interface TestInfoDao : IDao<TestInfo> {
}