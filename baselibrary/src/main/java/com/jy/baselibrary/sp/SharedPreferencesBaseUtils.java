package com.jy.baselibrary.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;

import com.jy.baselibrary.utils.EncodeUtils;
import com.jy.baselibrary.utils.ParcelableUtils;

import java.util.Map;
import java.util.Set;


/**
 * Administrator
 * created at 2018/11/20 12:08
 * TODO:sp基类
 */
public abstract class SharedPreferencesBaseUtils {

    private SharedPreferences sharedPreferences;
    private SharedPreferencesHelper helper;

    SharedPreferencesBaseUtils(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(getSpName(), getMode());
        }
        helper = new SharedPreferencesHelper(sharedPreferences);
    }

    public void init() {
        helper.applyLoad();
    }

    public abstract String getSpName();

    public abstract int getMode();

    //*************************************************************************
    public boolean getBoolean(String keyName) {
        return getBoolean(keyName, false);
    }

    public boolean getBoolean(String keyName, boolean defValue) {
        if (helper.containsKey(keyName)) {
            return (boolean) helper.get(keyName);
        }
        return sharedPreferences.getBoolean(keyName, defValue);
    }

    public void setBoolean(String keyName, boolean value) {
        helper.saveDataAndSyncTask(keyName, value);
    }

    public int getInt(String keyName) {
        return getInt(keyName, 0);
    }

    public int getInt(String keyName, int defValue) {
        if (helper.containsKey(keyName)) {
            return (int) helper.get(keyName);
        }
        return sharedPreferences.getInt(keyName, defValue);
    }

    public void setInt(String keyName, int value) {
        helper.saveDataAndSyncTask(keyName, value);
    }


    public String getString(String keyName) {
        return getString(keyName, "");
    }

    public String getString(String keyName, String defValue) {
        if (helper.containsKey(keyName)) {
            return (String) helper.get(keyName);
        }
        return sharedPreferences.getString(keyName, defValue);
    }


    public void setString(String keyName, String value) {
        helper.saveDataAndSyncTask(keyName, value);
    }

    public long getLong(String keyName) {
        return getLong(keyName, 0);
    }

    public long getLong(String keyName, long defValue) {
        if (helper.containsKey(keyName)) {
            return (long) helper.get(keyName);
        }
        return sharedPreferences.getLong(keyName, defValue);
    }

    public void setLong(String keyName, long value) {
        helper.saveDataAndSyncTask(keyName, value);
    }


    public float getFloat(String keyName) {
        return getFloat(keyName, 0);
    }

    public float getFloat(String keyName, float defValue) {
        if (helper.containsKey(keyName)) {
            return (float) helper.get(keyName);
        }
        return sharedPreferences.getFloat(keyName, defValue);
    }

    public void setFloat(String keyName, float value) {
        helper.saveDataAndSyncTask(keyName, value);
    }


    public Set<String> getStringSet(String keyName) {
        if (helper.containsKey(keyName)) {
            return (Set<String>) helper.get(keyName);
        }
        return sharedPreferences.getStringSet(keyName, null);
    }

    public void setStringSet(String keyName, Set<String> value) {
        helper.saveDataAndSyncTask(keyName, value);
    }

    public <T extends Parcelable> T getObject(String keyName, Parcelable.Creator<T> creator) {
        return ParcelableUtils.unMarShall(getString(keyName), creator);
    }


    public void setObject(String keyName, Parcelable parcelable) {
        byte[] bytes = ParcelableUtils.marShall(parcelable);
        helper.saveDataAndSyncTask(keyName, EncodeUtils.base64Encode2String(bytes));
    }


    public void setHashMap(Map<String, Object> dataMap) {
        helper.saveDataAndSyncTaskOfMap(dataMap);
    }
}
