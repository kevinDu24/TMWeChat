package com.tm.wechat.controller.sign;

import com.tm.wechat.dto.contractsign.GpsInstallInfoDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.sign.BankCardSignInfoDto;
import com.tm.wechat.dto.sign.BasicInfoAuthDto;
import com.tm.wechat.dto.sign.CarSignInfoDto;
import com.tm.wechat.dto.webank.SignSubmitWebDto;
import com.tm.wechat.service.sign.ApplyContractService;
import com.tm.wechat.service.sign.GpsInfoService;
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
 * 签约
 * Created by pengchao on 2018/3/15.
 */
@RestController
@RequestMapping("/onApplySign")
@PreAuthorize("@permission.isDealerUser(authentication.principal.username)")
public class ApplyContractController {

    @Autowired
    ApplyContractService applyContractService;

    @Autowired
    GpsInfoService gpsInfoService;


    private static final Logger logger = LoggerFactory.getLogger(ApplyContractController.class);


    /**
     * 查询创建未提交列表
     * @param user
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    @RequestMapping(value = "/getLocalInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getLocalInfo(Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition, String beginTime, String endTime){
        try {
            return applyContractService.getLocalInfo(user.getName(), condition, beginTime, endTime);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取待签署列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 补充车辆信息
     * @param applyNum 申请编号
     * @return
     */
    @RequestMapping(value = "/submitCarInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> submitCarInfo(@RequestBody CarSignInfoDto carSignInfoDto, String applyNum,Principal user){
        try {
            return applyContractService.submitCarInfo(carSignInfoDto, applyNum, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("补充车辆信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询补充车辆信息  （待签约 - 完善信息 - 合同车辆信息）
     * @param applyNum 申请编号
     * @return
     */
    @RequestMapping(value = "/getCarInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getCarInfo(String applyNum){
        try {
            return applyContractService.getCarInfo(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取签约车辆信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 补充银行卡信息
     * @param
     * @return
     */
    @RequestMapping(value = "/submitBankCardInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> submitBankCardInfo(@RequestBody BankCardSignInfoDto bankCardInfoDto, Principal user){
        try {
            return applyContractService.submitBankCardInfo(bankCardInfoDto, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("补充银行卡信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 四要素验证
     * @return
     */
    @RequestMapping(value = "/fourFactorVerification", method = RequestMethod.POST)
    public ResponseEntity<Message> fourFactorVerification(@RequestBody BasicInfoAuthDto basicInfoAuthDto, Principal user){
        try {
            return applyContractService.fourFactorVerification(basicInfoAuthDto, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("四要素验证异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     *  用户四要素验证结果查询
     * @param
     * @return
     */
    @RequestMapping(value = "/userVerificationResult", method = RequestMethod.POST)
    public ResponseEntity<Message> userVerificationResult(@RequestBody BankCardSignInfoDto bankCardInfoDto){
        try {
            return applyContractService.userVerificationResult(bankCardInfoDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(" 用户四要素验证结果查询异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }



    /**
     * 查询补充银行卡信息    （待签约 - 完善信息 - 银行卡）
     * @param applyNum 申请编号
     * @return
     */
    @RequestMapping(value = "/getBankCardInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getBankCardInfo(String applyNum){
        try {
            return applyContractService.getBankCardInfo(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取签约银行卡信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询签约补充信息状态
     * @param applyNum 合同id
     * @return
     */
    @RequestMapping(value = "/getInfoStatus", method = RequestMethod.GET)
    public ResponseEntity<Message> getInfoStatus(@RequestParam(value = "applyNum", required = true) String applyNum){
        try {
            return applyContractService.getInfoStatus(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取签约信息状态异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 生成合同  （待签约 - 生成合同）
     * @param applyNum 申请编号
     * @return
     */
    @RequestMapping(value = "/createContract", method = RequestMethod.POST)
    public ResponseEntity<Message> createContract(@RequestParam(value = "applyNum", required = true) String applyNum){
        try {
            return applyContractService.createContract(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("生成合同异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 查询签约状态
     * @param applyNum 合同id
     * @return
     */
    @RequestMapping(value = "/getSignStatus", method = RequestMethod.GET)
    public ResponseEntity<Message> getLocalInfo(@RequestParam(value = "applyNum", required = true) String applyNum){
        try {
            return applyContractService.getSignStatus(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取签约状态异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 获取未签署合同  （预览融资租赁合同 以及后面的合同 SearchKey:预览合同 获取合同 ）
     * @param user
     * @return
     */
    @RequestMapping(value = "/getOriginContract", method = RequestMethod.GET)
    public ResponseEntity<Message> getOriginContract(Principal user, @RequestParam(value = "applyNum", required = true) String applyNum){
        try {
            return applyContractService.getOriginContract(user.getName(), applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取合同异常error", ex);
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
            return applyContractService.sendMessage(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("发送短信验证异常error", ex);
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
            return applyContractService.sign(applyNum, code, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("签署合同异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * HPL电子签约提交
     * @return
     */
    @RequestMapping(value = "/hplSignSubmit", method = RequestMethod.POST)
    public ResponseEntity<Message> hplSignSubmit(@RequestBody SignSubmitWebDto signSubmitWebDto, Principal user){
        try {
            return applyContractService.hplSignSubmit(signSubmitWebDto, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("HPL产品签约异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 在线助力融电子签约提交
     * @return
     */
    @RequestMapping(value = "/weBankSignSubmit", method = RequestMethod.POST)
    public ResponseEntity<Message> weBankSignSubmit(@RequestBody SignSubmitWebDto signSubmitWebDto, Principal user){
        try {
            return applyContractService.weBankSignSubmit(signSubmitWebDto, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("在线助力融产品签约异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询微众电子签约信息   （待签约 - 签约 - 签约页 - 主贷人签约 - 签约）
     * @param
     * @return
     */
    @RequestMapping(value = "/querySignInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> queryApplyState(@RequestParam(value = "applyNum", required = true) String applyNum){
        try {
            return applyContractService.querySignInfo(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询微众电子签约信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 合同提交接口
     * @return
     */
    @RequestMapping(value = "/submitContract",method = RequestMethod.POST)
    public ResponseEntity<Message> submitContract(Principal user, String applyNum, String faceImageUrl, String idCardUrl){
        try {
            return applyContractService.submitContract(user.getName(), applyNum, faceImageUrl, idCardUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("合同提交异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 合同GPS安装信息接口
     * @return
     */
    @RequestMapping(value = "/submitGpsInfo",method = RequestMethod.POST)
    public ResponseEntity<Message> submitGpsInfo(@RequestBody GpsInstallInfoDto gpsInstallInfoDto, String applyNum){
        try {
            return gpsInfoService.submitGpsInfo(gpsInstallInfoDto, applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("GPS安装信息提交异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询GPS安装信息接口
     * @return
     */
    @RequestMapping(value = "/getGpsInfo",method = RequestMethod.GET)
    public ResponseEntity<Message> getGpsInfo(String applyNum){
        try {
            return gpsInfoService.getGpsInfo(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("GPS安装信息提交异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 查询签约历史
     * @param condition
     * @return
     */
    @RequestMapping(value = "/getSignHistory", method = RequestMethod.GET)
    public ResponseEntity<Message> getSignHistory(String condition, Principal user){
        try {
            return applyContractService.getSignHistory(user.getName(),condition);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询签约历史异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


}
