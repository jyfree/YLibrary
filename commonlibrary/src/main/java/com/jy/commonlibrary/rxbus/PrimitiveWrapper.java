package com.jy.commonlibrary.rxbus;

/**
 * @Author Administrator
 * @Date 2019/9/9-14:30
 * @TODO 基础类型装箱
 * 基础类型包括：boolean、char、byte、short、int、long、float、double
 * 对应的转换：
 * byte → Byte
 * short → Short
 * int → Integer
 * long → Long
 * float → Float
 * double → Double
 * char → Character
 * boolean→ Boolean
 */
public class PrimitiveWrapper {

    /**
     * 装箱
     *
     * @param target 目标类
     * @return 转换后的Class
     */
    public static Class<?> wrapper(final Class<?> target) {
        if (boolean.class.equals(target)) {
            return Boolean.class;
        } else if (char.class.equals(target)) {
            return Character.class;
        } else if (byte.class.equals(target)) {
            return Byte.class;
        } else if (short.class.equals(target)) {
            return Short.class;
        } else if (int.class.equals(target)) {
            return Integer.class;
        } else if (long.class.equals(target)) {
            return Long.class;
        } else if (float.class.equals(target)) {
            return Float.class;
        } else if (double.class.equals(target)) {
            return Double.class;
        } else {
            return target;
        }
    }
}
