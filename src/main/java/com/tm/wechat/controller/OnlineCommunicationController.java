package com.tm.wechat.controller;

import com.tm.wechat.dto.communication.PostOnlineCommunicationDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.communication.OnlineCommunicationService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by pengchao on 2018/5/14.
 */
@RestController
@RequestMapping("/onlineCommunication")
@PreAuthorize("@permission.isDealerUser(authentication.principal.username)")
public class OnlineCommunicationController {

    @Autowired
    private OnlineCommunicationService onlineCommunicationService;

    private static final Logger logger = LoggerFactory.getLogger(OnlineCommunicationController.class);

    /**
     * 查询在线沟通列表
     * @return
     */
    @RequestMapping(value = "/getOnlineCommunicationJK", method = RequestMethod.GET)
    public ResponseEntity<Message> getRequestPaymentHistory(Principal user,  @RequestParam(value = "condition", required = false, defaultValue="") String condition){
        try {
            return onlineCommunicationService.getOnlineCommunicationJK(user.getName(), condition);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询在线沟通列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询在线沟通消息历史
     * @return
     */
    @RequestMapping(value = "/historyMessageQuery", method = RequestMethod.GET)
    public ResponseEntity<Message> historyMessageQuery(@RequestParam(value = "applyNum", required = true) String applyNum){
        try {
            return onlineCommunicationService.historyMessageQuery(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询在线沟通消息历史异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询在线沟通附件历史
     * @return
     */
    @RequestMapping(value = "/historyAttachmentQuery", method = RequestMethod.GET)
    public ResponseEntity<Message> historyAttachmentQuery(@RequestParam(value = "applyNum", required = true) String applyNum){
        try {
            return onlineCommunicationService.historyAttachmentQuery(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询在线沟通附件历史异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 在线沟通提交接口
     * @param user
     * @param submitInfo 提交信息
     * @return
     */
    @RequestMapping(value = "/postOnlineCommunication", method = RequestMethod.POST)
    public ResponseEntity<Message> postOnlineCommunication(Principal user, @RequestBody PostOnlineCommunicationDto submitInfo){
        try {
            return onlineCommunicationService.postOnlineCommunication(submitInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("在线沟通提交接口异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

}
