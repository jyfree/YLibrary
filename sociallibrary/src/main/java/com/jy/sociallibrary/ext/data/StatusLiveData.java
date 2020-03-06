package com.jy.sociallibrary.ext.data;

import androidx.lifecycle.MutableLiveData;

public class StatusLiveData extends MutableLiveData<StatusBean> {
    private StatusLiveData() {
    }

    private static class Holder {
        public static final StatusLiveData INSTANCE = new StatusLiveData();
    }

    public static StatusLiveData getInstance() {
        return Holder.INSTANCE;
    }


}

