package com.jy.baselibrary.base.contract

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**
 * Administrator
 * created at 2018/11/19 15:15
 * TODO:多个fragment切换接口
 */
class FragmentMultiContract {

    interface View : BaseContract.BaseView {

        val isMFinishing: Boolean

        var currentTabIndex: Int

        val fragmentId: Int

        val mFragmentManager: FragmentManager

        var showingFragment: Fragment?

        val tabs: Array<android.view.View>?

        val fragments: Array<Fragment>

    }

    interface Presenter<V : BaseContract.BaseView, M : BaseContract.BaseModel> : BaseContract.BasePresenter<V, M> {
        fun gotoFragment(position: Int)
    }

    interface Model : BaseContract.BaseModel {

    }

}
