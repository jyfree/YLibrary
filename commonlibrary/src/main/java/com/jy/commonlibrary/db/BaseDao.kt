package com.jy.commonlibrary.db

import androidx.lifecycle.LifecycleOwner
import com.jy.baselibrary.thread.lifecycle.ThreadRequest
import com.jy.baselibrary.thread.lifecycle.ThreadResultCallback
import com.jy.commonlibrary.coroutine.CoroutineRequest
import com.jy.commonlibrary.coroutine.CoroutineResultCallback
import java.util.*

/**
 * @Author Administrator
 * @Date 2018/12/5-11:29
 * @TODO 数据库基类
 */
abstract class BaseDao<T> : BaseSuperDao<T>(), CoroutineRequest, ThreadRequest {


    //*******************************协程方式*********************************************

    /**
     * 插入单条数据
     */
    fun insertToCoroutine(item: T) {
        requestCoroutine { insert(item) }
    }

    /**
     * 批量插入（旧数据删除）
     */
    fun insertToCoroutine(dataList: ArrayList<T>) {
        requestCoroutine { insert(dataList) }
    }

    /**
     * 插入或更新单条数据
     */
    fun insertOrUpdateToCoroutine(item: T) {
        requestCoroutine { insertOrUpdate(item) }
    }

    /**
     * 批量插入或更新
     */
    fun insertOrUpdateToCoroutine(dataList: List<T>) {
        requestCoroutine { insertOrUpdate(dataList) }
    }

    /**
     * 获取map集合
     */
    fun getMapInfoToCoroutine(
        coroutineResultCallback: CoroutineResultCallback<Map<Any, T>>,
        key: String,
        lifecycleOwner: LifecycleOwner
    ) {
        requestCoroutine(coroutineResultCallback, lifecycleOwner) {
            getMapInfo(key)
        }
    }

    /**
     * 获取list集合
     */
    fun getListInfoToCoroutine(
        coroutineResultCallback: CoroutineResultCallback<ArrayList<T>>,
        lifecycleOwner: LifecycleOwner
    ) {
        requestCoroutine(coroutineResultCallback, lifecycleOwner) {
            getList()
        }
    }


    //********************************子线程方式*********************************************
    /**
     * 插入单条数据
     */
    fun insertToThread(item: T) {
        requestThread { insert(item) }
    }

    /**
     * 批量插入（旧数据删除）
     */
    fun insertToThread(dataList: ArrayList<T>) {
        requestThread { insert(dataList) }
    }


    /**
     * 插入或更新单条数据
     */
    fun insertOrUpdateToThread(item: T) {
        requestThread { insertOrUpdate(item) }
    }


    /**
     * 批量插入或更新
     */
    fun insertOrUpdateToThread(dataList: List<T>) {
        requestThread { insertOrUpdate(dataList) }
    }


    /**
     * 获取map集合
     */
    fun getMapInfoToThread(
        threadResultCallback: ThreadResultCallback<Map<Any, T>>,
        key: String,
        lifecycleOwner: LifecycleOwner
    ) {
        requestThread(threadResultCallback, lifecycleOwner) {
            getMapInfo(key)
        }
    }

    /**
     * 获取list集合
     */
    fun getListInfoToThread(
        threadResultCallback: ThreadResultCallback<ArrayList<T>>,
        lifecycleOwner: LifecycleOwner
    ) {
        requestThread(threadResultCallback, lifecycleOwner) {
            getList()
        }
    }


}
