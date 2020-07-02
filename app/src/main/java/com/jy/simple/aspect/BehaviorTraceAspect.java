package com.jy.simple.aspect;

import com.jy.baselibrary.utils.YLogUtils;
import com.jy.commonlibrary.aspect.AspectUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


/**
 * @Author Administrator
 * @Date 2019/11/8-16:21
 * @TODO 用户行为统计切面
 */
@Aspect
public class BehaviorTraceAspect {

    /**
     * 找到处理的切点
     * * *(..)  可以处理BehaviorTrace这个类所有的方法
     */
    @Pointcut("execution(@com.jy.simple.aspect.BehaviorTrace  * *(..))")
    public void executionBehaviorTrace() {
    }

    /**
     * 处理切面
     *
     * @param joinPoint
     * @return
     */
    @Around("executionBehaviorTrace()")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        BehaviorTrace annotation = signature.getMethod().getAnnotation(BehaviorTrace.class);
        if (annotation != null) {
            String name = annotation.name();
            String explain = annotation.explain();
            long begin = System.currentTimeMillis();
            joinPoint.proceed();
            YLogUtils.INSTANCE.iFormatTag(AspectUtils.TAG, "用户行为统计--name：%s---explain：%s--time：%s(ms)", name, explain, System.currentTimeMillis() - begin);
            //TODO 此处可处理友盟统计
        }
        return null;
    }
}
