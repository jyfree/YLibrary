package com.jy.simple.workers

import android.content.Context
import android.os.Bundle
import com.jy.baselibrary.base.BaseActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.simple.R
import kotlinx.android.synthetic.main.simple_work_test_activity.*

/**
 * @description work示例
 * @date: 2020/4/17 16:28
 * @author: jy
 */
class WorkSimpleActivity : BaseActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, WorkSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_work_test_activity

    override fun initUI(savedInstanceState: Bundle?) {

        requestWork.setOnClickListener {
            WorkHelper.submitLog()
        }
    }
}