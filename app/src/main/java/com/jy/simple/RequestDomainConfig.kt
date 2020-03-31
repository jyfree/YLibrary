package com.jy.simple


/**
 * Administrator
 * created at 2018/7/31 9:55
 * TODO:域名配置
 */
object RequestDomainConfig {
    private val isTestService = BuildConfig.DEBUG

    fun getBaseUrl(): String {
        return if (isTestService) {
            "http://testservice.wowolive99.com/cmd/"
        } else {
            "http://service.wowolive99.com/cmd/"
        }
    }
}
