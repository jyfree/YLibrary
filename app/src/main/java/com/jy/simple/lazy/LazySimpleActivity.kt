package com.jy.simple.lazy

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.jy.baselibrary.base.BaseFragmentActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.simple.R
import kotlinx.android.synthetic.main.simple_lazy_activity.*
import java.util.*


/**

 * @Author Administrator
 * @Date 2019/10/31-11:24
 * @TODO
 * 横竖屏切换不销毁activity方法：
 * 1、重写onConfigurationChanged
 * 2、manifest添加属性android:configChanges="keyboardHidden|orientation|screenSize"
 */
class LazySimpleActivity : BaseFragmentActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, LazySimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_lazy_activity


    private val list = ArrayList<Fragment>()
    private val title = arrayOf("Android", "iOS", "web", "winPhone")

    override fun initUI(savedInstanceState: Bundle?) {

        for (i in title.indices) {
            list.add(LazySimpleFragment.newInstance(title[i]))
            tab_Layout.addTab(tab_Layout.newTab().setText(title[i]))
        }
        val adapter = MyFragmentPagerAdapter()
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = list.size

        tab_Layout.setupWithViewPager(viewPager)
        tab_Layout.setTabsFromPagerAdapter(adapter)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // 检测屏幕的方向：纵向或横向
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //TODO 当前为横屏， 在此处添加额外的处理代码
        } else if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //TODO 当前为竖屏， 在此处添加额外的处理代码
        }
        //检测实体键盘的状态：推出或者合上
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            //TODO 实体键盘处于推出状态，在此处添加额外的处理代码
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            //TODO 实体键盘处于合上状态，在此处添加额外的处理代码
        }
    }


    private inner class MyFragmentPagerAdapter :
        FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = list[position]

        override fun getCount(): Int = list.size

        override fun getPageTitle(position: Int): CharSequence? = title[position]
    }

}