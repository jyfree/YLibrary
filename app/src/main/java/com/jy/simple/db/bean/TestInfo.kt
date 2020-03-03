package com.jy.simple.db.bean

import com.jy.commonlibrary.db.Scope


/**
 * @Author Administrator
 * @Date 2019/11/7-15:27
 * @TODO
 */
class TestInfo {
    @Scope(isPrimaryKey = true, isAutoKey = true)
    var id: Int = 0
    var savePath: String? = null
    var countLength: Int = 0
    var readLength: Int = 0
    var connectionTime: Int = 0
    var stateInt: Int = 0
    @Scope(isCompareField = true)
    var url: String? = null
    var updateProgress: Int = 0
    @Scope(isUpdateField = true, updateFieldVersion = 2)
    var testUpdate: String? = null
    @Scope(isUpdateField = true, updateFieldVersion = 3)
    var testUpdateTwo: String? = null
    @Scope(isFilter = true)
    var testFilter: String? = null
}
