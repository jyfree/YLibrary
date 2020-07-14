package com.jy.simple.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jy.simple.repository.BannerRepository
import com.jy.simple.repository.UserRepository

/**
 * @description MvpViewModel的工厂
 * @date: 2020/7/14 10:31
 * @author: jy
 */
class MvpViewModelFactory(
    private val bannerRepository: BannerRepository,
    private val userRepository: UserRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MvpViewModel(bannerRepository, userRepository) as T
    }
}
