package com.jy.sociallibrary.ext.data

import androidx.lifecycle.MutableLiveData

object StatusLiveData : MutableLiveData<StatusBean>() {

    fun getInstance(): StatusLiveData = Holder.INSTANCE

    private object Holder {
        val INSTANCE: StatusLiveData = StatusLiveData
    }
}