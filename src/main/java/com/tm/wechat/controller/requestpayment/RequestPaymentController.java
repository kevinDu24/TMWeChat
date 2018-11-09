package com.tm.wechat.controller.requestpayment;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.requestpayment.*;
import com.tm.wechat.service.requestpayment.RequestPaymentService;
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
 * Created by pengchao on 2018/3/23.
 */
@RestController
@RequestMapping("/requestPayment")
@PreAuthorize("@permission.isDealerUser(authentication.principal.username)")
public class RequestPaymentController {

    @Autowired
    private RequestPaymentService requestPaymentService;

    private static final Logger logger = LoggerFactory.getLogger(RequestPaymentController.class);

    /**
     * 查询请款创建未提交列表（后面文字方便全局搜索：待请款列表&请款首页）
     * @param user
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    @RequestMapping(value = "/getLocalInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getLocalInfo(Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition, String beginTime, String endTime){
        try {
            return requestPaymentService.getLocalInfo(user.getName(), condition, beginTime, endTime);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取待请款列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 查询GPS-SN号
     * @param user
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/getSn", method = RequestMethod.GET)
    public ResponseEntity<Message> getSn(Principal user, String applyNum){
        try {
            return requestPaymentService.getSn(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取GPS-SN号异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询请款文件所需附件列表
     * @param applyNum 申请编号
     * @return
     */
    @RequestMapping(value = "/getPleaseDocument", method = RequestMethod.GET)
    public ResponseEntity<Message> getPleaseDocument(Principal user, String applyNum){
        try {
            return requestPaymentService.getPleaseDocument(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询请款文件所需附件列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 查询请款保单所需附件列表
     * @param applyNum 申请编号
     * @return
     */
    @RequestMapping(value = "/getPleaseKindOfPolicy", method = RequestMethod.GET)
    public ResponseEntity<Message> getPleaseKindOfPolicy(Principal user, String applyNum){
        try {
            return requestPaymentService.getPleaseKindOfPolicy(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询请款保单所需附件列表", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 查询请款信息是否保存
     * @param user
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/getRequestPaymentState", method = RequestMethod.GET)
    public ResponseEntity<Message> getRequestPaymentState(Principal user, String applyNum){
        try {
            return requestPaymentService.getRequestPaymentState(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取请款信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }



    /**
     * 查询请款文件信息
     * @param user
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/getFileInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getFileInfo(Principal user, String applyNum){
        try {
            return requestPaymentService.getFileInfo(applyNum, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取请款文件信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * GPS确认激活
     * @param sn  sn号
     * @param name 申请人姓名
     * @return
     */
    @RequestMapping(value = "/submitGpsInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> submitGpsInfo(String sn, String applyNum, String name, Principal user){
        try {
            return requestPaymentService.submitGpsInfo(applyNum, name, sn, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("GPS确认激活异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }




    /**
     * 保存请款文件信息
     * @param fileInfoDto
     * @return
     */
    @RequestMapping(value = "/saveFileInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> saveFileInfo(@RequestBody FileInfoDto fileInfoDto, Principal user){
        try {
            return requestPaymentService.saveFileInfo(fileInfoDto,user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("保存请款文件信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询请款保单信息
     * @param user
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/getInsurancePolicyInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getInsurancePolicyInfo(Principal user, String applyNum){
        try {
            return requestPaymentService.getInsurancePolicyInfo(applyNum, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取请款保单信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }



    /**
     * 保存请款保单信息
     * @param insurancePolicyDto
     * @return
     */
    @RequestMapping(value = "/saveInsurancePolicyInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> saveInsurancePolicyInfo(@RequestBody InsurancePolicyDto insurancePolicyDto, Principal user){
        try {
            return requestPaymentService.saveInsurancePolicyInfo(insurancePolicyDto, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("保存请款保单信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 提交请款信息
     * @param type 提交类型 0 提交文件信息  1 提交保单信息
     * @param applyNum 申请编号
     * @return
     */
    @RequestMapping(value = "/submitRequestPaymentInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> submitRequestPaymentInfo(@RequestParam(value = "type", required = true) String type, String applyNum, String name, Principal user){
        try {
            return requestPaymentService.submitRequestPaymentInfo(type, applyNum, user.getName(), name);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("提交请款信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 合同请款&首保上传
     * @param type  提交类型 0 提交文件信息  1 提交保单信息
     * @param name  名字
     * @param user  用户
     * @param requestPaymentDetailDto   请款信息详情
     * @return
     */
    @RequestMapping(value = "/submitRequestPaymentInfoD", method = RequestMethod.POST)
    public ResponseEntity<Message> submitRequestPaymentInfo(String type,String name, Principal user,@RequestBody RequestPaymentDetailDto requestPaymentDetailDto){
        try {
            return requestPaymentService.submitRequestPaymentInfo(type, requestPaymentDetailDto.getApplyNum(), user.getName(), name);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("提交请款信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询请款历史
     * @param condition
     * @return
     */
    @RequestMapping(value = "/getRequestPaymentHistory", method = RequestMethod.GET)
    public ResponseEntity<Message> getRequestPaymentHistory(String condition, Principal user){
        try {
            return requestPaymentService.getRequestPaymentHistory(user.getName(),condition);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询请款历史异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }



}
