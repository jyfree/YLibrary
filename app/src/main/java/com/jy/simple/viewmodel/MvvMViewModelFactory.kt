package com.jy.simple.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jy.simple.repository.BannerRepository

/**
 * @description SharedViewModel的工厂
 * @date: 2020/7/14 10:31
 * @author: jy
 */
class MvvMViewModelFactory(private val repository: BannerRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MvvMViewModel(repository) as T
    }
}
