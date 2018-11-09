package com.tm.wechat.aspect;

import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.dao.LoginRecordRepository;
import com.tm.wechat.domain.LoginRecord;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Created by huzongcheng on 2017/4/6.
 */
//@Aspect
//@Component
public class WebRequestAspect {

    @Autowired
    private LoginRecordRepository loginRecordRepository;
//
//    // 定义切点Pointcut
//    @Pointcut("execution(public * com.tm.wechat.controller..*.*(..))")
//    public void excuteService() {
//    }
//
//    @Around("excuteService()")
//    public Object doAround(ProceedingJoinPoint joinPoint) {
//        ResponseEntity<Message> result = null;
//        try {
//            // 接收到请求，记录请求内容
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            HttpServletRequest request = attributes.getRequest();
//            String headerParam = request.getHeader("Header-Param");
//            //一些非app接口（如微信接口）没有headerParam，不拦截
//            if(null == headerParam){
//                result = (ResponseEntity<Message>) joinPoint.proceed();
//                return result;
//            }
//            String customer = JsonPath.from(headerParam).get("systemflag");
//            if ("yachi".equals(customer)) {
//                result = (ResponseEntity<Message>) joinPoint.proceed();
//                return result;
//            }
//            boolean loginCheckFlag = loginCheck(request);
//            if (loginCheckFlag) {   //将当前
//                Message message = new Message(MessageType.MSG_TYPE_ERROR, "9500");
//                return new ResponseEntity<Message>(message, HttpStatus.OK);
//            } else {
//                result = (ResponseEntity<Message>) joinPoint.proceed();
//                return result;
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return result;
//    }



//    @Around("excuteService()")
//    public Object doAround(ProceedingJoinPoint joinPoint) {
//        ResponseEntity<Message> result = null;
//        try {
//            result = (ResponseEntity<Message>) joinPoint.proceed();
//            return result;
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    /**
     * 用户登陆状态检查
     *
     * @return boolean true在其实设备中该用户处于登陆状态，否则，为非登陆状态
     * */
    private boolean loginCheck(HttpServletRequest request) {
        boolean flag = false;
        Principal user = request.getUserPrincipal();
        String headerParam = request.getHeader("Header-Param");
        String path = request.getServletPath();
        if (user == null || headerParam == null || "/user".equals(path) || "/tmLogout".equals(path)) {
            return flag;
        }
        String userName = user.getName();
        String customer = JsonPath.from(headerParam).get("systemflag");
        String channelId = JsonPath.from(headerParam).get("channelId");
        LoginRecord loginRecord = loginRecordRepository.findTop1ByUserNameAndCustomerAndDeviceIdOrderByTimeDesc(userName, customer, channelId);
        if (loginRecord != null && "0".equals(loginRecord.getEffectiveFlg())) {
            flag =  true;
        }
        return flag;
    }
}
