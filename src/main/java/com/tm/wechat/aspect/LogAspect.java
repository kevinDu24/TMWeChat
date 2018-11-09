package com.tm.wechat.aspect;

import com.tm.wechat.utils.commons.DateUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: TMWeChat
 * @description: Spring aop 拦截 springmvc 的 controller 请求方法，添加日志和统计方法执行时间
 * @author: ChengQC
 * @create: 2018-10-10 13:26
 **/
@Aspect
@Component
public class LogAspect {
    private final Logger logger = Logger.getLogger(LogAspect.class);

    // 定义方法执行的打印时间 ms
    private static final long ONE_TIME = 0;

    @Pointcut("execution(public * com.tm..controller..*.*(..))")
    public void myMethod(){};

    /**
     *  进入方法后打印日志
     * @param joinPoint
     */
    @Before("myMethod()")
    public void before(JoinPoint joinPoint) {
        logger.debug(this.getMethodName(joinPoint)+" start "+ DateUtils.dateToString(new Date()));
    }

    /**
     * 方法结束打印日志
     * @param joinPoint
     */
    @After("myMethod()")
    public void after(JoinPoint joinPoint) {
        logger.debug(this.getMethodName(joinPoint)+" after"+ DateUtils.dateToString(new Date()));
    }


    @Around("execution(* com.tm..controller..*.*(..))")
    public Object processLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 定义返回对象、得到方法需要的参数
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();

        try {
            obj = joinPoint.proceed(args);
        } catch (Throwable e) {
            logger.error("统计某方法执行耗时环绕通知出错", e);
        }

        // 获取执行的方法名
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();

        // 打印耗时的信息
        this.printExecTime(methodName, startTime, endTime);

        return obj;
    }

    /**
     * 打印方法执行耗时的信息，如果超过了一定的时间，才打印
     * @param methodName
     * @param startTime
     * @param endTime
     */
    private void printExecTime(String methodName, long startTime, long endTime) {
        long diffTime = endTime - startTime;
        if (diffTime > ONE_TIME) {
            logger.debug( "<<<<<<======"+ methodName + " 方法执行耗时：" + diffTime + " ms");
//            System.out.println("<<<<<<======"+ methodName + " 方法执行耗时：" + diffTime + " ms");
        }
    }

    /**
     * 获取方法名(类的详细包路径)
     * @param joinPoint
     * @return
     */
    private String getMethodName(JoinPoint joinPoint){
        return joinPoint.getSignature().getDeclaringTypeName() +
                "." + joinPoint.getSignature().getName();
    }

    /**
     * AfterReturning  拦截执行
     */
    @AfterReturning("execution(public * com.tm..controller..*.*(..))")
    public void AfterReturning() {
        logger.debug("method AfterReturning");
    }

    /**
     *  AfterThrowing 拦截执行
     */
    @AfterThrowing("execution(public * com.tm..controller..*.*(..))")
    public void AfterThrowing() {
        logger.debug("method AfterThrowing");
    }
}
