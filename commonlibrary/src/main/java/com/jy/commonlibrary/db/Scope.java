package com.jy.commonlibrary.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Administrator
 * @Date 2019/10/28-9:46
 * @TODO
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    /**
     * 是否为主键
     *
     * @return
     */
    boolean isPrimaryKey() default false;

    /**
     * 是否为自增字段
     *
     * @return
     */
    boolean isAutoKey() default false;

    /**
     * 是否为更新字段
     *
     * @return
     */
    boolean isUpdateField() default false;

    /**
     * 更新字段的版本
     *
     * @return
     */
    int updateFieldVersion() default 1;

    /**
     * 过滤字段（不需要创建的字段）
     *
     * @return
     */
    boolean isFilter() default false;

    /**
     * 对比字段（用于更新匹配）
     *
     * @return
     */
    boolean isCompareField() default false;
}
