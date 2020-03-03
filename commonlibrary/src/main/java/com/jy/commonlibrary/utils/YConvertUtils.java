package com.jy.commonlibrary.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @Author Administrator
 * @Date 2019/10/30-13:49
 * @TODO 转换工具
 */
public class YConvertUtils {

    /**
     * 深拷贝数据
     *
     * @param data 对象（注意：需要指定类型，不能是泛型，否则转换对象失败）
     * @param type 转换类型
     * @param <T>
     * @return T
     */
    public static <T> T deepClone(final T data, final Type type) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(gson.toJson(data), type);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 深拷贝数据
     *
     * @param json  json元数据
     * @param clazz 转换class对象
     * @param <T>
     * @return ArrayList<T>
     */
    public static <T> ArrayList<T> deepClone(String json, Class<T> clazz) {
        Gson gson = new Gson();
        ArrayList<JsonObject> jsonObjects = gson.fromJson(json, new TypeToken<ArrayList<JsonObject>>() {
        }.getType());
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(gson.fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

}
