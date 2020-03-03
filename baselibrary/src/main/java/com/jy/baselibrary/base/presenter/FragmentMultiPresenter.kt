package com.jy.baselibrary.base.presenter

import com.jy.baselibrary.base.contract.FragmentMultiContract
import com.jy.baselibrary.utils.YLogUtils


/**
 * Administrator
 * created at 2018/11/19 15:16
 * TODO:多个Fragment切换Presenter
 */
class FragmentMultiPresenter<V : FragmentMultiContract.View, W : FragmentMultiContract.Model> :
    BasePresenter<V, W>(), FragmentMultiContract.Presenter<V, W> {


    override fun gotoFragment(position: Int) {
        try {
            mView?.let {
                val fragments = it.fragments
                if (it.currentTabIndex != position) {
                    val trx = it.mFragmentManager.beginTransaction()
                    if (!fragments[position].isAdded && null == it.mFragmentManager.findFragmentByTag(
                            position.toString()
                        )
                    ) {
                        if (it.showingFragment != null) {
                            trx.hide(fragments[it.currentTabIndex])
                                .add(it.fragmentId, fragments[position], position.toString())
                        } else {
                            trx.add(it.fragmentId, fragments[position])
                        }
                    } else {
                        if (it.showingFragment != null) {
                            trx.hide(fragments[it.currentTabIndex]).show(fragments[position])
                        } else {
                            trx.show(fragments[position])
                        }
                    }
                    it.showingFragment = fragments[position]
                    if (!it.isMFinishing) {
                        trx.commitAllowingStateLoss()
                        it.mFragmentManager.executePendingTransactions()
                    }
                }
                if (it.tabs != null) {
                    it.tabs!![it.currentTabIndex].isSelected = false
                    it.tabs!![position].isSelected = true
                }
                it.currentTabIndex = position
            }
        } catch (e: Exception) {
            YLogUtils.e(e.message)
        }

    }
}
