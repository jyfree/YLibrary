package com.jy.commonlibrary.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.jy.commonlibrary.http.bean.DownInfo

object DownloadDao : BaseDao<DownInfo>() {

    const val TABLE_NAME = "download"

    const val COLUMN_NAME_PRIMARY_ID = "id"//主键
    const val COLUMN_NAME_SAVE_PATH = "savePath"//存储位置
    const val COLUMN_NAME_COUNT_LENGTH = "countLength"//文件总长度
    const val COLUMN_NAME_READ_LENGTH = "readLength"//下载长度
    const val COLUMN_NAME_CONNECTION_TIME = "connectionTime"//超时设置
    const val COLUMN_NAME_STATE_INT = "stateInt"//state状态数据库保存
    const val COLUMN_NAME_URL = "url"//url
    const val COLUMN_NAME_UPDATE_PROGRESS = "updateProgress"//是否需要实时更新下载进度,避免线程的多次切换

    /**
     * 下载信息表
     */
    const val DOWNLOAD_TABLE_CREATE = ("CREATE TABLE "
            + TABLE_NAME + " ("
            + COLUMN_NAME_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME_SAVE_PATH + " TEXT, "
            + COLUMN_NAME_COUNT_LENGTH + " INTEGER, "
            + COLUMN_NAME_READ_LENGTH + " INTEGER, "
            + COLUMN_NAME_CONNECTION_TIME + " INTEGER, "
            + COLUMN_NAME_STATE_INT + " INTEGER, "
            + COLUMN_NAME_URL + " TEXT, "
            + COLUMN_NAME_UPDATE_PROGRESS + " INTEGER); ")


    override val tableName: String
        get() = TABLE_NAME

    override fun getSubClass(): Class<DownInfo> = DownInfo::class.java

    override fun getContentValues(item: DownInfo): ContentValues {
        val values = ContentValues()
        values.put(COLUMN_NAME_SAVE_PATH, item.savePath)
        values.put(COLUMN_NAME_COUNT_LENGTH, item.countLength)
        values.put(COLUMN_NAME_READ_LENGTH, item.readLength)
        values.put(COLUMN_NAME_CONNECTION_TIME, item.connectionTime)
        values.put(COLUMN_NAME_STATE_INT, item.stateInt)
        values.put(COLUMN_NAME_URL, item.url)
        values.put(COLUMN_NAME_UPDATE_PROGRESS, item.updateProgress)
        return values
    }

    override fun getItemInfo(cursor: Cursor): DownInfo = DownInfo(
        getString(cursor, COLUMN_NAME_SAVE_PATH),
        getLong(cursor, COLUMN_NAME_COUNT_LENGTH),
        getLong(cursor, COLUMN_NAME_READ_LENGTH),
        getInt(cursor, COLUMN_NAME_CONNECTION_TIME),
        getInt(cursor, COLUMN_NAME_STATE_INT),
        getString(cursor, COLUMN_NAME_URL),
        getBool(cursor, COLUMN_NAME_UPDATE_PROGRESS)
    )

    override fun compareItem(item1: DownInfo, item2: DownInfo): Boolean = item1.url == item2.url

    override fun updateItem(db: SQLiteDatabase, item: DownInfo) {
        db.update(tableName, getContentValues(item), "$COLUMN_NAME_URL = ?", arrayOf(item.url))
    }

    fun queryDownloadInfoByPath(path: String): DownInfo? {

        val db = DBManager.getInstance().openDatabase()
        val cursor =
            db.query(TABLE_NAME, null, "$COLUMN_NAME_URL = ?", arrayOf(path), null, null, null)

        return queryItem(db, cursor)
    }
}