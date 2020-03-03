package com.jy.simple.lazy

import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseLazyFragment
import com.jy.baselibrary.utils.YLogUtils
import com.jy.simple.R
import kotlinx.android.synthetic.main.simple_lazy_fragment.*

/**

 * @Author Administrator
 * @Date 2019/10/31-13:37
 * @TODO 懒加载--不保存状态
 */
class LazySimpleFragment : BaseLazyFragment() {

    companion object {
        @JvmStatic
        fun newInstance(msg: String): LazySimpleFragment {
            val lazyFragment = LazySimpleFragment()
            val bundle = Bundle()
            bundle.putString("msg", msg)
            lazyFragment.arguments = bundle
            return lazyFragment
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_lazy_fragment

    private var msg: String? = null

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        msg = arguments?.getString("msg") ?: ""
        textView.text = msg
    }

    override fun lazyLoad() {
        YLogUtils.i("lazyLoad：$msg")
    }

    override fun visibleToUser() {
        YLogUtils.i("visibleToUser：$msg")
    }

}