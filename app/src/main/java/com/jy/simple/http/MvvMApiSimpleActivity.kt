package com.jy.simple.http

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.jy.baselibrary.base.contract.BaseContract
import com.jy.baselibrary.base.mvvm.MvvMBaseActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.simple.BR
import com.jy.simple.R
import com.jy.simple.databinding.SimpleMvvmApiActivityBinding
import com.jy.simple.http.mvvm.MvvMApiSimpleModel
import com.jy.simple.http.mvvm.MvvMApiSimpleViewModel


/**

 * @Author Administrator
 * @Date 2019/11/1-15:48
 * @TODO
 */
class MvvMApiSimpleActivity : MvvMBaseActivity<MvvMApiSimpleViewModel, SimpleMvvmApiActivityBinding>() {
    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, MvvMApiSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_mvvm_api_activity

    override fun initViewModelClass(): Class<MvvMApiSimpleViewModel> = MvvMApiSimpleViewModel::class.java

    override fun initViewModelId(): Int = BR.test

    override fun initModel(): BaseContract.BaseModel = MvvMApiSimpleModel()

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.loading.observe(this, Observer { show ->
            show?.let { showPopWindowLoading(it) }
        })

    }

}