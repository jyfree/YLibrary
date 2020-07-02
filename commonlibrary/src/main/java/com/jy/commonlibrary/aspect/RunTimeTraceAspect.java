package com.jy.commonlibrary.aspect;

import com.jy.baselibrary.utils.YLogUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.HashMap;


/**
 * @Author Administrator
 * @Date 2019/11/11-10:47
 * @TODO 性能监控切面
 */
@Aspect
public class RunTimeTraceAspect {

    //存储页面停留时间
    private HashMap<Object, Long> remainMap = new HashMap<>();

    //统计某个方法执行时长
    private static final String POINTCUT_METHOD =
            "execution(@com.jy.commonlibrary.aspect.RunTimeTrace * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.jy.commonlibrary.aspect.RunTimeTrace *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedRunTimeTrace() {
    }

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedRunTimeTrace() {
    }

    //***************************activity相关****************************************
    @Pointcut("execution(* *..Activity+.onCreate(..))")
    public void onCreate() {
    }

    @Pointcut("execution(* *..Activity+.onStart(..))")
    public void onStart() {
    }

    @Pointcut("execution(* *..Activity+.onStop(..))")
    public void onStop() {
    }

    @Pointcut("execution(* *..Activity+.onDestroy(..))")
    public void onDestroy() {
    }

    /**
     * Pointcut 选择出的切入点 P 的控制流中的所有 Join Point，不包括 P 本身
     * 即onCreate为切入点，不包括onCreate
     */
    @Pointcut("onCreate() && !cflowbelow(onCreate())")
    public void realOnCreate() {
    }

    @Pointcut("onStart() && !cflowbelow(onStart())")
    public void realOnStart() {
    }

    @Pointcut("onStop() && !cflowbelow(onStop())")
    public void realOnStop() {
    }

    @Pointcut("onDestroy() && !cflowbelow(onDestroy())")
    public void realOnDestroy() {
    }

    //***************************fragment相关****************************************
    @Pointcut("execution(* *..Fragment+.onCreate(..))")
    public void onCreateFragment() {
    }

    @Pointcut("execution(* *..Fragment+.onViewCreated(..))")
    public void onViewCreatedFragment() {
    }


    @Pointcut("onCreateFragment() && !cflowbelow(onCreateFragment())")
    public void realOnCreateFragment() {
    }

    @Pointcut("onViewCreatedFragment() && !cflowbelow(onViewCreatedFragment())")
    public void realOnViewCreatedFragment() {
    }


    @Around("methodAnnotatedRunTimeTrace() || constructorAnnotatedRunTimeTrace() || realOnCreate()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取方法信息对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取当前对象
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = methodSignature.getName();

        long startTime = System.currentTimeMillis();
        //调用原方法的执行。
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        YLogUtils.INSTANCE.iFormatTag(AspectUtils.TAG, "性能监控--》执行方法耗时--%s--%s--%s(ms)", className, methodName, endTime - startTime);
        return result;
    }

    @Around("realOnStart()")
    public Object startJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        if (target != null) {
            remainMap.put(target, System.currentTimeMillis());
        }
        return joinPoint.proceed();
    }

    @Around("realOnStop()")
    public Object stopJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        String className = target.getClass().getSimpleName();
        Long startTime = remainMap.get(target);
        if (startTime == null) {
            startTime = 0L;
        }
        Object result = joinPoint.proceed();
        YLogUtils.INSTANCE.iFormatTag(AspectUtils.TAG, "性能监控--》停留时间--%s--%s(ms)", className, System.currentTimeMillis() - startTime);
        return result;
    }

    @Around("realOnDestroy()")
    public void destroyJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取当前对象
        String className = joinPoint.getTarget().getClass().getSimpleName();
        joinPoint.proceed();
        YLogUtils.INSTANCE.iFormatTag(AspectUtils.TAG, "性能监控--》页面销毁--%s", className);
    }


    //****************************fragment**********************************************
    @Around("realOnCreateFragment()")
    public Object onStartFragmentJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        if (target != null) {
            remainMap.put(target, System.currentTimeMillis());
        }
        return joinPoint.proceed();
    }

    @Around("realOnViewCreatedFragment()")
    public Object onViewCreatedFragmentJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        String className = target.getClass().getSimpleName();
        Long startTime = remainMap.get(target);
        if (startTime == null) {
            startTime = 0L;
        }
        Object result = joinPoint.proceed();
        YLogUtils.INSTANCE.iFormatTag(AspectUtils.TAG, "性能监控--》fragment创建耗时--%s--%s(ms)", className, System.currentTimeMillis() - startTime);
        return result;
    }

}
