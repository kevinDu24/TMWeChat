package com.tm.wechat.controller.requestpayment;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.requestpayment.*;
import com.tm.wechat.service.requestpayment.RequestPaymentNService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created by ChengQC on 2018/9/27.
 */
@RestController
@RequestMapping("/requestPayment")
//@PreAuthorize("@permission.isDealerUser(authentication.principal.username)")
public class RequestPaymentNController {

    @Autowired
    private RequestPaymentNService requestPaymentNService;

    private static final Logger logger = LoggerFactory.getLogger(RequestPaymentNController.class);

    /**
     * 获取首页任务列表数量
     * @param user
     * @return
     */
    @RequestMapping(value = "/getHomeListNum", method = RequestMethod.GET)
    public ResponseEntity<Message> getHomeListNum(Principal user){
        try{
            return requestPaymentNService.getHomeListNum(user.getName());
        } catch (Exception ex){
            ex.printStackTrace();
            logger.error("获取首页任务数量error",ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 查询请款详细信息
     * @param user
     * @param applyNum   申请编号
     * @return
     */
    @RequestMapping(value = "/getRequestPaymentInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getRequestPaymentInfo(Principal user,String applyNum){
        try{
            return requestPaymentNService.getRequestPaymentInfo(applyNum);
        } catch (Exception ex){
            ex.printStackTrace();
            logger.error("获取待请款详细信息异常",ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 验证车架号返回车辆信息
     * @param vin
     * @return
     */
    @RequestMapping(value = "/getVehicleInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getVehicleInfo(String vin, Principal user){
        try {
            return requestPaymentNService.getVehicleInfo(vin);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("验证车架号返回车辆信息error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 首保上传详情
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/getFirstInformation", method = RequestMethod.GET)
    public ResponseEntity<Message> getFirstInformation(String applyNum){
        try {
            return requestPaymentNService.getFirstInformation(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询首保上传详情error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 获取保险公司列表
     * @return
     */
    @RequestMapping(value = "/getInsuranceCompanyList",method = RequestMethod.GET)
    public ResponseEntity<Message> getInsuranceCompanyList(){
        try {
            return requestPaymentNService.getInsuranceCompanyList();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取保险公司列表error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 获取商业险保单信息
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/getCommInsurancePolicyInfo",method = RequestMethod.GET)
    public ResponseEntity<Message> getCommInsurancePolicyInfo(String applyNum){
        try {
            return requestPaymentNService.getCommInsurancePolicyInfo(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取商业险保单信息error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 提交商业险保单信息
     * @param commInsurancePolicyInfoDto
     * @return
     */
    @RequestMapping(value = "/submitCommInsurancePolicyInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Message> submitCommInsurancePolicyInfo(@RequestBody CommInsurancePolicyInfoDto commInsurancePolicyInfoDto, Principal user){
        try {
            commInsurancePolicyInfoDto.setFpName(user.getName());
            return requestPaymentNService.submitCommInsurancePolicyInfo(commInsurancePolicyInfoDto.getApplyNum(), commInsurancePolicyInfoDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("提交商业险保单信息error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 获取交强险保单信息
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/getSaliInsurancePolicyInfo",method = RequestMethod.GET)
    public ResponseEntity<Message> getSaliInsurancePolicyInfo(String applyNum){
        try {
            return requestPaymentNService.getSaliInsurancePolicyInfo(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取交强险保单信息error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 提交交强险保单信息
     * @return
     */
    @RequestMapping(value = "/submitSaliInsurancePolicyInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Message> submitSaliInsurancePolicyInfo(@RequestBody SaliIsInsurancePolicyInfoDto saliIsInsurancePolicyInfoDto, Principal user){
        try {
            saliIsInsurancePolicyInfoDto.setFpName(user.getName());
            return requestPaymentNService.submitSaliInsurancePolicyInfo(saliIsInsurancePolicyInfoDto.getApplyNum(),saliIsInsurancePolicyInfoDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("提交交强险保单信息error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 提交合同请款
     * @param submitRequestPaymentInfoDto 合同请款详情
     * @return
     */
    @RequestMapping(value = "/submitRequestPaymentInfoN",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Message> submitRequestPaymentInfoN(@RequestBody SubmitRequestPaymentInfoDto submitRequestPaymentInfoDto, Principal user){
        try {
            return requestPaymentNService.submitRequestPaymentInfoN(submitRequestPaymentInfoDto,user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("提交合同请款error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);

        }
    }


    /**
     * 提交首保上传
     * @param firstInformationDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/submitFirstInformation",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Message> submitFirstInformation(@RequestBody FirstInformationDto firstInformationDto, Principal user){
        try {
            return requestPaymentNService.submitFirstInformation(firstInformationDto,user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("提交首保上传error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

}
