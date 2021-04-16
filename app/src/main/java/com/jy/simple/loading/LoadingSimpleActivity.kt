package com.jy.simple.loading

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YHandlerUtils
import com.jy.simple.R
import kotlinx.android.synthetic.main.simple_loading_activity.*

/**
 * @Author Administrator
 * @Date 2019/9/26-11:38
 * @TODO
 */
class LoadingSimpleActivity : BaseActivity() {

    companion object {

        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, LoadingSimpleActivity::class.java)
        }
    }


    override fun initLayoutID(): Int = R.layout.simple_loading_activity

    override fun initUI(savedInstanceState: Bundle?) {

    }

    fun stopLoading(view: View) {
        when (view.id) {
            R.id.showDefaultLoading -> {
                setBtnEnabled(false)
                showLoading(tv_target, true)
                YHandlerUtils.runOnUiThread(Runnable {
                    showLoading(tv_target, false)
                    setBtnEnabled(true)
                }, 5)
            }
            R.id.showPopWindowsLoading -> {

                setBtnEnabled(false)
                showPopWindowLoading(true)
                YHandlerUtils.runOnUiThread(Runnable {
                    showPopWindowLoading(false)
                    setBtnEnabled(true)
                }, 5)
            }
        }
    }

    private fun setBtnEnabled(isEnabled: Boolean) {
        showDefaultLoading.isEnabled = isEnabled
        showPopWindowsLoading.isEnabled = isEnabled
    }

}
