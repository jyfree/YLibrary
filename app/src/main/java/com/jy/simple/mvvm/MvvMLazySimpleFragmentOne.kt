package com.jy.simple.mvvm


import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.jy.baselibrary.base.mvvm.MvvMBaseLazyFragment
import com.jy.baselibrary.utils.YLogUtils
import com.jy.simple.R
import com.jy.simple.databinding.SimpleMvvmApiFragmentBinding
import com.jy.simple.repository.BannerRepository
import com.jy.simple.viewmodel.MvvMViewModel
import com.jy.simple.viewmodel.MvvMViewModelFactory


/**

 * @Author Administrator
 * @Date 2019/10/31-13:37
 * @TODO
 */
class MvvMLazySimpleFragmentOne : MvvMBaseLazyFragment<SimpleMvvmApiFragmentBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(): MvvMLazySimpleFragmentOne {
            return MvvMLazySimpleFragmentOne()
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_mvvm_api_fragment

    private val viewModel: MvvMViewModel by lazy {
        ViewModelProviders.of(activity!!, MvvMViewModelFactory(BannerRepository()))
            .get(MvvMViewModel::class.java)
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        attachViewModel(viewModel)
        dataBinding.sharedViewModel = viewModel
    }

    override fun lazyLoad() {
        YLogUtils.i("lazyLoad：${javaClass.simpleName}")
    }

    override fun visibleToUser() {
        YLogUtils.i("visibleToUser：${javaClass.simpleName}")
    }


}