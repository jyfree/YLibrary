package com.jy.commonlibrary.db

import com.jy.baselibrary.utils.YLogUtils
import java.util.*

/**
 * @Author Administrator
 * @Date 2019/10/28-14:08
 * @TODO 反射管理器
 */
object DBFieldManager {

    private val TAG = DBFieldManager::class.java.simpleName

    /**
     * 解析注解，生成注解信息集合
     */
    private fun register(subClass: Class<*>): List<DBFieldInfo> {
        val fields = subClass.declaredFields
        val list = ArrayList<DBFieldInfo>()
        for (fie in fields) {
            fie.isAccessible = true
            //过滤掉编译器自动生成的成员变量
            if (fie.isSynthetic) {
                continue
            }

            var isPrimaryKey = false
            var isAutoKey = false
            var isUpdateField = false
            var updateFieldVersion = 1
            var isFilter = false
            var isCompareField = false

            //解析注解
            if (fie.isAnnotationPresent(Scope::class.java)) {
                val scope = fie.getAnnotation(Scope::class.java)

                isPrimaryKey = scope.isPrimaryKey
                isAutoKey = scope.isAutoKey
                isUpdateField = scope.isUpdateField
                updateFieldVersion = scope.updateFieldVersion
                isFilter = scope.isFilter
                isCompareField = scope.isCompareField
            }
            list.add(DBFieldInfo(subClass, fie, isPrimaryKey, isAutoKey, isUpdateField, updateFieldVersion, fie.name, fie.type, isFilter, isCompareField))
        }

        return list
    }

    /**
     * 获取注解信息集合
     */
    private fun getScopeListInfo(subClass: Class<*>): List<DBFieldInfo> {

        return register(subClass)
    }


    /**
     * 创建表
     */
    fun createTable(subClass: Class<*>): String {

        val list = getScopeListInfo(subClass)

        val sb = StringBuilder("CREATE TABLE ")
        sb.append(subClass.simpleName)
        sb.append(" (")

        for (it in list) {
            if (it.isFilter) {
                continue
            }
            sb.append(it.name)
            sb.append(" ")
            //判断是否为主键
            if (it.isPrimaryKey) {
                if (it.isAutoKey) {
                    sb.append("INTEGER PRIMARY KEY AUTOINCREMENT")
                } else {
                    sb.append("PRIMARY KEY")
                }
            } else {
                when {
                    Int::class.java == it.type -> sb.append("INTEGER")
                    Long::class.java == it.type -> sb.append("INTEGER")
                    Float::class.java == it.type -> sb.append("float")
                    String::class.java == it.type -> sb.append("TEXT")
                    Boolean::class.java == it.type -> sb.append("INTEGER")
                    Double::class.java == it.type -> sb.append("double")
                    else -> sb.append("TEXT")
                }
            }
            sb.append(", ")
        }

        val sqlMsg = sb.substring(0, sb.lastIndexOf(", ")) + ");"
        YLogUtils.iTag(TAG, "创建表", sqlMsg)

        return sqlMsg

    }

    /**
     * 新增字段
     *
     * @param subClass
     * @param oldVersion
     * @return
     */
    fun addField(subClass: Class<*>, oldVersion: Int): List<String> {

        val formatStr = "alter table [%s] add %s %s"
        val sqlList = ArrayList<String>()

        val fields = subClass.declaredFields
        for (fie in fields) {
            fie.isAccessible = true
            //过滤掉编译器自动生成的成员变量
            if (fie.isSynthetic) {
                continue
            }
            //解析注解
            if (fie.isAnnotationPresent(Scope::class.java)) {
                val scope = fie.getAnnotation(Scope::class.java)
                //更新字段
                if (scope.isUpdateField && oldVersion < scope.updateFieldVersion) {
                    val typeStr: String = when {
                        Int::class.java == fie.type -> "INTEGER"
                        Long::class.java == fie.type -> "INTEGER"
                        Float::class.java == fie.type -> "float"
                        String::class.java == fie.type -> "TEXT"
                        Boolean::class.java == fie.type -> "INTEGER"
                        Double::class.java == fie.type -> "double"
                        else -> "TEXT"
                    }
                    val sql = String.format(formatStr, subClass.simpleName, fie.name, typeStr)
                    sqlList.add(sql)

                    YLogUtils.iTag(TAG, "修改表", sql)

                }
            }
        }
        return sqlList
    }

}
