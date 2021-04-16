package com.jy.simple.viewbinding

import android.content.Context
import android.os.Bundle
import com.jy.baselibrary.base.vb.BaseVBActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.simple.databinding.SimpleVbActivityBinding

/**
 * @description viewBinding示例
 * @date: 2021/4/15 17:29
 * @author: jy
 */
class TestVBActivity : BaseVBActivity<SimpleVbActivityBinding>() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, TestVBActivity::class.java)
        }
    }

    override fun initUI(savedInstanceState: Bundle?) {
        viewBinding.tvViewBinding.text = "测试viewBinding示例"
    }
}