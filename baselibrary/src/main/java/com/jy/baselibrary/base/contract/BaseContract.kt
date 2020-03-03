package com.jy.baselibrary.base.contract

import com.trello.rxlifecycle2.LifecycleProvider

/**
 * @Author Administrator
 * @Date 2019/9/25-20:48
 * @TODO
 */
interface BaseContract {

    interface BasePresenter<V : BaseView, M : BaseModel> {

        /**
         * 挂载mode
         */
        fun attachMode(mode: M)

        /**
         * view挂载
         *
         * @param view
         */
        fun attachView(view: V)

        /**
         * View卸载
         */
        fun detachView()

    }

    interface BaseView {

        /**
         * 获取RxJava生命周期
         */
        val lifecycleProvider: LifecycleProvider<*>

        /**
         * 显示或隐藏进度中
         */
        fun showLoading(target: Any, flag: Boolean)

        /**
         * 显示PopupWindow类型弹窗
         */
        fun showPopWindowLoading(flag: Boolean)

    }

    interface BaseModel {

    }
}
