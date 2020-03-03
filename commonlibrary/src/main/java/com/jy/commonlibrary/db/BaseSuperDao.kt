package com.jy.commonlibrary.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.utils.YConvertUtils
import java.util.*
import kotlin.collections.ArrayList


/**

 * @Author Administrator
 * @Date 2019/10/26-13:24
 * @TODO 数据库表超类
 */
abstract class BaseSuperDao<T> {

    private val hashMap = HashMap<String, Int>()

    /**
     * 获取表名
     *
     * @return
     */
    abstract val tableName: String


    /**
     * 插入单条数据
     *
     * @param item
     */
    @Synchronized
    fun insert(item: T) {
        try {
            val db = DBManager.getInstance().openDatabase()
            if (db.isOpen) {
                db.insert(tableName, null, getContentValues(item))
            }
            //加入缓存
            addCache(item)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            DBManager.getInstance().closeDatabase()
        }
    }

    /**
     * 批量插入（旧数据删除）
     *
     * @param dataList
     */
    @Synchronized
    fun insert(dataList: ArrayList<T>) {
        try {
            val db = DBManager.getInstance().openDatabase()
            if (db.isOpen) {
                db.beginTransaction() // 手动设置开始事务

                deleteAll(db)

                for (item in dataList) {
                    db.insert(tableName, null, getContentValues(item))

                }
                db.setTransactionSuccessful() // 设置事务处理成功，不设置会自动回滚不提交
                db.endTransaction() // 处理完成
            }
            //加入缓存
            DBCacheManager.instance.dbCache.putList(tableName, dataList)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            DBManager.getInstance().closeDatabase()
        }
    }

    /**
     * 插入或更新单条数据
     *
     * @param item
     */
    @Synchronized
    fun insertOrUpdate(item: T) {
        val tmpList = getList()
        //深拷贝数据
        val cloneList = YConvertUtils.deepClone(Gson().toJson(tmpList), getSubClass())

        try {
            val db = DBManager.getInstance().openDatabase()
            if (db.isOpen) {
                //db是否存在此数据
                var isExist = false
                for (position in 0 until tmpList.size) {
                    if (compareItem(item, tmpList[position])) {
                        isExist = true
                        //替换旧数据
                        cloneList[position] = item
                        break
                    }
                }
                if (isExist) {
                    updateItem(db, item)
                } else {
                    db.insert(tableName, null, getContentValues(item))
                    //加入拷贝集合
                    cloneList.add(item)
                }
                //加入缓存
                DBCacheManager.instance.dbCache.putList(tableName, cloneList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            DBManager.getInstance().closeDatabase()
        }
    }

    /**
     * 批量插入或更新
     */
    @Synchronized
    fun insertOrUpdate(dataList: List<T>) {
        val tmpList = getList()
        //深拷贝数据
        val cloneList = YConvertUtils.deepClone(Gson().toJson(tmpList), getSubClass())

        try {
            val db = DBManager.getInstance().openDatabase()
            if (db.isOpen) {
                db.beginTransaction() // 手动设置开始事务


                for (item in dataList) {

                    var isExist = false//db是否存在此数据

                    for (position in 0 until tmpList.size) {

                        if (compareItem(item, tmpList[position])) {
                            isExist = true
                            //替换旧数据
                            cloneList[position] = item
                            break
                        }
                    }
                    if (isExist) {
                        updateItem(db, item)
                    } else {
                        db.insert(tableName, null, getContentValues(item))
                        //加入拷贝集合
                        cloneList.add(item)
                    }
                }

                db.setTransactionSuccessful() // 设置事务处理成功，不设置会自动回滚不提交
                db.endTransaction() // 处理完成

                //加入缓存
                DBCacheManager.instance.dbCache.putList(tableName, cloneList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            DBManager.getInstance().closeDatabase()
        }
    }

    /**
     * 获取map集合
     *
     * @return
     */
    fun getMapInfo(key: String): Map<Any, T> {
        val map = HashMap<Any, T>()

        var cursor: Cursor? = null

        try {
            val db = DBManager.getInstance().openDatabase()
            cursor = db.query(tableName, null, null, null, null, null, null)

            if (db.isOpen) {
                while (cursor?.moveToNext() == true) {

                    val id = cursor.getString(getColumnIndex(cursor, key))
                    map[id] = getItemInfo(cursor)

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            DBManager.getInstance().closeDatabase()
        }
        return map

    }

    /**
     * 获取list集合
     * 注意：此方法需要查询数据库，不建议主线程使用
     *
     * @return
     */
    fun getList(): ArrayList<T> {
        val db = DBManager.getInstance().openDatabase()
        val cursor = db.query(tableName, null, null, null, null, null, null)
        return queryList(db, cursor)
    }

    /**
     * 插入缓存
     */
    private fun addCache(item: T) {
        var list = DBCacheManager.instance.dbCache.getList(tableName)
        if (null == list) {
            list = ArrayList<T>()
            list.add(item)
            DBCacheManager.instance.dbCache.putList(tableName, list)
        } else {
            list as ArrayList<T>
            list.add(item)
        }
    }


    /**
     * 获取list集合（内存缓存）
     * 注意：此方法因为强转ArrayList<T>比较耗时，不建议子线程或协程使用（子线程可能会阻塞，取消不了），推荐主线程使用
     */
    fun getListInfo(): ArrayList<T> {
        var list = DBCacheManager.instance.dbCache.getList(tableName)
        YLogUtils.i("db缓存", list?.size)
        if (list.isNullOrEmpty()) {
            list = getList()
            //加入内存缓存
            DBCacheManager.instance.dbCache.putList(tableName, list)
        }
        return list as ArrayList<T>
    }

    /**
     * 获取list集合（自定义db和cursor）
     *
     * @return
     */
    fun queryList(db: SQLiteDatabase, cursor: Cursor?): ArrayList<T> {

        val msgList = ArrayList<T>()
        try {
            if (db.isOpen) {
                while (cursor?.moveToNext() == true) {
                    msgList.add(getItemInfo(cursor))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            DBManager.getInstance().closeDatabase()
        }
        return msgList

    }

    /**
     * 获取item（自定义db和cursor）
     */
    fun queryItem(db: SQLiteDatabase, cursor: Cursor?): T? {

        var t: T? = null
        try {
            if (db.isOpen) {
                if (cursor?.moveToFirst() == true) {
                    t = getItemInfo(cursor)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            DBManager.getInstance().closeDatabase()
        }
        return t
    }


    /**
     * 删除所有信息
     *
     * @param db
     */
    fun deleteAll(db: SQLiteDatabase) {

        try {
            db.delete(tableName, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 删除所有信息
     */
    fun deleteAll() {

        try {
            val db = DBManager.getInstance().openDatabase()
            if (db.isOpen) {
                db.delete(tableName, null, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            DBManager.getInstance().closeDatabase()
        }

    }

    abstract fun getSubClass(): Class<T>

    /**
     * item转ContentValues
     *
     * @param item
     * @return
     */
    abstract fun getContentValues(item: T): ContentValues

    /**
     * 获取item
     *
     * @param cursor
     * @return
     */
    abstract fun getItemInfo(cursor: Cursor): T

    /**
     * 对比两个item是否相同
     *
     * @param item1
     * @param item2
     * @return
     */
    abstract fun compareItem(item1: T, item2: T): Boolean

    /**
     * 更新item
     *
     * @param db
     * @param item
     */
    abstract fun updateItem(db: SQLiteDatabase, item: T)


    fun getString(cursor: Cursor, name: String): String? {
        return cursor.getString(getColumnIndex(cursor, name))
    }

    fun getInt(cursor: Cursor, name: String): Int {
        return cursor.getInt(getColumnIndex(cursor, name))
    }

    fun getLong(cursor: Cursor, name: String): Long {
        return cursor.getLong(getColumnIndex(cursor, name))
    }

    fun getFloat(cursor: Cursor, name: String): Float {
        return cursor.getFloat(getColumnIndex(cursor, name))
    }

    fun getBool(cursor: Cursor, name: String): Boolean {
        return cursor.getInt(getColumnIndex(cursor, name)) == 1
    }

    fun getDouble(cursor: Cursor, name: String): Double {
        return cursor.getDouble(getColumnIndex(cursor, name))
    }

    private fun getColumnIndex(cursor: Cursor, name: String): Int {
        if (hashMap.containsKey(name)) {
            return hashMap[name] ?: 0
        }
        val index = cursor.getColumnIndex(name)
        hashMap[name] = index
        return index
    }
}