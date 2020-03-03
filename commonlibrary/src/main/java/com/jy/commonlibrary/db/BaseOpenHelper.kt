package com.jy.commonlibrary.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.jy.baselibrary.utils.YLogUtils


/**
 * Administrator
 * created at 2015/12/8 14:16
 * TODO:数据库辅助类
 *
 *
 * 注意：如果数据库表有int|double类型的字段。
 *
 *
 * 插入数据时，该类型字段不能为null，否则不能插入数据。
 *
 *
 * 若为null，则不能设置事务，否则会导致db死锁
 */
abstract class BaseOpenHelper(context: Context, dbName: String, version: Int) :
    SQLiteOpenHelper(context, dbName, null, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DownloadDao.DOWNLOAD_TABLE_CREATE)
        onCreateDB(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgradeDB(db, oldVersion, newVersion)
    }

    abstract fun onCreateDB(db: SQLiteDatabase)

    abstract fun onUpgradeDB(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)

    /**
     * 新增表字段
     * 注意：更新的字段必需添加注解：@Scope(isUpdateField = true, updateFieldVersion = xxx)
     * 否则更新失败
     *
     * @param db         db
     * @param oldVersion 旧版本号
     * @param subClass   需要更新的目标
     */
    fun addField(db: SQLiteDatabase, oldVersion: Int, subClass: Class<*>) {
        execUpgradeSQL(db, *DBFieldManager.addField(subClass, oldVersion).toTypedArray())
    }

    /**
     * 更新版本
     * 示例：String sql = "alter table [" + DownloadDao.TABLE_NAME + "] add " + "test" + " INTEGER";
     *
     * @param db
     * @param sqlArray 数据库语句集合
     */
    fun execUpgradeSQL(db: SQLiteDatabase, vararg sqlArray: String) {
        if (sqlArray.isEmpty()) {
            YLogUtils.e(TAG, "更新db语句错误，请检查版本号、db实体")
            return
        }
        db.beginTransaction()
        try {
            for (sql in sqlArray) {
                db.execSQL(sql)
            }
            db.setTransactionSuccessful()
        } catch (ex: Throwable) {
            ex.printStackTrace()
            YLogUtils.e(TAG, "更新db失败", sqlArray)
        } finally {
            db.endTransaction()
        }
    }

    companion object {

        private val TAG = BaseOpenHelper::class.java.simpleName
    }

}
