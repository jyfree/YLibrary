package com.jy.baselibrary.base.broker


/**
 * @Author Administrator
 * @Date 2019/9/25-20:51
 * @TODO
 */
open class BasePresenter<V : BaseContract.BaseView, VM : BaseViewModel> :
    BaseContract.BasePresenter<V, VM> {

    var mView: V? = null
    lateinit var mViewModel: VM


    override fun attachViewModel(vm: VM) {
        mViewModel = vm
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
