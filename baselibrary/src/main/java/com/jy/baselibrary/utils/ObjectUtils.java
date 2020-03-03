package com.jy.baselibrary.utils;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.widget.ProgressBar;

import java.lang.reflect.Method;
import java.util.Collection;

public class ObjectUtils {
    /**
     * 根据属性名获取该类此属性的值
     */
    public static Object getValueByFieldName(String fieldName, Object object) {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        try {
            Method method = object.getClass().getMethod(getter);
            return method.invoke(object);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressLint("PrivateApi")
    public static Drawable getProgressDrawable(String methodName, Object o, Object[] paras) {
        Drawable newDrawable = null;
        try {
            Class<?> c[] = new Class[2];
            c[0] = Drawable.class;
            c[1] = boolean.class;
            Method method = ProgressBar.class.getDeclaredMethod(methodName, c);
            method.setAccessible(true);
            newDrawable = (Drawable) method.invoke(o, paras);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newDrawable;
    }

    /**
     * 利用反射实现对象之间属性复制
     */
    public static void copyProperties(Object from, Object to) throws Exception {
        copyPropertiesExclude(from, to);
    }

    /**
     * 复制对象属性
     */
    @SuppressWarnings("unchecked")
    private static void copyPropertiesExclude(Object from, Object to) throws Exception {
        Method[] fromMethods = from.getClass().getDeclaredMethods();
        Method[] toMethods = to.getClass().getDeclaredMethods();
        Method fromMethod, toMethod;
        String fromMethodName, toMethodName;
        for (Method fromMethod1 : fromMethods) {
            fromMethod = fromMethod1;
            fromMethodName = fromMethod.getName();
            if (!fromMethodName.contains("get") || fromMethodName.contains("getId"))
                continue;
            //排除列表检测
            toMethodName = "set" + fromMethodName.substring(3);
            toMethod = findMethodByName(toMethods, toMethodName);
            if (toMethod == null)
                continue;
            Object value = fromMethod.invoke(from);
            if (value == null)
                continue;
            //集合类判空处理
            if (value instanceof Collection) {
                Collection newValue = (Collection) value;
                if (newValue.size() <= 0)
                    continue;
            }
            toMethod.invoke(to, value);
        }
    }

    /**
     * 从方法数组中获取指定名称的方法
     */
    private static Method findMethodByName(Method[] methods, String name) {
        for (Method method : methods) {
            if (method.getName().equals(name))
                return method;
        }
        return null;
    }
}
