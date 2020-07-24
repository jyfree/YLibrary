package com.jy.simple.sp

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseAppCompatActivity
import com.jy.baselibrary.sp.SharedPreferencesConfigUtils
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.simple.R


/**

 * @Author Administrator
 * @Date 2019/10/14-14:20
 * @TODO
 */
class SpSimpleActivity : BaseAppCompatActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, SpSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_sp_activity


    override fun initUI(savedInstanceState: Bundle?) {

    }

    fun onClickSp(view: View) {
        when (view.id) {
            R.id.sp_put -> putTest()
            R.id.sp_get -> getTest()
        }
    }

    private fun putTest() {
        val startTime = System.currentTimeMillis()
        val count = 10000
        for (i in 1..count) {
            SharedPreferencesConfigUtils.getInstance().setInt(i.toString(), i)
        }
        YLogUtils.eFormat("执行SP--put操作--%d次--耗时：%s ms", count, System.currentTimeMillis() - startTime)
    }

    private fun getTest() {
        val startTime = System.currentTimeMillis()
        val count = 10000
        for (i in 1..count) {
            SharedPreferencesConfigUtils.getInstance().getInt(i.toString())
        }
        YLogUtils.eFormat("执行SP--get操作--%d次--耗时：%s ms", count, System.currentTimeMillis() - startTime)
    }
}