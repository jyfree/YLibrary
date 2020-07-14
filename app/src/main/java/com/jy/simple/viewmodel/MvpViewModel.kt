package com.jy.simple.viewmodel

import com.jy.commonlibrary.broker.OkViewModel
import com.jy.simple.repository.BannerRepository
import com.jy.simple.repository.UserRepository

class MvpViewModel constructor(
    val bannerRepository: BannerRepository,
    val userRepository: UserRepository
) : OkViewModel() {

}