package com.jy.simple.selector

import android.content.Context
import android.os.Bundle
import com.jy.baselibrary.base.BaseAppCompatActivity
import com.jy.baselibrary.selector.selector.CompoundDrawableSelector
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.simple.R
import kotlinx.android.synthetic.main.simple_selector_activity.*


/**

 * @Author Administrator
 * @Date 2019/10/21-13:50
 * @TODO
 */
class SelectorSimpleActivity : BaseAppCompatActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, SelectorSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_selector_activity

    override fun initUI(savedInstanceState: Bundle?) {
        btn_Shadow.setShadow(R.color.colorAccent, R.color.colorPrimary)
        btn_BgColor.setBgColor(R.color.colorAccent, R.color.colorPrimary)
        tv_TextColor.setTextColor(R.color.colorAccent, R.color.colorPrimary)
        iv_BgDrawable.setBgDrawable(R.drawable.social_sdk_logo_qq, R.drawable.social_sdk_logo_qzone)
        iv_CDrawable.setCompoundDrawable(
            R.drawable.social_sdk_logo_qq,
            R.drawable.social_sdk_logo_qzone,
            CompoundDrawableSelector.TOP
        )
    }
}