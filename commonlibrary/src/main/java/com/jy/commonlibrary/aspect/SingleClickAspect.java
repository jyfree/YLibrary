package com.jy.commonlibrary.aspect;

import android.view.View;

import com.jy.baselibrary.utils.YLogUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


/**
 * @Author Administrator
 * @Date 2019/11/8-17:06
 * @TODO 快速点击切面
 */
@Aspect
public class SingleClickAspect {

    @Pointcut("execution(@com.jy.commonlibrary.aspect.SingleClick  * *(..))")
    public void executionSingleClick() {
    }

    //环绕通知, 在目标执行中执行通知, 控制目标执行时机
    @Around("executionSingleClick()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        View view = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof View) {
                view = (View) arg;
                break;
            }
        }
        if (view == null) {
            return;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SingleClick annotation = signature.getMethod().getAnnotation(SingleClick.class);
        if (annotation != null) {
            if (!isFastDoubleClick(view, annotation.value())) {
                joinPoint.proceed();
            } else {
                YLogUtils.INSTANCE.iTag(AspectUtils.TAG, "你点击的太快了");
            }
        }
    }

    /**
     * 最近一次点击的时间
     */
    private long mLastClickTime;
    /**
     * 最近一次点击的控件ID
     */
    private int mLastClickViewId;

    /**
     * 是否是快速点击
     *
     * @param view           点击的控件
     * @param intervalMillis 时间间期（毫秒）
     * @return true:是，false:不是
     */
    private boolean isFastDoubleClick(View view, long intervalMillis) {
        int viewId = view.getId();
        long time = System.currentTimeMillis();
        long timeInterval = Math.abs(time - mLastClickTime);
        if (timeInterval < intervalMillis && viewId == mLastClickViewId) {
            return true;
        } else {
            mLastClickTime = time;
            mLastClickViewId = viewId;
            return false;
        }
    }
}
