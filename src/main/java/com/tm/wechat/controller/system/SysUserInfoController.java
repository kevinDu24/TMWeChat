package com.tm.wechat.controller.system;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.sysUser.AddUserFromCodeDto;
import com.tm.wechat.service.sysUser.SysUserService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by pengchao on 2018/4/14.
 */
@RestController
@RequestMapping("/userInfo")
public class SysUserInfoController {


    @Autowired
    SysUserService sysUserService;


    private static final Logger logger = LoggerFactory.getLogger(SysUserInfoController.class);


    /**
     *认证用户手机号
     * @param addUserFromCodeDto:
     * @return
     */
    @RequestMapping(value = "/authUserPhone", method = RequestMethod.POST)
    public ResponseEntity<Message> authUserPhone(@RequestBody AddUserFromCodeDto addUserFromCodeDto){
        try {
            return sysUserService.authUserPhone(addUserFromCodeDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("认证手机号异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     *认证用户身份
     * @param addUserFromCodeDto:
     * @return
     */
    @RequestMapping(value = "/authUserIdCard", method = RequestMethod.POST)
    public ResponseEntity<Message> authUserIdCard(@RequestBody AddUserFromCodeDto addUserFromCodeDto){
        try {
            return sysUserService.authUserIdCard(addUserFromCodeDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("认证身份证异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     *认证信息提交
     * @param addUserFromCodeDto:
     * @return
     */
    @RequestMapping(value = "/addUserInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addUserInfo(@RequestBody AddUserFromCodeDto addUserFromCodeDto){
        try {
            return sysUserService.addUserInfo(addUserFromCodeDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("认证信息提交异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     *用户认证状态查询
     * @param userName:
     * @return
     */
    @RequestMapping(value = "/getUserAuthState", method = RequestMethod.GET)
    public ResponseEntity<Message> getUserAuthState(String userName){
        try {
            return sysUserService.getUserAuthState(userName);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("认证信息提交异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     *用户登录权限查询
     * @param userCode:
     * @return
     */
    @RequestMapping(value = "/getLoginAuth", method = RequestMethod.GET)
    public ResponseEntity<Message> getLoginAuth(String userCode){
        try {
            return sysUserService.getLoginAuth(userCode);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取登录权限异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     *更新用户登录权限
     * @param userCode: 要更新的用户名
     * @return
     */
    @RequestMapping(value = "/updateLoginAuth", method = RequestMethod.POST)
    public ResponseEntity<Message> updateLoginAuth(String userCode, String loginAuth){
        try {
            return sysUserService.updateLoginAuth(userCode, loginAuth);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("更新登录权限异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


}
