package com.tm.wechat.controller;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.sysUser.SysUserFunctionResultDto;
import com.tm.wechat.dto.sysUser.UserAuthInfoDto;
import com.tm.wechat.service.sysUser.SysUserFunctionService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * Created by pengchao on 2018/4/23.
 */
@RestController
@RequestMapping("/userAuth")
public class SysUserFunctionController {

    @Autowired
    SysUserFunctionService sysUserFunctionService;

    private static final Logger logger = LoggerFactory.getLogger(SysUserFunctionController.class);

    @RequestMapping(value = "/getFunctionAuth", method = RequestMethod.GET)
    public ResponseEntity<Message> getLoginAuth(String userCode){
        try {
            return sysUserFunctionService.getRole(userCode);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取功能权限异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/updateFunctionAuth", method = RequestMethod.POST)
    public ResponseEntity<Message> updateFunctionAuth(String userCode, @RequestBody SysUserFunctionResultDto sysUserFunctionResultDto){
        try {
            return sysUserFunctionService.updateRole(userCode, sysUserFunctionResultDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("更新功能权限异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }
    /**
     * 查询车300评估权限接口
     * @param
     * @return
     */
    @RequestMapping(value = "/getUsedCarAnalysis1Power", method = RequestMethod.GET)
    public ResponseEntity<Message> getUsedCarAnalysis1Power(Principal user){
        try {
            return sysUserFunctionService.getUsedCarAnalysis1Power(user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取二手车评估功能权限异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 添加车300评估权限接口
     * @param userList
     * @return
     */
    @RequestMapping(value = "/addUsedCarAnalysis1Power", method = RequestMethod.POST)
    public ResponseEntity<Message> addUsedCarAnalysis1Power(Principal user, @RequestBody List<String> userList){
        try {
            return sysUserFunctionService.addUsedCarAnalysis1Power(userList, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("更新二手车评估功能权限异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 获取用户可选产品
     * @param userCode 选择的用户
     * @param user 登录的用户(主账号)
     * @return
     */
    @RequestMapping(value = "/enableUsers", method = RequestMethod.GET)
    public ResponseEntity<Message> getLoginAuth(String userCode, Principal user){
        try {
            return sysUserFunctionService.enableUsers(user.getName(), userCode);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取可选产品列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/updateUserProduct", method = RequestMethod.POST)
    public ResponseEntity<Message> updateFunctionAuth(String userCode, @RequestBody UserAuthInfoDto userAuthInfoDto){
        try {
            return sysUserFunctionService.updateUserProduct(userCode, userAuthInfoDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("更新用户可选产品列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }
}
