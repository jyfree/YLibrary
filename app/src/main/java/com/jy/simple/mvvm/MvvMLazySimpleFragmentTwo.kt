package com.jy.simple.mvvm

import android.os.Bundle
import com.jy.baselibrary.base.contract.BaseContract
import com.jy.baselibrary.base.mvvm.MvvMBaseLazyFragment
import com.jy.simple.BR
import com.jy.simple.R
import com.jy.simple.databinding.SimpleMvvmFragmentBinding

/**

 * @Author Administrator
 * @Date 2019/11/5-9:51
 * @TODO
 */
class MvvMLazySimpleFragmentTwo : MvvMBaseLazyFragment<SharedViewModel, SimpleMvvmFragmentBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(): MvvMLazySimpleFragmentTwo {
            return MvvMLazySimpleFragmentTwo()
        }
    }


    override fun initLayoutID(): Int = R.layout.simple_mvvm_fragment


    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initViewModelClass(): Class<SharedViewModel> = SharedViewModel::class.java

    override fun initViewModelId(): Int = BR.sharedViewModel

    override fun initModel(): BaseContract.BaseModel = SharedModel()

    override fun isShareData(): Boolean = true

    override fun lazyLoad() {
    }

    override fun visibleToUser() {
    }
}