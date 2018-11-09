package com.tm.wechat.aspect;


import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.SystemService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.apache.commons.codec.binary.Base64;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by pengchao on 2018/3/30.
 */
//@Aspect
//@Component
public class AppRequestAspect {

    @Autowired
    private SystemService systemService;

    // 定义切点Pointcut
    @Pointcut("execution(public * com.tm.wechat.controller..*.*(..))")
    public void excuteService() {
    }

    @Around("excuteService()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            // 获取消息头中的设备标识
            String deviceToken = request.getHeader("deviceToken");
            if(CommonUtils.isNull(deviceToken)){
                return joinPoint.proceed();
            }
            String authCode = request.getHeader("Authorization");
            if(CommonUtils.isNull(authCode)){
                return joinPoint.proceed();
            }
            String auth = new String(Base64.decodeBase64(authCode.split(" ")[1].replace("==","").getBytes("utf-8")), "utf-8");
            // 用户名
            String userName = auth.split(":")[0];
            String path = request.getServletPath();
            if("/user".equals(path) || "/maps".equals(path) ||  path.contains("/baiduMap" )||
                    path.contains("/download") || path.contains("excel")
                    || path.contains("/tenure") || path.contains("/informations") || path.contains("/files") || path.contains("/push")){
                return joinPoint.proceed();
            }
            // 验证有效性
            ResponseEntity<Message> verifyResult = systemService.verify(deviceToken, userName);
            if(CommonUtils.errorCode.equals(verifyResult.getBody().getError())){
                return verifyResult;
            } else {
                return joinPoint.proceed();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Message message = new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo);
            return new ResponseEntity<Message>(message, HttpStatus.OK);
        }
    }

}
