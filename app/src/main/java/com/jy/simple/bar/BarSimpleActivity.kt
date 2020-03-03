package com.jy.simple.bar

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import com.jy.baselibrary.base.BaseActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.AdaptScreenUtils
import com.jy.baselibrary.utils.BarUtils
import com.jy.simple.Constants
import com.jy.simple.R
import kotlinx.android.synthetic.main.simple_bar_activity.*

/**

 * @Author Administrator
 * @Date 2019/9/28-15:34
 * @TODO bar示例
 */
class BarSimpleActivity : BaseActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, BarSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_bar_activity

    override fun initUI(savedInstanceState: Bundle?) {
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        BarUtils.addMarginTopEqualStatusBarHeight(lLayout)
    }

    override fun getResources(): Resources =
        AdaptScreenUtils.adaptHeight(super.getResources(), Constants.SYSTEM_DESIGN_HEIGHT)

    override fun onDestroy() {
        super.onDestroy()
        AdaptScreenUtils.closeAdapt(resources)
    }
}