package com.jy.commonlibrary.aspect;

import android.content.Context;

import com.jy.baselibrary.utils.NetworkUtils;
import com.jy.baselibrary.utils.ToastUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


/**
 * @Author Administrator
 * @Date 2019/11/8-11:25
 * @TODO 判断是否有网络切面
 */
@Aspect
public class CheckNetworkAspect {

    /**
     * 找到处理的切点
     * * *(..)  “**”表示是任意包名   “..”表示任意类型任意多个参数
     */
    @Pointcut("execution(@com.jy.commonlibrary.aspect.CheckNetwork  * *(..))")
    public void executionCheckNetwork() {
    }

    /**
     * 处理切面
     * 注：
     * Around 环绕通知, 在目标执行中执行通知, 控制目标执行时机
     * Before 前置通知, 在目标执行之前执行通知
     * After  后置通知, 目标执行后执行通知
     * AfterReturning 后置返回通知, 目标返回时执行通知
     * AfterThrowing 异常通知, 目标抛出异常时执行通知
     */
    @Around("executionCheckNetwork()")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        Context context = AspectUtils.getContext(joinPoint.getThis());
        if (NetworkUtils.isNetworkConnected()) {
            return joinPoint.proceed();
        } else {
            ToastUtils.showToast(context, "没有网络连接");
            return null;
        }

    }


}
