package com.jy.baselibrary.base.presenter

import com.jy.baselibrary.base.contract.BaseContract


/**
 * @Author Administrator
 * @Date 2019/9/25-20:51
 * @TODO
 */
open class BasePresenter<V : BaseContract.BaseView, M : BaseContract.BaseModel> :
    BaseContract.BasePresenter<V, M> {

    var mView: V? = null
    lateinit var mMode: M

    override fun attachMode(mode: M) {
        mMode = mode
    }

    override fun attachView(view: V) {
        mView = view
    }

    override fun detachView() {
        if (mView != null) {
            mView = null
        }

    }
}
