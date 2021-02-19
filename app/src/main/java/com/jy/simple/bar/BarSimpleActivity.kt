package com.jy.simple.bar

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.jy.baselibrary.base.BaseAppCompatActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.BarUtils
import com.jy.simple.R
import com.jy.simple.dialog.DialogSimple


/**

 * @Author Administrator
 * @Date 2019/9/28-15:34
 * @TODO bar示例
 */
class BarSimpleActivity : BaseAppCompatActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, BarSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_bar_activity

    override fun initUI(savedInstanceState: Bundle?) {
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
    }

    private var statusBar: View? = null

    fun onClickBar(view: View) {
        when (view.id) {
            R.id.btn_full -> {
                statusBar?.visibility = View.GONE
                BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
            }
            R.id.btn_white -> {
                statusBar?.visibility = View.GONE
                BarUtils.setStatusBarColor(this, Color.WHITE)
                BarUtils.setDarkMode(this)
            }
            R.id.btn_black -> {
                statusBar?.visibility = View.GONE
                BarUtils.setStatusBarColor(this, Color.parseColor("#9938e4"))
                BarUtils.setLightMode(this)
            }
            R.id.btn_gradual -> {
                if (statusBar == null) {
                    statusBar = BarUtils.getView(this, R.drawable.gradual_bg)
                    val contentView = findViewById<ViewGroup>(android.R.id.content)
                    contentView.addView(statusBar)
                }
                BarUtils.setStatusBarCustom(statusBar!!)
            }
            R.id.btn_dialog -> {
                DialogSimple(this, R.style.Theme_AppCompat_Dialog).show()
            }
        }
    }
}