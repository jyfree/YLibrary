package com.jy.commonlibrary.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


/**

 * @Author Administrator
 * @Date 2019/10/26-14:35
 * @TODO 公共增删改查db
 */
open class BaseWrapper<T : Any> constructor(private var subClass: Class<T>) : BaseDao<T>() {


    override val tableName: String
        get() = subClass.simpleName

    private val subClassFields = subClass.declaredFields

    override fun getSubClass(): Class<T> = subClass


    override fun getContentValues(item: T): ContentValues {
        val values = ContentValues()
        val fields = item.javaClass.declaredFields

        for (fie in fields) {
            fie.isAccessible = true
            //过滤掉编译器自动生成的成员变量
            if (fie.isSynthetic) {
                continue
            }
            var value = fie.get(item)
            if (value == null) {
                value = ""
            }
            //解析注解
            if (fie.isAnnotationPresent(Scope::class.java)) {
                val scope = fie.getAnnotation(Scope::class.java)
                if (scope != null && scope.isPrimaryKey && scope.isAutoKey) {
//                    YLogUtil.iFormatTag("BaseWrapper", "插入|更新--自增字段--表名：%s--字段名：%s", tableName, fie.name)
                    continue
                }
                if (scope != null && scope.isFilter) {
//                    YLogUtil.iFormatTag("BaseWrapper", "插入|更新--过滤字段--表名：%s--字段名：%s", tableName, fie.name)
                    continue
                }
            }
            when {
                Int::class.java == fie.type -> values.put(fie.name, value as Int)
                Long::class.java == fie.type -> values.put(fie.name, value as Long)
                Float::class.java == fie.type -> values.put(fie.name, value as Float)
                String::class.java == fie.type -> values.put(fie.name, value as String)
                Boolean::class.java == fie.type -> {
                    val tmpB = if (value as Boolean) 1 else 0
                    values.put(fie.name, tmpB)
                }
                Double::class.java == fie.type -> values.put(fie.name, value as Double)
                else -> {
                    values.put(fie.name, value as String)
                }
            }
        }

        return values
    }

    override fun getItemInfo(cursor: Cursor): T {

        val fields = subClassFields
        val subObject = subClass.newInstance()

        for (fie in fields) {
            fie.isAccessible = true
            //过滤掉编译器自动生成的成员变量
            if (fie.isSynthetic) {
                continue
            }
            //解析注解
            if (fie.isAnnotationPresent(Scope::class.java)) {
                val scope = fie.getAnnotation(Scope::class.java)
                if (scope != null && scope.isFilter) {
//                    YLogUtil.iFormatTag("BaseWrapper", "查询--过滤字段--表名：%s--字段名：%s", tableName, fie.name)
                    continue
                }
            }
            when {
                Int::class.java == fie.type -> fie.set(subObject, getInt(cursor, fie.name))
                Long::class.java == fie.type -> fie.set(subObject, getLong(cursor, fie.name))
                Float::class.java == fie.type -> fie.set(subObject, getFloat(cursor, fie.name))
                String::class.java == fie.type -> fie.set(subObject, getString(cursor, fie.name))
                Boolean::class.java == fie.type -> {
                    val tmpB = getInt(cursor, fie.name) == 1
                    fie.set(subObject, tmpB)
                }
                Double::class.java == fie.type -> fie.set(subObject, getDouble(cursor, fie.name))
                else -> {
                    fie.set(subObject, getString(cursor, fie.name))
                }
            }
        }

        return subObject
    }

    override fun compareItem(item1: T, item2: T): Boolean {
        val value1 = getValue(item1)
        val value2 = getValue(item2)
        if (value1 == null || value2 == null) {
            return false
        }
        return value1 == value2
    }

    override fun updateItem(db: SQLiteDatabase, item: T) {
        val fields = subClassFields
        var value: Any? = ""
        var key: Any? = null
        for (fie in fields) {
            fie.isAccessible = true
            if (fie.isAnnotationPresent(Scope::class.java)) {
                val scope = fie.getAnnotation(Scope::class.java)
                if (scope != null && scope.isCompareField) {
                    value = fie.get(item)
                    key = fie.name
                    break
                }
            }
        }
        db.update(tableName, getContentValues(item), "$key = ?", arrayOf(value?.toString()))
    }


    private fun getValue(item1: T): Any? {
        val fields = item1.javaClass.declaredFields
        var value: Any? = null
        for (fie in fields) {
            fie.isAccessible = true
            if (fie.isAnnotationPresent(Scope::class.java)) {
                val scope = fie.getAnnotation(Scope::class.java)
                if (scope != null && scope.isCompareField) {
                    value = fie.get(item1)
                    break
                }
            }
        }
        return value
    }

}