package com.jy.baselibrary.base.vb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

object ViewBindingCreator {
    fun <VB : ViewBinding?> createViewBinding(target: Any, layoutInflater: LayoutInflater): VB {
        val entityClass =
            (target::class.java.genericSuperclass as ParameterizedType).actualTypeArguments.first()
        val declaredMethod =
            (entityClass as Class<VB>).getDeclaredMethod("inflate", LayoutInflater::class.java)
        return declaredMethod.invoke(target, layoutInflater) as VB
    }

    fun <VB : ViewBinding?> createViewBinding(
        target: Any,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB {
        val entityClass =
            (target::class.java.genericSuperclass as ParameterizedType).actualTypeArguments.first()
        val declaredMethod = (entityClass as Class<VB>).getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return declaredMethod.invoke(target, inflater, container, false) as VB
    }
}