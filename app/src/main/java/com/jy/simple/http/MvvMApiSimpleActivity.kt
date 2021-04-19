package com.jy.simple.http

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.jy.baselibrary.base.mvvm.MvvMBaseActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.simple.R
import com.jy.simple.databinding.SimpleMvvmApiActivityBinding
import com.jy.simple.repository.BannerRepository
import com.jy.simple.viewmodel.MvvMViewModel
import com.jy.simple.viewmodel.MvvMViewModelFactory


/**

 * @Author Administrator
 * @Date 2019/11/1-15:48
 * @TODO
 */
class MvvMApiSimpleActivity : MvvMBaseActivity<SimpleMvvmApiActivityBinding>() {
    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, MvvMApiSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_mvvm_api_activity

    private val viewModel: MvvMViewModel by lazy {
        ViewModelProviders.of(this, MvvMViewModelFactory(BannerRepository()))
            .get(MvvMViewModel::class.java)
    }

    override fun initUI(savedInstanceState: Bundle?) {
        attachViewModel(viewModel)
        dataBinding.test = viewModel
    }


}