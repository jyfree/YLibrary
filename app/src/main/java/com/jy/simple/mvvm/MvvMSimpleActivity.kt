package com.jy.simple.mvvm

import android.content.Context
import android.os.Bundle
import com.jy.baselibrary.base.mvvm.MvvMBaseActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.ToastUtils
import com.jy.simple.R
import com.jy.simple.databinding.SimpleMvvmNomodelActivityBinding


/**

 * @Author Administrator
 * @Date 2019/11/1-11:26
 * @TODO mvvm示例
 */
class MvvMSimpleActivity : MvvMBaseActivity<SimpleMvvmNomodelActivityBinding>() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, MvvMSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_mvvm_nomodel_activity

    override fun initUI(savedInstanceState: Bundle?) {
        dataBinding.listener = this
    }

    fun onClickPosition(position: Int) {
        ToastUtils.showToast(position.toString())
    }
}