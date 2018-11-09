package com.tm.wechat.controller.sign;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.sign.GuaranteeContractService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by pengchao on 2018/3/27.
 */
@RestController
@RequestMapping("/guaranteeSign")
@PreAuthorize("@permission.isDealerUser(authentication.principal.username)")
public class GuaranteeContractController {

    @Autowired
    private GuaranteeContractService guaranteeContractService;

    private static final Logger logger = LoggerFactory.getLogger(GuaranteeContractController.class);


    /**
     * 获取未签署合同
     * @param user
     * @return
     */
    @RequestMapping(value = "/getOriginContract", method = RequestMethod.GET)
    public ResponseEntity<Message> getOriginContract(Principal user, @RequestParam(value = "applyNum", required = true) String applyNum){
        try {
            return guaranteeContractService.getOriginContract(user.getName(), applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取共申人合同异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 发送短信验证
     * @param user
     * @return
     */
    @RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
    public ResponseEntity<Message> sendMessage(Principal user, @RequestParam(value = "applyNum", required = true) String applyNum){
        try {
            return guaranteeContractService.sendMessage(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("共申人发送短信验证异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 合同签署
     * @param user
     * @return
     */
    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    public ResponseEntity<Message> sign(Principal user, @RequestParam(value = "applyNum", required = true) String applyNum, String code){
        try {
            return guaranteeContractService.sign(applyNum, code);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("签署合同异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }



    /**
     * 合同提交接口
     * @return
     */
    @RequestMapping(value = "/submitContract",method = RequestMethod.POST)
    public ResponseEntity<Message> submitContract(String applyNum, String faceImageUrl, String idCardUrl){
        try {
            return guaranteeContractService.submitContract(applyNum, faceImageUrl, idCardUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("合同提交异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

}
