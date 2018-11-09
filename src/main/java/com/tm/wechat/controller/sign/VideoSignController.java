package com.tm.wechat.controller.sign;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.sign.VideoSignAccountDto;
import com.tm.wechat.service.sign.VideoSignService;
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

import java.security.Principal;

/**
 * 视频面签
 * Created by pengchao on 2018/8/28.
 */
@RestController
@RequestMapping("/videoSign")
public class VideoSignController {

    @Autowired
    private VideoSignService videoSignService;

    private static final Logger logger = LoggerFactory.getLogger(VideoSignController.class);

    /**
     * 查询是否需要视频面签
     * 1 不需要面签；2 需要面签； 3 已经做过面签；
     * @param
     * @return
     */
    @RequestMapping(value = "/iSvisaInterview", method = RequestMethod.GET)
    public ResponseEntity<Message> iSvisaInterview(String applyNum){
        try {
            return  videoSignService.iSvisaInterview(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询是否需要视频面签异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 工行面签报告提交
     * @return
     */
    @RequestMapping(value = "/submitVisaInterview", method = RequestMethod.POST)
    public ResponseEntity<Message> submitVisaInterview(@RequestBody VideoSignAccountDto videoSignAccountDto, Principal user){
        try {
            return  videoSignService.submitVisaInterview(videoSignAccountDto, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("工行面签报告提交异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 获取签约排队数量
     * @return
     */
    @RequestMapping(value = "/getSignCount", method = RequestMethod.GET)
    public ResponseEntity<Message> getSignCount(){
        try {
            return  videoSignService.getSignCount();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取排队数量异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 排队数量减一
     * @param applyNum
     * @param user
     * @return
     */
    @RequestMapping(value = "/subtractionCount", method = RequestMethod.POST)
    public ResponseEntity<Message> subtractionCount(String applyNum, Principal user, String signAccount){
        try {
            return  videoSignService.subtractionCount(applyNum, user.getName(), signAccount);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("面签数量减一异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 排队数量加一
     * @param applyNum
     * @param user
     * @return
     */
    @RequestMapping(value = "/addCount", method = RequestMethod.POST)
    public ResponseEntity<Message> addCount(String applyNum, Principal user, String signAccount){
        try {
            return  videoSignService.addCount(applyNum, user.getName(), signAccount);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("面签数量加一异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询面签账号
     * @param user
     * @return
     */
    @RequestMapping(value = "/getSignAccount", method = RequestMethod.GET)
    public ResponseEntity<Message> getSignAccount(Principal user){
        try {
            return  videoSignService.getSignAccount(user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询面签账号异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 根据用户名获取主账号
     * @param user
     * @return
     */
    @RequestMapping(value = "/queryMasterAccount", method = RequestMethod.GET)
    public ResponseEntity<Message> queryMasterAccount(Principal user){
        try {
            return  videoSignService.queryMasterAccount(user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询主账号异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }
}
