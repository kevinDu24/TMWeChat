package com.tm.wechat.aspect;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by pengchao on 2017/9/21.
 */
//@Aspect
//@Component
public class HttpAspect {
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

//    @Pointcut("execution(public * com.tm.wechat.controller..*.*(..))")
//    public void log(){
//
//    }
//
//    @Before("log()")
//    public void doBefore(JoinPoint joinPoint){
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        Map<String, String[]> paramMap = request.getParameterMap();
//        Map<String, List<String>> map = getProcessedParamMap(request);
//        //url
//        logger.info("url={}", request.getRequestURL());
//        //method
//        logger.info("method={}", request.getMethod());
//        //ip
//        logger.info("ip={}", request.getRemoteAddr());
//        //类方法
//        logger.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        //参数
//        logger.info("args={}", joinPoint.getArgs());
//        logger.info("params{}", map);
//    }
//
//    @After("log()")
//    public void doAfter(){
//        logger.info("方法执行完毕!");
//    }
//
//
//    @AfterReturning(returning = "object", pointcut = "log()")
//    public void doAfterReturning(Object object){
//        if(object != null){
//            logger.info("response={}", object.toString());
//        }
//    }

    public static Map<String, List<String>> getProcessedParamMap(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, List<String>> processedParamMap = Maps.transformValues(paramMap, new Function<String[], List<String>>() {
            @Override
            public List<String> apply(String[] input) {
                return Lists.newArrayList(input);
            }
        });
        return processedParamMap;
    }
}
