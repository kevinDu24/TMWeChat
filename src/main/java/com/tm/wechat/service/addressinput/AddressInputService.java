package com.tm.wechat.service.addressinput;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.dto.approval.BankCardVerificationDto;
import com.tm.wechat.dto.approval.BankCardVerificationInfoDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.result.CoreResult;
import com.tm.wechat.dto.result.XftmAppResult;
import com.tm.wechat.dto.sign.AddressInputInfoDto;
import com.tm.wechat.dto.sms.SendShortMessageDto;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
import com.tm.wechat.service.applyOnline.XftmSystemInterface;
import com.tm.wechat.service.sign.ApplyContractService;
import com.tm.wechat.service.xftm.XftmAppInterFace;
import com.tm.wechat.utils.commons.CommonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by pengchao on 2018/6/28.
 */

@Service
public class AddressInputService {

    @Autowired
    private CoreSystemInterface coreSystemInterface;

    @Autowired
    private XftmSystemInterface xftmSystemInterface;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private XftmAppInterFace xftmAppInterFace;

    private static final Logger logger = LoggerFactory.getLogger(ApplyContractService.class);

    /**
     * 用户地址录入获取信息
     *
     * @param applyNum 申请编号
     * @return
     */
    public ResponseEntity<Message> getAddressInputInfo(String applyNum) {
        // 版本号
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.getAddressInputInfo("GetLoanCardCustomer", applyNum);
//            coreResult = "{\"addressInputInfo\":{\"bank\":\"\",\"bankCardNum\":\"\",\"idCardNum\":\"440921197401086015\",\"name\":\"张明才\",\"phoneNum\":\"13712562293\", \"address\":\"新华国际广场\"},\"result\":{\"isSuccess\":\"true\",\"resultCode\":\"000\",\"resultMsg\":\"成功\"}}";
            logger.info("GetLoanCardCustomer={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            AddressInputInfoDto addressInputInfoDto = codeResult.getClientInfo();
            if (addressInputInfoDto == null) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取主系统用户信息异常"), HttpStatus.OK);
            } else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, addressInputInfoDto), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    /**
     * 用户收货地址信息提交（web端用）
     * @return
     */
    public ResponseEntity<Message> submitAddressInputInfo(AddressInputInfoDto addressInputInfoDto) {
        //提交到主系统
        CoreResult codeResult = new CoreResult();
        try {
            logger.info("addressInputInfoDto={}", JSONObject.fromObject(addressInputInfoDto).toString(2));
            String coreResult = coreSystemInterface.submitAddressInputInfo("sendReceivingAddress", addressInputInfoDto);
            logger.info("sendReceivingAddress={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }

    /**
     * 核实工行放款卡提交
     * @return
     */
    public ResponseEntity<Message> submitVerificationInfo(BankCardVerificationDto bankCardVerificationDto) {
        if (CommonUtils.isNull(bankCardVerificationDto.getBankCardNum()) || CommonUtils.isNull(bankCardVerificationDto.getUserName())) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
        }
        //提交到主系统
        CoreResult codeResult = new CoreResult();
        try {
            logger.info("bankCardVerificationDto={}", JSONObject.fromObject(bankCardVerificationDto).toString(2));
            String coreResult = xftmSystemInterface.submitVerificationInfo("insertCardSts", bankCardVerificationDto.getUserName(), bankCardVerificationDto.getBankCardNum());
//            coreResult = "{\"bankCardVerificationInfoDto\":{\"bank\":\"\",\"contactNum\":\"65434333\",\"verificationCount\":\"5\",\"bankCardNum\":\"12345432\", \"idCardNum\":\"440921197401086015\",\"name\":\"张明才\",\"phoneNum\":\"13712562293\", \"address\":\"新华国际广场\"},\"result\":{\"isSuccess\":\"true\",\"resultCode\":\"000\",\"resultMsg\":\"成功\"}}";
            logger.info("insertCardSts={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }


    /**
     * 获取放款卡信息
     * @return
     */
    public ResponseEntity<Message> getVerificationInfo(String bankCardNum, String userName) {
        if (CommonUtils.isNull(bankCardNum) || CommonUtils.isNull(userName)) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
        }
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = xftmSystemInterface.getVerificationInfo("queryCardInfo", userName, bankCardNum);
//            coreResult = "{\"bankCardVerificationInfoDto\":{\"bank\":\"\",\"contactNum\":\"65434333\",\"verificationCount\":\"5\",\"bankCardNum\":\"12345432\", \"idCardNum\":\"440921197401086015\",\"name\":\"张明才\",\"phoneNum\":\"13712562293\", \"address\":\"新华国际广场\"},\"result\":{\"isSuccess\":\"true\",\"resultCode\":\"000\",\"resultMsg\":\"成功\"}}";
            logger.info("queryCardInfo={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            BankCardVerificationInfoDto bankCardVerificationInfoDto = codeResult.getCardVerification();
            if (bankCardVerificationInfoDto == null) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取主系统放款卡信息异常"), HttpStatus.OK);
            } else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, codeResult.getCardVerification()), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    /**
     * 获取放款卡物流信息
     * @return
     */
    public ResponseEntity<Message> getExpressInfoByCardNO(String bankCardNum, String userName) {
        if (CommonUtils.isNull(bankCardNum) || CommonUtils.isNull(userName)) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
        }
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = xftmSystemInterface.getExpressInfoByCardNO("getExpressInfoByCardNO", bankCardNum);
            logger.info("getExpressInfoByCardNO={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            if (CommonUtils.isNull(codeResult.getResult().getResultCode())) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
            }
            JSONArray expressInfoDtoList = JSONArray.fromObject(codeResult.getResult().getResultCode());
            if (expressInfoDtoList == null || expressInfoDtoList.isEmpty()) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到物流信息"), HttpStatus.OK);
            } else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, expressInfoDtoList.get(0)), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }


    /**
     * 获取放款卡物流列表信息
     * @return
     */
    public ResponseEntity<Message> listCardStatusByCondition(String queryType, String userName, String startTime, String endTime, String verificationCount, String condition, String pageIndex, String pageSize) {
//        if(CommonUtils.isNull(userName) || CommonUtils.isNull(queryType)){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
//        }
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = xftmSystemInterface.listCardStatusByCondition(
                    "listCardStatusByCondition", queryType, userName, startTime,
                    endTime, verificationCount, condition, pageIndex, pageSize);
            logger.info("listCardStatusByCondition={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            if (CommonUtils.isNull(codeResult.getResult().getResultCode())) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
            }
            JSONArray expressInfoDtoList = JSONArray.fromObject(codeResult.getResult().getResultCode());
            if (expressInfoDtoList == null || expressInfoDtoList.isEmpty()) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到物流信息"), HttpStatus.OK);
            } else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, expressInfoDtoList), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    /**
     * 获取放款卡快递详细信息
     * @return
     */
    public ResponseEntity<Message> getExpress(String courierNumber) {
        if (CommonUtils.isNull(courierNumber)) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
        }
        XftmAppResult codeResult = new XftmAppResult();
        try {
            String coreResult = xftmAppInterFace.getExpress(courierNumber);
            logger.info("getExpress={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, XftmAppResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getSuccess())) {
            JSONArray expressInfoDtoList = JSONArray.fromObject(codeResult.getObj());
            if (expressInfoDtoList == null || expressInfoDtoList.isEmpty()) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到物流详细信息"), HttpStatus.OK);
            } else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, expressInfoDtoList), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getMsg()), HttpStatus.OK);
        }
    }


    /**
     * 发送短信接口
     *
     * @return
     */
    public ResponseEntity<Message> sendShortMessage(SendShortMessageDto sendShortMessageDto) {
        if (CommonUtils.isNull(sendShortMessageDto.getPhoneNum()) || CommonUtils.isNull(sendShortMessageDto.getText())) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
        }
        //提交到主系统
        CoreResult codeResult = new CoreResult();
        try {
            logger.info("sendShortMessageDto={}", JSONObject.fromObject(sendShortMessageDto).toString(2));
            String coreResult = coreSystemInterface.SendShortMessage("SendShortMessage", sendShortMessageDto);
            logger.info("SendShortMessage={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }
}