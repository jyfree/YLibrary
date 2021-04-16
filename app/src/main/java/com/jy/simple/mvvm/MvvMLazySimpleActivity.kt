package com.jy.simple.mvvm

import android.content.Context
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
 */
class MvvMLazySimpleActivity : BaseFragmentActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, MvvMLazySimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_lazy_activity


    private val list = ArrayList<Fragment>()
    private val title = arrayOf("Android", "iOS")

    override fun initUI(savedInstanceState: Bundle?) {

        list.add(MvvMLazySimpleFragmentOne.newInstance())
        list.add(MvvMLazySimpleFragmentTwo.newInstance())

        for (element in title) {
            tab_Layout.addTab(tab_Layout.newTab().setText(element))
        }
        val adapter = MyFragmentPagerAdapter()
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = list.size

        tab_Layout.setupWithViewPager(viewPager)
        tab_Layout.setTabsFromPagerAdapter(adapter)

    }


    private inner class MyFragmentPagerAdapter :
        FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = list[position]

        override fun getCount(): Int = list.size

        override fun getPageTitle(position: Int): CharSequence? = title[position]
    }

}