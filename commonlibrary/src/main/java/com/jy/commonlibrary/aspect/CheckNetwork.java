package com.jy.commonlibrary.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Administrator
 * @Date 2019/11/8-11:26
 * @TODO 检查网络注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface CheckNetwork {
}