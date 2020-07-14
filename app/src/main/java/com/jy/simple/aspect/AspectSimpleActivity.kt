package com.jy.simple.aspect

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseAppCompatActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.aspect.CheckNetwork
import com.jy.commonlibrary.aspect.RunTimeTrace
import com.jy.commonlibrary.aspect.SingleClick
import com.jy.simple.R

/**

 * @Author Administrator
 * @Date 2019/11/8-11:55
 * @TODO
 */
class AspectSimpleActivity : BaseAppCompatActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, AspectSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_aspect_activity

    override fun initUI(savedInstanceState: Bundle?) {

    }


    @CheckNetwork
    fun onAspectNetwork(view: View) {
        YLogUtils.i("有网络连接")
    }

    @BehaviorTrace(name = "onClickBehavior", explain = "点击按钮")
    fun onUserBehavior(view: View) {
        YLogUtils.i("用户行为统计")
    }

    @RunTimeTrace
    @SingleClick
    fun onSingle(view: View) {
        YLogUtils.i("点击测试")
        testTime()
    }

    @RunTimeTrace
    fun testTime() {
        sleep(10)
    }

    private fun sleep(millis: Long) {
        Thread.sleep(millis)
    }
}