package com.jy.simple.dialog

import android.content.Context
import android.view.Gravity
import com.jy.baselibrary.base.dialog.BaseDialog
import com.jy.baselibrary.utils.YUnitUtils
import com.jy.simple.R

/**

 * @Author Administrator
 * @Date 2019/9/28-12:02
 * @TODO
 */
class DialogSimple(context: Context, themeResId: Int) : BaseDialog(context, themeResId) {
    override fun getLayoutResId(): Int = R.layout.simple_dialog

    init {
        initCustomWidthAndHeightContentView(Gravity.CENTER, YUnitUtils.dp2px(context, 200f), YUnitUtils.dp2px(context, 200f))
    }
}