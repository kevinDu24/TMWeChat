package com.tm.wechat.service.sign;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timevale.esign.sdk.tech.bean.PosBean;
import com.timevale.esign.sdk.tech.bean.result.AddSealResult;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.tm.wechat.config.FileUploadProperties;
import com.tm.wechat.config.VersionProperties;
import com.tm.wechat.config.WzProperties;
import com.tm.wechat.dao.ApprovalRepository;
import com.tm.wechat.dao.SysUserRepository;
import com.tm.wechat.dao.sign.*;
import com.tm.wechat.domain.*;
import com.tm.wechat.dto.approval.ApplyFromDto;
import com.tm.wechat.dto.approval.BankCardInfoDto;
import com.tm.wechat.dto.contractsign.*;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.result.CoreResult;
import com.tm.wechat.dto.sign.*;
import com.tm.wechat.dto.webank.*;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
import com.tm.wechat.service.approval.WzApplyInterface;
import com.tm.wechat.service.sysUser.SysUserService;
import com.tm.wechat.utils.IPAddressUtils;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.utils.commons.DateUtils;
import com.tm.wechat.consts.ContractType;
import com.tm.wechat.utils.esign.FileHelper;
import com.tm.wechat.utils.esign.SignHelper;
import com.tm.wechat.consts.ContractSignStatus;
import com.tm.wechat.consts.WzSubmitType;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.tm.wechat.utils.commons.ItemDeserialize.deserialize;


/**
 * Created by pengchao on 2018/3/14.
 */
@Service
public class ApplyContractService {

    @Autowired
    VersionProperties versionProperties;

    @Autowired
    SysUserRepository sysUserRepository;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    ApplyContractRepository applyContractRepository;

    @Autowired
    GuaranteeContractRepository guaranteeContractRepository;

    @Autowired
    JointContractRepository jointContractRepository;

    @Autowired
    CoreSystemInterface coreSystemInterface;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileUploadProperties fileUploadProperties;

    @Autowired
    CarSignInfoRepository carSignInfoRepository;

    @Autowired
    BankCardSignRepository bankCardSignRepository;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private WzProperties wzProperties;

    @Autowired
    private WzApplyInterface wzApplyInterface;

    @Autowired
    private ApprovalRepository approvalRepository;

    private static final Logger logger = LoggerFactory.getLogger(ApplyContractService.class);


    /**
     * 查询签约待提交列表
     * @param userName
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    public ResponseEntity<Message> getLocalInfo(String userName, String condition, String beginTime, String endTime){
        if(condition == null){
            condition = "";
        }
        if(beginTime == null){
            beginTime = "";
        }
        if(endTime == null){
            endTime = "";
        }
        List<ApplyContractLocalInfoDto> resultList = new ArrayList();
        CoreResult codeResult = new CoreResult();
        String coreResult = coreSystemInterface.queryListToSign("QueryListToSign", userName, beginTime, endTime, condition);
        try {
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            resultList = codeResult.getSignList();
            if(resultList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
            }
            //签约状态
            ApplyContract applyContract;
            for(ApplyContractLocalInfoDto applyContractLocalInfoDto : resultList){
                applyContract = applyContractRepository.findTopByApplyNum(applyContractLocalInfoDto.getApplyNum());
                if(applyContract != null){
                    applyContractLocalInfoDto.setSignState(applyContract.getSubmitState());
                }
                BankCardSign bankCardSign = bankCardSignRepository.findTopByApplyNum(applyContractLocalInfoDto.getApplyNum());
                BankCardSign newBankCardSign = new BankCardSign();
                if(bankCardSign == null){
                    newBankCardSign.setApplyNum(applyContractLocalInfoDto.getApplyNum());
                    newBankCardSign.setAuthState(applyContractLocalInfoDto.getBankVerifyState());
                    bankCardSignRepository.save(newBankCardSign);
                }else {
                    bankCardSign.setAuthState(applyContractLocalInfoDto.getBankVerifyState());
                    bankCardSignRepository.save(bankCardSign);
                }
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }


    /**
     * 获取签约补全车辆信息
     * @param applyNum 申请编号
     * @return
     */
    public ResponseEntity<Message> getCarInfo(String applyNum) {
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.getVehicleComplement("GetSignedVehicleInformationJK", applyNum);
            logger.info("GetSignedVehicleInformationJK={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            CarSignInfoDto carSignInfoDto = codeResult.getSignedVehicle();
            if(carSignInfoDto == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,  new CarSignInfoDto()), HttpStatus.OK);
            }else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, codeResult.getSignedVehicle()), HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }


    /**
     * 电子签约补充车辆信息
     * @param carSignInfoDto
     * @param applyNum
     * @return
     */
    @Transactional
    public ResponseEntity<Message> submitCarInfo(CarSignInfoDto carSignInfoDto, String applyNum, String userName){
//        if(CommonUtils.isNull(carSignInfoDto.getAuthBookImg()) || CommonUtils.isNull(carSignInfoDto.getHoldAuthBookImg()) || CommonUtils.isNull(carSignInfoDto.getSignAuthBookImg())){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，请补全签约附件"), HttpStatus.OK);
//        }
        ApplyContract personalInfo = applyContractRepository.findByApplyNum(applyNum);
        carSignInfoDto.setApplyNum(applyNum);
        //提交到主系统
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.vehicleComplement("VehicleComplement", carSignInfoDto);
            logger.info("VehicleComplement={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            CarSignInfo carSignInfo = carSignInfoRepository.findByApplyNum(applyNum);
            CarSignInfo newCarSignInfo = new CarSignInfo();
            if(carSignInfo != null){
                carSignInfoRepository.delete(carSignInfo);
            }
            BeanUtils.copyProperties(carSignInfoDto, newCarSignInfo);
            newCarSignInfo.setApplyNum(applyNum);
            newCarSignInfo.setSubmitState(ContractSignStatus.SUBMIT.code());
            newCarSignInfo.setCreateUser(userName);
            carSignInfoRepository.save(newCarSignInfo);
            //银行卡信息改变更新合同为未创建状态
            if(personalInfo == null){
                personalInfo = new ApplyContract();
                personalInfo.setApplyNum(applyNum);
            }
            //完善信息改变，改变生成合同和提交状态,打印状态,签约状态为未完成
            personalInfo.setState(ContractSignStatus.NEW.code());
            personalInfo.setCreateState(ContractSignStatus.NEW.code());
            personalInfo.setSubmitState(ContractSignStatus.NEW.code());
            personalInfo.setGetState(ContractSignStatus.NEW.code());
            JointContract jointContract = jointContractRepository.findByApplyNum(applyNum);
            if(jointContract != null){
                jointContract.setState(ContractSignStatus.NEW.code());
                jointContractRepository.save(jointContract);
            }
            GuaranteeContract guaranteeContract = guaranteeContractRepository.findByApplyNum(applyNum);
            if(guaranteeContract != null){
                guaranteeContract.setState(ContractSignStatus.NEW.code());
                guaranteeContractRepository.save(guaranteeContract);
            }
            applyContractRepository.save(personalInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }

    /**
     * 获取签约补全还款卡信息
     * @param applyNum 申请编号
     * @return
     */
    public ResponseEntity<Message> getBankCardInfo(String applyNum) {
//        BankCardSign bankCardSign = bankCardSignRepository.findByApplyNum(applyNum);
//        BankCardSignInfoDto bankCardInfoDto = new BankCardSignInfoDto();
//        if(bankCardSign != null){
//            BeanUtils.copyProperties(bankCardSign, bankCardInfoDto);
//        }
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, bankCardInfoDto), HttpStatus.OK);

        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalList = approvalRepository.findApprovalInfo(applyNum, version);
        BankCardInfoDto bankCardInfoDto = new BankCardInfoDto();
        if (!approvalList.isEmpty()) {
            ApplyFromDto resultDto = new ApplyFromDto();
            try {
                resultDto = deserialize(ApplyFromDto.class, approvalList);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
            }
            bankCardInfoDto = resultDto.getBankCardInfoDto();
        }
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.getBankCardInformationComplementJK("GetRepaymentCardInformationJK", applyNum);
            logger.info("GetRepaymentCardInformationJK={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            BankCardSignInfoDto bankCardSignInfoDto = codeResult.getBankCard();
            if(bankCardSignInfoDto == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,  new BankCardSignInfoDto()), HttpStatus.OK);
            }else {
                //若银行信息有空值，查询预审批银行卡信息
                //银行卡照片
                if(CommonUtils.isNull(bankCardSignInfoDto.getBankcardImg())){
                    bankCardSignInfoDto.setBankcardImg(bankCardInfoDto.getBankImg() == null ? "" : bankCardInfoDto.getBankImg());
                }
                //开户行
                if(CommonUtils.isNull(bankCardSignInfoDto.getBank())){
                    bankCardSignInfoDto.setBank(bankCardInfoDto.getBank() == null ? "" : bankCardInfoDto.getBank());
                }
                //银行卡卡号
                if(CommonUtils.isNull(bankCardSignInfoDto.getBankCardNum())){
                    bankCardSignInfoDto.setBankCardNum(bankCardInfoDto.getAccountNum() == null ? "" : bankCardInfoDto.getAccountNum());
                }
                //银行预留手机号
                if(CommonUtils.isNull(bankCardSignInfoDto.getPhoneNum())){
                    bankCardSignInfoDto.setPhoneNum(bankCardInfoDto.getBankPhoneNum() == null ? "" : bankCardInfoDto.getBankPhoneNum());
                }
                //银行卡户名
                if(CommonUtils.isNull(bankCardSignInfoDto.getName())){
                    bankCardSignInfoDto.setName(bankCardInfoDto.getName() == null ? "" : bankCardInfoDto.getName());
                }
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, bankCardSignInfoDto), HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    /**
     * 获取用户四要素信息
     * @param applyNum 申请编号
     * @return
     */
    public ResponseEntity<Message> fourFactorVerificationInfo(String applyNum) {
        // 版本号
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.getBankCardInformationComplementJK("GainUserFourElements", applyNum);
            logger.info("GainUserFourElements={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            FourFactorVerificationInfoDto fourFactorVerificationInfoDto = codeResult.getElements();
            if(fourFactorVerificationInfoDto == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "获取主系统用户信息异常"), HttpStatus.OK);
            }else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, codeResult.getElements()), HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    /**
     * 电子签约补充银行卡信息
     * @return
     */
    @Transactional
    public ResponseEntity<Message> submitBankCardInfo(BankCardSignInfoDto bankCardInfoDto, String userName){
        //提交到主系统
        CoreResult codeResult = new CoreResult();
        ApplyContract personalInfo = applyContractRepository.findByApplyNum(bankCardInfoDto.getApplyNum());
        if(CommonUtils.isNull(bankCardInfoDto.getBankcardImg())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全银行卡照片"), HttpStatus.OK);
        }
        try {
            String coreResult = coreSystemInterface.bankCardInformationComplementJK("BankCardInformationComplementJK", bankCardInfoDto);
            logger.info("BankCardInformationComplementJK={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            BankCardSign bankCardSign = bankCardSignRepository.findByApplyNum(bankCardInfoDto.getApplyNum());
            BankCardSign newBankCardSign = new BankCardSign();
            if(bankCardSign != null){
                bankCardSignRepository.delete(bankCardSign);
            }
            BeanUtils.copyProperties(bankCardInfoDto, newBankCardSign);
            newBankCardSign.setSubmitState(ContractSignStatus.SUBMIT.code());
            newBankCardSign.setCreateUser(userName);
            bankCardSignRepository.save(newBankCardSign);
            //银行卡信息改变更新合同为未创建状态
            if(personalInfo == null){
                personalInfo = new ApplyContract();
                personalInfo.setApplyNum(bankCardInfoDto.getApplyNum());
            }
            //完善信息改变，改变生成合同和提交状态,打印状态,签约状态为未完成
            personalInfo.setState(ContractSignStatus.NEW.code());
            personalInfo.setCreateState(ContractSignStatus.NEW.code());
            personalInfo.setSubmitState(ContractSignStatus.NEW.code());
            personalInfo.setGetState(ContractSignStatus.NEW.code());
            applyContractRepository.save(personalInfo);
            JointContract jointContract = jointContractRepository.findByApplyNum(bankCardInfoDto.getApplyNum());
            if(jointContract != null){
                jointContract.setState(ContractSignStatus.NEW.code());
                jointContractRepository.save(jointContract);
            }
            GuaranteeContract guaranteeContract = guaranteeContractRepository.findByApplyNum(bankCardInfoDto.getApplyNum());
            if(guaranteeContract != null){
                guaranteeContract.setState(ContractSignStatus.NEW.code());
                guaranteeContractRepository.save(guaranteeContract);
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }


    /**
     * 电子签约补充银行卡信息（web端）
     * @return
     */
    @Transactional
    public ResponseEntity<Message> webSubmitBankCardInfo(BankCardSignInfoDto bankCardInfoDto){
        //提交到主系统
        CoreResult codeResult = new CoreResult();
        String uniqueMark = "";
        try {
            if(CommonUtils.isNull(bankCardInfoDto.getApplyNum())){
                uniqueMark = UUID.randomUUID().toString().replace("-", "");
                bankCardInfoDto.setUniqueMark(uniqueMark);
            }
            logger.info("bankCardInfoDto={}", JSONObject.fromObject(bankCardInfoDto).toString(2));
            String coreResult = coreSystemInterface.bankCardInformationComplementJK("BankCardInformationComplementJK", bankCardInfoDto);
//            coreResult = "{\"result\":{\"isSuccess\":\"true\",\"resultCode\":\"000\",\"resultMsg\":\"For input string: \\\"null\\\"\"}}";
            logger.info("BankCardInformationComplementJK={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
//        codeResult.getResult().setIsSuccess("true");
        if("true".equals(codeResult.getResult().getIsSuccess())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, uniqueMark), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统："+codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }


    /**
     * 用户四要素验证结果查询
     * @return
     */
    public ResponseEntity<Message> userVerificationResult(BankCardSignInfoDto bankCardInfoDto){
        if(CommonUtils.isNull(bankCardInfoDto.getName()) || CommonUtils.isNull(bankCardInfoDto.getIdCardNum())
                || CommonUtils.isNull(bankCardInfoDto.getPhoneNum())
                || CommonUtils.isNull(bankCardInfoDto.getBankCardNum())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全四要素信息"), HttpStatus.OK);
        }
        //提交到主系统
        CoreResult codeResult = new CoreResult();
        try {
            logger.info("bankCardInfoDto={}", JSONObject.fromObject(bankCardInfoDto).toString(2));
            String coreResult = coreSystemInterface.userVerificationResult("UserVerificationResult", bankCardInfoDto);
//             coreResult = "{\n" +
//                    "    \"result\": {\n" +
//                    "        \"isSuccess\": \"true\",\n" +
//                    "        \"resultCode\": \"000\",\n" +
//                    "        \"resultMsg\": \"未完成四要素验证\"\n" +
//                    "    }\n" +
//                    "}";
            logger.info("UserVerificationResult={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,null, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }

    /**
     * 电子签约四要素验证
     * @param
     * @return
     */
    @Transactional
    public ResponseEntity<Message> fourFactorVerification(BasicInfoAuthDto bankCardInfoDto, String userName){
        //提交到主系统
        CoreResult codeResult = new CoreResult();
        ApplyContract personalInfo = applyContractRepository.findByApplyNum(bankCardInfoDto.getApplyNum());
        try {
            String coreResult = coreSystemInterface.fourFactorVerificationJK("FourFactorVerificationJK", bankCardInfoDto);
            logger.info("FourFactorVerificationJK={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            BankCardSign bankCardSign = bankCardSignRepository.findByApplyNum(bankCardInfoDto.getApplyNum());
            if(bankCardSign != null){
                bankCardSign.setAuthState(ContractSignStatus.SUBMIT.code());
                bankCardSign.setUpdateUser(userName);
                bankCardSignRepository.save(bankCardSign);
            }
            //银行卡信息改变更新合同未未创建状态
            if(personalInfo == null){
                personalInfo = new ApplyContract();
                personalInfo.setApplyNum(bankCardInfoDto.getApplyNum());
            }
            //完善信息改变，改变生成合同和签约状态为未完成
            personalInfo.setState(ContractSignStatus.NEW.code());
            personalInfo.setCreateState(ContractSignStatus.NEW.code());
            personalInfo.setSubmitState(ContractSignStatus.NEW.code());
            personalInfo.setGetState(ContractSignStatus.NEW.code());
            applyContractRepository.save(personalInfo);
            JointContract jointContract = jointContractRepository.findByApplyNum(bankCardInfoDto.getApplyNum());
            if(jointContract != null){
                jointContract.setState(ContractSignStatus.NEW.code());
                jointContractRepository.save(jointContract);
            }
            GuaranteeContract guaranteeContract = guaranteeContractRepository.findByApplyNum(bankCardInfoDto.getApplyNum());
            if(guaranteeContract != null){
                guaranteeContract.setState(ContractSignStatus.NEW.code());
                guaranteeContractRepository.save(guaranteeContract);
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }


    /**
     * 电子签约四要素验证（web端）
     * @param
     * @return
     */
    @Transactional
    public ResponseEntity<Message> webFourFactorVerification(BasicInfoAuthDto basicInfoAuthDto){
        //提交到主系统
        CoreResult codeResult = new CoreResult();
        logger.info("basicInfoAuthDto={}", JSONObject.fromObject(basicInfoAuthDto).toString(2));
        try {
            String coreResult = coreSystemInterface.fourFactorVerificationJK("FourFactorVerificationJK", basicInfoAuthDto);
//            coreResult = "{\"result\":{\"isSuccess\":\"true\",\"resultCode\":\"900\",\"resultMsg\":\"系统异常For input string: \\\"null\\\"\"}}";
            logger.info("FourFactorVerificationJK={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }


    /**
     * 查询签约状态
     * @param applyNum 申请编号
     * @return
     */
    public ResponseEntity<Message> getInfoStatus(String applyNum){
        SignInfoStateDto signInfoStatusDto = new SignInfoStateDto();
        Object result = carSignInfoRepository.getInfoStatus(applyNum);
        if(result != null){
            Object[] objects = (Object[]) result;
            signInfoStatusDto = new SignInfoStateDto(objects);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, signInfoStatusDto), HttpStatus.OK);
    }

    /**
     * 生成合同
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> createContract(String applyNum){
        ApplyContract personalInfo = applyContractRepository.findByApplyNum(applyNum);
        SignInfoStateDto signInfoStatusDto = new SignInfoStateDto();
        Object result = carSignInfoRepository.getInfoStatus(applyNum);
        if(result != null){
            Object[] objects = (Object[]) result;
            signInfoStatusDto = new SignInfoStateDto(objects);
        }
        if(!ContractSignStatus.SUBMIT.code().equals(signInfoStatusDto.getSignCarInfoState()) ||
                ! ContractSignStatus.SUBMIT.code().equals(signInfoStatusDto.getSignBankCardInfoState())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先完善信息"), HttpStatus.OK);
        }
        CoreResult codeResult = new CoreResult();
        ContractDto contractDto = new ContractDto();
        try {
            String coreResult = coreSystemInterface.createContract("GenerateContractJK", applyNum);
            logger.info(applyNum+"GenerateContractJK={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            if(personalInfo == null){
                personalInfo = new ApplyContract();
                personalInfo.setApplyNum(applyNum);
            }
            //生成合同，改变签约状态，提交状态为未完成
            personalInfo.setState(ContractSignStatus.NEW.code());
            personalInfo.setSubmitState(ContractSignStatus.NEW.code());
            personalInfo.setGetState(ContractSignStatus.NEW.code());
            personalInfo.setCreateState(ContractSignStatus.SUBMIT.code());
            applyContractRepository.save(personalInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }


    /**
     * 查询签约状态,保存合同信息
     * @param applyNum 申请编号
     * @return
     */
    public ResponseEntity<Message> getSignStatus(String applyNum){
        SignStatusDto signStatusDto = new SignStatusDto();
        ApplyContract applyContract = applyContractRepository.findByApplyNum(applyNum);
        //判断是否已签约，已签约不可再签约
        if(applyContract != null && ContractSignStatus.SUBMIT.code().equals(applyContract.getSubmitState())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"您已签约过电子合同"), HttpStatus.OK);
        }
        //判断是否已已经生成合同，未生成合同必须先生成合同
//        if(applyContract == null || CommonUtils.isNull(applyContract.getCreateState())|| ContractSignStatus.NEW.code().equals(applyContract.getCreateState())){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先生成合同"), HttpStatus.OK);
//        }
        JointContract jointContract = jointContractRepository.findByApplyNum(applyNum);
        GuaranteeContract guaranteeContract = guaranteeContractRepository.findByApplyNum(applyNum);
        ApplicantContractDto applicantInfo = new ApplicantContractDto();//申请人信息
        ApplicantContractDto jointInfo = new ApplicantContractDto();//共申人信息
        ApplicantContractDto guaranteeInfo = new ApplicantContractDto();//担保人信息
        if(applyContract != null){
            Object result = applyContractRepository.getSignStatus(applyNum);
            if(result == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"未查询到该单号合同信息"), HttpStatus.OK);
            }
            Object[] objects = (Object[]) result;
            signStatusDto = new SignStatusDto(objects);
        }
        //未查询到主贷人合同信息(第一次调用该接口,或者尚未签约)
        if(applyContract == null || (applyContract != null && (CommonUtils.isNull(signStatusDto.getApplyContractStatus())
                || ContractSignStatus.NEW.code().equals(applyContract.getState())))){
            ContractInfoDto contractInfoDto = new ContractInfoDto();
            CoreResult codeResult = new CoreResult();
            String coreResult = coreSystemInterface.orderInformation("OrderInformation", applyNum);
            logger.info("OrderInformation={}", coreResult);
            try {
                codeResult = objectMapper.readValue(coreResult, CoreResult.class);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            if("true".equals(codeResult.getResult().getIsSuccess())){
                contractInfoDto = codeResult.getOrderInfo();
                if(contractInfoDto == null){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
                }
                //主审人
                if(contractInfoDto.getApplicant() != null && !contractInfoDto.getApplicant().isEmpty()){
                    //保存主申人信息
                    applicantInfo = contractInfoDto.getApplicant().get(0);
                    if(applyContract == null){
                        applyContract = new ApplyContract();
                    }
                    applyContract.setName(applicantInfo.getName());
                    applyContract.setIdCard(applicantInfo.getIdCard());
                    applyContract.setPhoneNum(applicantInfo.getPhoneNum());
                    applyContract.setApplyNum(applyNum);
                    applyContract.setIsICBC(contractInfoDto.getIsICBC());
                    applyContract.setState(ContractSignStatus.NEW.code());
                    applyContractRepository.save(applyContract);
                    signStatusDto.setApplyContractStatus(ContractSignStatus.NEW.code());
                    //保存共申人信息
//                    if(contractInfoDto.getJoint() != null &&  !contractInfoDto.getJoint().isEmpty()){
//                        jointInfo = contractInfoDto.getJoint().get(0);
//                        if(jointContract == null){
//                            jointContract = new JointContract();
//                        }
//                        jointContract.setName(jointInfo.getName());
//                        jointContract.setIdCard(jointInfo.getIdCard());
//                        jointContract.setPhoneNum(jointInfo.getPhoneNum());
//                        jointContract.setApplyNum(applyNum);
//                        jointContract.setApplyContractId(applyContract.getId());
//                        jointContract.setState(ContractSignStatus.NEW.code());
//                        jointContractRepository.save(jointContract);
//                        signStatusDto.setJointContractStatus(ContractSignStatus.NEW.code());
//                        jointInfo.setIdCard(contractInfoDto.getJoint().get(0).getIdCard());
//                        jointInfo.setName(contractInfoDto.getJoint().get(0).getName());
//                    }
//                    //保存担保人信息
//                    if(contractInfoDto.getGuarantee() != null &&  !contractInfoDto.getGuarantee().isEmpty()){
//                        guaranteeInfo = contractInfoDto.getGuarantee().get(0);
//                        if(guaranteeContract == null){
//                            guaranteeContract = new GuaranteeContract();
//                        }
//                        guaranteeContract.setName(guaranteeInfo.getName());
//                        guaranteeContract.setIdCard(guaranteeInfo.getIdCard());
//                        guaranteeContract.setPhoneNum(guaranteeInfo.getPhoneNum());
//                        guaranteeContract.setApplyNum(applyNum);
//                        guaranteeContract.setApplyContractId(applyContract.getId());
//                        guaranteeContract.setState(ContractSignStatus.NEW.code());
//                        guaranteeContractRepository.save(guaranteeContract);
//                        signStatusDto.setGuaranteeContractStatus(ContractSignStatus.NEW.code());
//                        guaranteeInfo.setIdCard(contractInfoDto.getGuarantee().get(0).getIdCard());
//                        guaranteeInfo.setName(contractInfoDto.getGuarantee().get(0).getName());
//                    }
                    signStatusDto.setIsICBC(codeResult.getOrderInfo().getIsICBC());
                    signStatusDto.setApplicantInfo(applicantInfo);
                    //目前只需要主贷人签约
//                    signStatusDto.setJointInfo(jointInfo);
//                    signStatusDto.setGuaranteeInfo(guaranteeInfo);
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, signStatusDto), HttpStatus.OK);
                }
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
            }
        }
//        Object[] objects = (Object[]) result;
//        signStatusDto = new SignStatusDto(objects);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, signStatusDto), HttpStatus.OK);
    }


    /**
     * 获取合同
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> getOriginContract(String userName, String applyNum){
        ApplyContract personalInfo = applyContractRepository.findByApplyNum(applyNum);
        // *************** AppStore审核用的测试账号分支，开始 **************
        if("123456".equals(applyNum)){
            FileUrlDto resultDto = new FileUrlDto();
            resultDto.setContactPdfUrl("http://222.73.56.22:89/cmdOtherImg/20180410/26cd9cf8-2fc5-418b-bbeb-2b9e1f18c1cb.pdf");
//            resultDto.setContactPdfUrl("http://222.73.56.22:89/cmdOtherImg/20180414/d0db877a-7c27-465b-b736-9dc7890ed039.pdf");
            resultDto.setDeliveryReceitp("http://222.73.56.22:89/cmdOtherImg/20180410/71440593-552b-4647-a517-33dd3e87d7f9.pdf");
            resultDto.setMortgageContractPdfUrl("http://222.73.56.22:89/cmdOtherImg/20180410/8082bbe5-ffb7-4a02-91ad-62019a3865d7.pdf");
//            resultDto.setRepaymentPlanPdfUrl("http://222.73.56.22:89/cmdOtherImg/20180414/d0db877a-7c27-465b-b736-9dc7890ed039.pdf");
            resultDto.setRepaymentPlanPdfUrl("http://222.73.56.22:89/cmdOtherImg/20180410/a06931a7-4a77-42e2-9658-aecc7e1970bf.pdf");
            resultDto.setRiskNotificationPdfUrl("http://222.73.56.22:89/cmdOtherImg/20180410/91db727f-9b04-4fbc-9d9f-40e942f27d8c.pdf");
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto), HttpStatus.OK);
        }
        // *************** AppStore审核用的测试账号分支，结束 **************
        CoreResult codeResult = new CoreResult();
        ContractDto contractDto = new ContractDto();
        //合同已打印，从数据库获取合同
        if(personalInfo != null && ContractSignStatus.SUBMIT.code().equals(personalInfo.getGetState()) && CommonUtils.isNotNull(personalInfo.getContactPdf())){
            FileUrlDto resultDto = new FileUrlDto();
            resultDto.setContactPdfUrl(personalInfo.getContactPdf());
            resultDto.setDeliveryReceitp(personalInfo.getConfirmationPdf());
            resultDto.setMortgageContractPdfUrl(personalInfo.getMortgageContractPdf());
            resultDto.setRepaymentPlanPdfUrl(personalInfo.getRepaymentPlanPdf());
            resultDto.setRiskNotificationPdfUrl(personalInfo.getRiskNotificationPdf());
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto), HttpStatus.OK);
        }
        try {
            String coreResult = coreSystemInterface.acquisitionOfContract("AcquisitionOfContract", applyNum);
            logger.info("AcquisitionOfContract={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            contractDto = codeResult.getContractrInfo();
            if(contractDto.getApplicant() == null || contractDto.getApplicant().isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取合同失败，合同为空"), HttpStatus.OK);
            }
            String contractPdf = contractDto.getApplicant().get(0).getLeaseContract();
            String confirmationPdf = contractDto.getApplicant().get(0).getDeliveryReceitp();
            String mortgageContractPdf = contractDto.getApplicant().get(0).getMortgageContract();
            String repaymentPlanPdf = contractDto.getApplicant().get(0).getRepaymentPlan();
            String riskNotificationPdf = contractDto.getApplicant().get(0).getRiskNotification();
            if(CommonUtils.isNull(contractPdf) || CommonUtils.isNull(confirmationPdf)
                    || CommonUtils.isNull(mortgageContractPdf)  || CommonUtils.isNull(repaymentPlanPdf) || CommonUtils.isNull(riskNotificationPdf) ){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未获取到合同模板，请联系客服"), HttpStatus.OK);
            }
            if(personalInfo == null){
                personalInfo = new ApplyContract();
                personalInfo.setApplyNum(applyNum);
            }
            personalInfo.setContactPdf(contractPdf);
            personalInfo.setConfirmationPdf(confirmationPdf);
            personalInfo.setMortgageContractPdf(mortgageContractPdf);
            personalInfo.setRepaymentPlanPdf(repaymentPlanPdf);
            personalInfo.setRiskNotificationPdf(riskNotificationPdf);
            personalInfo.setApplyNum(applyNum);
            applyContractRepository.save(personalInfo);

            //判断用户签约了之后，才打印合同
            if(ContractSignStatus.SUBMIT.code().equals(personalInfo.getSubmitState())){
                //打印合同
                try {
                    String result = coreSystemInterface.getContractPrinting("GetContractPrinting", applyNum);
                    logger.info("GetContractPrinting={}", result);
                    codeResult = objectMapper.readValue(result, CoreResult.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if("true".equals(codeResult.getResult().getIsSuccess())){
                    personalInfo.setGetState(ContractSignStatus.SUBMIT.code());
                    applyContractRepository.save(personalInfo);

                }else {
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统：打印合同失败："+ codeResult.getResult().getResultMsg()), HttpStatus.OK);
                }
            }



            //返回合同地址到页面
            FileUrlDto resultDto = new FileUrlDto();
            resultDto.setContactPdfUrl(personalInfo.getContactPdf());
            resultDto.setDeliveryReceitp(personalInfo.getConfirmationPdf());
            resultDto.setMortgageContractPdfUrl(personalInfo.getMortgageContractPdf());
            resultDto.setRepaymentPlanPdfUrl(personalInfo.getRepaymentPlanPdf());
            resultDto.setRiskNotificationPdfUrl(personalInfo.getRiskNotificationPdf());
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto), HttpStatus.OK);



        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统："+codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }


    /**
     * 发送短信（签订合同）
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> sendMessage(String applyNum){
        ApplyContract personalInfo = applyContractRepository.findByApplyNum(applyNum);
        if(personalInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "发送短信失败"), HttpStatus.OK);
        }
        String accountId = personalInfo.getAccountId();
        if(accountId == null || accountId.isEmpty()){
            accountId = SignHelper.addPersonAccount(personalInfo.getIdCard(), personalInfo.getName());
            if(accountId == null || accountId.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "短信发送失败，创建个人账户失败"), HttpStatus.OK);
            }
        }
        boolean flag = SignHelper.sendMessage(accountId, personalInfo.getPhoneNum());
        if(!flag){
            logger.error("发送短信失败，手机号", personalInfo.getPhoneNum());
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "短信发送失败!"), HttpStatus.OK);
        }
        personalInfo.setAccountId(accountId);
        applyContractRepository.save(personalInfo);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 签署合同
     * @return
     */
    public ResponseEntity<Message> sign(String applyNum, String code, String userName){
        ResponseEntity<Message> responseEntity ;
        ApplyContract personalInfo = applyContractRepository.findByApplyNum(applyNum);
        if(personalInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "签约失败，未找到订单!"), HttpStatus.OK);
        }
        JointContract jointContract = jointContractRepository.findByApplyNum(applyNum);
        //返回客户端结果
        FileUrlDto signFileUrlDto = new FileUrlDto();
        //判断有无共申人，有无共申人租赁合同不一致
        boolean haveJoint = false;
        //有共申人
        if(jointContract != null) {
            //签署合同(有共申人的租赁合同)
            haveJoint = true;
        }
        responseEntity = signPdf(personalInfo,"1", code, haveJoint, userName);
        //签署失败返回错误信息
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        signFileUrlDto.setContactPdfUrl(responseEntity.getBody().getData().toString());
        //租赁合同签署完成后签署确认函
        responseEntity = signPdf(personalInfo,"0", code, haveJoint, userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        signFileUrlDto.setDeliveryReceitp(responseEntity.getBody().getData().toString());
        //确认函签署完成后签署抵押合同
        responseEntity = signPdf(personalInfo,"2", code, haveJoint, userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        signFileUrlDto.setMortgageContractPdfUrl(responseEntity.getBody().getData().toString());

        //抵押合同签署完成后签署还款计划表
        responseEntity = signPdf(personalInfo,"3", code, haveJoint, userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        signFileUrlDto.setRepaymentPlanPdfUrl(responseEntity.getBody().getData().toString());
        //还款计划表签署完成后签署风险告知书
        responseEntity = signPdf(personalInfo,"4", code, haveJoint, userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        signFileUrlDto.setRiskNotificationPdfUrl(responseEntity.getBody().getData().toString());
        //全部签署完成后更新主贷人签约状态为完成状态
        personalInfo.setState(ContractSignStatus.SUBMIT.code());
        applyContractRepository.save(personalInfo);
        //签署完成的主合同和还款计划表交给共申人签约
        GuaranteeContract guaranteeContract = guaranteeContractRepository.findByApplyNum(applyNum);
        //如果有共申人由共申人签
        if(jointContract != null){
            jointContract.setContactPdf(signFileUrlDto.getContactPdfUrl());
            jointContract.setRepaymentPlanPdf(signFileUrlDto.getRepaymentPlanPdfUrl());
            jointContractRepository.save(jointContract);
            //如果无共申人由担保人签
        } else if(jointContract == null && guaranteeContract != null){
            guaranteeContract.setContactPdf(signFileUrlDto.getContactPdfUrl());
            guaranteeContract.setRepaymentPlanPdf(signFileUrlDto.getRepaymentPlanPdfUrl());
            guaranteeContractRepository.save(guaranteeContract);
        }
        //签署成功后返回所有合同
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, signFileUrlDto), HttpStatus.OK);
    }


    /**
     * HPL电子签约提交
     * @return
     */
    public ResponseEntity<Message> hplSignSubmit(SignSubmitWebDto signSubmitWebDto, String userName){
        //校验短信验证码
        if(CommonUtils.isNull(signSubmitWebDto.getMsgCode())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "短信验证码不可为空"), HttpStatus.OK);
        }
        String message = sysUserService.checkMsgCode(signSubmitWebDto.getPhoneNum(), signSubmitWebDto.getMsgCode());
        if(message != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
        }
        ApplyContract personalInfo = applyContractRepository.findByApplyNum(signSubmitWebDto.getApplyNum());
        if(personalInfo == null){
            personalInfo = new ApplyContract();
        }
        //更新申请状态
        personalInfo.setState(ContractType.SIGNED.code());
        personalInfo.setSubmitTime(new Date());
        personalInfo.setSubmitUser(userName);
        personalInfo.setSubmitState(ContractSignStatus.SUBMIT.code());
        applyContractRepository.save(personalInfo);


        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 在线助力融电子签约提交
     * @return
     */
    public ResponseEntity<Message> weBankSignSubmit(SignSubmitWebDto signSubmitWebDto, String userName){
        //校验短信验证码
        if(CommonUtils.isNull(signSubmitWebDto.getMsgCode())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "短信验证码不可为空"), HttpStatus.OK);
        }
        String message = sysUserService.checkMsgCode(signSubmitWebDto.getPhoneNum(), signSubmitWebDto.getMsgCode());
        if(message != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
        }
        //若前端获取不到ip，后台获取
        if (CommonUtils.isNull(signSubmitWebDto.getIp())){
            String ip = IPAddressUtils.getIpAddress(httpServletRequest);
            if(ip == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取ip地址失败"), HttpStatus.OK);
            }
            signSubmitWebDto.setIp(ip);
        }

        SignSubmitDto dto = buildDto(signSubmitWebDto); //提交参数构建
        WzResultCommonDto wzResultCommonDto = new WzResultCommonDto();
        try {
            //电子签约提交
            logger.info("SignSubmitDto={}", JSONObject.fromObject(dto).toString(2));
            String result = wzApplyInterface.signSubmit(dto);
//            System.out.println(result);
            logger.info("SignSubmitResult={}", result);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            wzResultCommonDto = objectMapper.readValue(result, WzResultCommonDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "签约失败,系统异常"), HttpStatus.OK);
        }
        ApplyContract personalInfo = applyContractRepository.findByApplyNum(signSubmitWebDto.getApplyNum());
        if(personalInfo == null){
            personalInfo = new ApplyContract();
        }
        //更新申请状态
        if("0000".equals(wzResultCommonDto.getCode())){
            personalInfo.setState(ContractSignStatus.SUBMIT.code());
            personalInfo.setSignStatus(ContractType.SIGNED.code());
            if(wzResultCommonDto.getData() != null){
                personalInfo.setSignDate(wzResultCommonDto.getData().toString());
            }
            personalInfo.setSubmitTime(new Date());
            personalInfo.setSubmitUser(userName);
            personalInfo.setSubmitState(ContractSignStatus.SUBMIT.code());
            applyContractRepository.save(personalInfo);

            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统："+ wzResultCommonDto.getMessage()), HttpStatus.OK);
    }


    private SignSubmitDto buildDto(SignSubmitWebDto signSubmitWebDto) {
        SignSubmitDto result = new SignSubmitDto(signSubmitWebDto, wzProperties);
        return result;
    }


    /**
     * 电子签约信息查询
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> querySignInfo(String applyNum){
        SignInfoQueryDto signInfoQueryDto = new SignInfoQueryDto();
        signInfoQueryDto.setApplyNum(applyNum);
        signInfoQueryDto.setTXN_ID(WzSubmitType.SIGNINFOQUERY.code());
        WzSignInfoResultDto wzSignInfoResultDto = new WzSignInfoResultDto();
        try {
            //提交一审信息到主系统
            String result = wzApplyInterface.querySignInfo(signInfoQueryDto);
            String result1 = "{\"CODE\":\"0000\",\"DATA\":{\n" +
                    "  \"totalInvestment\":\"100000\",\n" +
                    "  \"applyNum\":\"38154354\",\n" +
                    "  \"financeTerm\":\"24\",\n" +
                    "  \"monthPay\":\"4200\",\n" +
                    "  \"salePrice\":\"20000\",\n" +
                    "  \"retrofittingFee\":\"101\",\n" +
                    "  \"extendedWarranty\":\"102\",\n" +
                    "  \"gps\":\"650\",\n" +
                    "  \"compulsoryInsurance\":\"100,101\",\n" +
                    "  \"commercialInsurance\":\"201,202\",\n" +
                    "  \"vehicleTax\":\"202,203\",\n" +
                    "  \"unexpected\":\"1231\",\n" +
                    "  \"xfws\":\"9527\",\n" +
                    "  \"name\":\"杜雨生\",\n" +
                    "  \"idCardNum\": \"342244199404083222\",\n" +
                    "  \"carName\":\"奥迪\",\n" +
                    "  \"grantBank\":\"微众银行\",\n" +
                    "    \"phoneNum\":\"18055313782\",\n" +
                    "  \"financeAmount\": \"12000\",\n" +
                    "  \"purchaseTax\":\"100\",\n" +
                    "    \"otherFee\":\"22\",\n" +
                    "    \"creditFinanceAmount\":\"1233\",\n" +
                    "  \"rates\": \"14.88%\"\n" +
                    "},\"MESSAGE\":\"人太丑，不予通过\"}";
            logger.info("querySignInfo={}", result);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            wzSignInfoResultDto = objectMapper.readValue(result, WzSignInfoResultDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询审批结果失败"), HttpStatus.OK);
        }

        //更新申请状态
        if("0000".equals(wzSignInfoResultDto.getCode())){
            // 申请信息保存到本地DB中
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, wzSignInfoResultDto.getData()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, wzSignInfoResultDto.getMessage()), HttpStatus.OK);
    }

    /**
     * 签署合同pdf
     * @return
     */
    public ResponseEntity<Message> signPdf(ApplyContract personalInfo, String type, String code, boolean haveJoint, String userName){
        String timeStamp = String.valueOf(new Date().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String uploadDate = sdf.format(new Date());
        String srcPdfUrl = "";
        String signType = "";
        if("0".equals(type)){
            srcPdfUrl = personalInfo.getConfirmationPdf();
            signType = "_确认函_";
        } else if("1".equals(type)){
            srcPdfUrl = personalInfo.getContactPdf();
            signType = "_合同_";
        } else if("2".equals(type)){
            srcPdfUrl = personalInfo.getMortgageContractPdf();
            signType = "_抵押合同_";
        } else if("3".equals(type)){
            srcPdfUrl = personalInfo.getRepaymentPlanPdf();
            signType = "_还款计划表_";
        } else if("4".equals(type)){
            srcPdfUrl = personalInfo.getRiskNotificationPdf();
            signType = "_风险告知书_";
        }
        URL url1 = null;
        byte[] byt = null;
        try {
            url1 = new URL(srcPdfUrl);
            HttpURLConnection conn = (HttpURLConnection)url1.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            DataInputStream in = new DataInputStream(conn.getInputStream());
            File goalFile = new File(fileUploadProperties.getPdfPath()
                    + uploadDate + "/" + personalInfo.getName() + signType  + timeStamp + ".pdf");
            // 获取父级文件夹
            File fileParent = goalFile.getParentFile();
            if(!fileParent.exists()){
                // 不存在就创建文件夹
                fileParent.mkdirs();
            }
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileUploadProperties.getPdfPath()
                    + uploadDate + "/" + personalInfo.getName() + signType  + timeStamp + ".pdf"));
            byte[] buffer = new byte[10485760];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同模板下载失败" +signType), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同模板下载失败" + signType), HttpStatus.OK);
        }
        // 待签署的PDF文件路径
        String srcPdfFile = fileUploadProperties.getPdfPath() + uploadDate + "/" + personalInfo.getName() + signType  + timeStamp + ".pdf";

        // 最终签署后的PDF文件路径
        String signedFolder = fileUploadProperties.getSignedPdfPath() + uploadDate + "/";
        // 最终签署后PDF文件名称
        String signedFileName = personalInfo.getName() + signType + timeStamp + ".pdf";
        String url = fileUploadProperties.getSignedPdfPath() + uploadDate + "/" + signedFileName;
        File goalFile = new File(url);
        // 获取父级文件夹
        File fileParent = goalFile.getParentFile();
        if(!fileParent.exists()){
            // 不存在就创建
            fileParent.mkdirs();
        }
        String downLoadUrl = fileUploadProperties.getRequestSignedPdfPath() + uploadDate + "/" + signedFileName;
        FileDigestSignResult result = doSignWithTemplateSealByStream(personalInfo, downLoadUrl, srcPdfFile, signedFolder, signedFileName, personalInfo.getPhoneNum(), code, type, haveJoint, userName);
        if(0 == result.getErrCode()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, (Object)downLoadUrl), HttpStatus.OK);
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "签署合同失败！" + signType + result.getMsg()), HttpStatus.OK);
        }
    }




    /***
     * 签署人之间用文件二进制流传递,标准模板印章签署，所用印章SealData为addTemplateSeal接口创建的模板印章返回的SealData
     *
     * @param srcPdfFile
     * @param signedFolder
     * @param signedFileName
     * @param type 0:确认函 1:合同
     */
    private FileDigestSignResult doSignWithTemplateSealByStream(ApplyContract personalInfo, String downLoadUrl,
                                                                String srcPdfFile, String signedFolder,
                                                                String signedFileName, String mobile, String code, String type, boolean haveJoint, String userName) {
        // 创建个人客户账户
        String userPersonAccountId = personalInfo.getAccountId();
        if(userPersonAccountId == null || userPersonAccountId.isEmpty()){
            userPersonAccountId = SignHelper.addPersonAccount(personalInfo.getIdCard(), personalInfo.getName());
        }
        // 创建个人印章
        AddSealResult userPersonSealData = new AddSealResult();
        String sealData = personalInfo.getSealData();
        if(sealData == null || sealData.isEmpty()){
            userPersonSealData = SignHelper.addPersonTemplateSeal(userPersonAccountId);
        } else {
            userPersonSealData.setSealData(sealData);
        }
        // 签署合同
        if("1".equals(type)){
            // 贵公司签署，签署方式,以文件流的方式传递pdf文档
//            FileDigestSignResult platformSignResult = SignHelper.platformSignByStreamm(srcPdfFile);

            FileDigestSignResult userPersonSignResult = new FileDigestSignResult();
            // 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档,签章1
            // 有共申人的租赁合同
            if(haveJoint){
                userPersonSignResult = SignHelper.userPersonSignByStream(FileHelper.getBytes(srcPdfFile),
                        userPersonAccountId, userPersonSealData.getSealData(), mobile, code, "4");
                // 无共申人的租赁合同
            }else {
                userPersonSignResult = SignHelper.userPersonSignByStream(FileHelper.getBytes(srcPdfFile),
                        userPersonAccountId, userPersonSealData.getSealData(), mobile, code, "1");
            }
            if (0 != userPersonSignResult.getErrCode()) {
                return userPersonSignResult;
            }
            // 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档,签章1
            FileDigestSignResult userPersonSignResult1 = SignHelper.userPersonSignByStream(userPersonSignResult.getStream(),
                    userPersonAccountId, userPersonSealData.getSealData(), mobile, code, "2");
            // 所有签署完成,将最终签署后的文件流保存到本地
            if (0 == userPersonSignResult1.getErrCode()) {
                // 首次创建个人账号，保存个人账号信息
                personalInfo.setAccountId(userPersonAccountId);
                personalInfo.setSealData(userPersonSealData.getSealData());
                // 保存签署记录
                personalInfo.setSignServiceId(userPersonSignResult1.getSignServiceId());
                personalInfo.setContactSignedPdf(downLoadUrl);
                personalInfo.setSignUser(userName);
                applyContractRepository.save(personalInfo);
                SignHelper.saveSignedByStream(userPersonSignResult1.getStream(), signedFolder, signedFileName);
            }
            return userPersonSignResult1;
            //签署确认函
        } else if("0".equals(type)){
            // 贵公司签署，签署方式,以文件流的方式传递pdf文档
//            FileDigestSignResult platformSignResult = SignHelper.platformSignByStreammConfirm(srcPdfFile);

            PosBean posBean = setKeyPosBeanConfirm("承租人签字");
            // 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档
            FileDigestSignResult userPersonSignResult = SignHelper.userPersonSignByStreamConfirm(FileHelper.getBytes(srcPdfFile),
                    userPersonAccountId, userPersonSealData.getSealData(), posBean);
            // 所有签署完成,将最终签署后的文件流保存到本地
            if (0 == userPersonSignResult.getErrCode()) {
                // 首次创建个人账号，保存个人账号信息
                personalInfo.setAccountId(userPersonAccountId);
                personalInfo.setSealData(userPersonSealData.getSealData());
                // 保存签署记录
                personalInfo.setSignConfirmationServiceId(userPersonSignResult.getSignServiceId());
                personalInfo.setConfirmationSignedPdf(downLoadUrl);
                personalInfo.setSignUser(userName);
                applyContractRepository.save(personalInfo);
                SignHelper.saveSignedByStream(userPersonSignResult.getStream(), signedFolder, signedFileName);
            }
            return userPersonSignResult;
            //签署抵押合同
        } else if("2".equals(type)){
            // 贵公司签署，签署方式,以文件流的方式传递pdf文档
//            FileDigestSignResult platformSignResult = SignHelper.platformSignByStreammConfirm(srcPdfFile);

            PosBean posBean = setKeyPosBean("抵押人签字/盖章");
            // 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档
            FileDigestSignResult userPersonSignResult = SignHelper.userPersonSignByStreamConfirm(FileHelper.getBytes(srcPdfFile),
                    userPersonAccountId, userPersonSealData.getSealData(), posBean);
            // 所有签署完成,将最终签署后的文件流保存到本地
            if (0 == userPersonSignResult.getErrCode()) {
                // 首次创建个人账号，保存个人账号信息
                personalInfo.setAccountId(userPersonAccountId);
                personalInfo.setSealData(userPersonSealData.getSealData());
                // 保存签署记录
                personalInfo.setSignMortgageContractServiceId(userPersonSignResult.getSignServiceId());
                personalInfo.setMortgageContractSignedPdf(downLoadUrl);
                personalInfo.setSignUser(userName);
                applyContractRepository.save(personalInfo);
                SignHelper.saveSignedByStream(userPersonSignResult.getStream(), signedFolder, signedFileName);
            }
            return userPersonSignResult;
            //还款计划表
        } else if("3".equals(type)){
            PosBean posBean;
            if(haveJoint){
                posBean =setKeyPosBean("主承租人签字/盖章");
            }else {
                posBean =setKeyPosBean("承租人签字/盖章");
            }
            // 贵公司签署，签署方式,以文件流的方式传递pdf文档
//            FileDigestSignResult platformSignResult = SignHelper.platformSignByStreammConfirm(srcPdfFile);
            // 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档,签章1
            FileDigestSignResult userPersonSignResult = SignHelper.userSignByStream(FileHelper.getBytes(srcPdfFile),
                    userPersonAccountId, userPersonSealData.getSealData(), posBean);
            if (0 != userPersonSignResult.getErrCode()) {
                return userPersonSignResult;
            }
            posBean = setKeyPosBean1("还款银行卡户主签字");
            // 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档,签章1
            FileDigestSignResult userPersonSignResult1 = SignHelper.userSignByStream(userPersonSignResult.getStream(),
                    userPersonAccountId, userPersonSealData.getSealData(), posBean);
            // 所有签署完成,将最终签署后的文件流保存到本地
            if (0 == userPersonSignResult1.getErrCode()) {
                // 首次创建个人账号，保存个人账号信息
                personalInfo.setAccountId(userPersonAccountId);
                personalInfo.setSealData(userPersonSealData.getSealData());
                // 保存签署记录
                personalInfo.setSignRepaymentPlanServiceId(userPersonSignResult1.getSignServiceId());
                personalInfo.setRepaymentPlanSignedPdf(downLoadUrl);
                personalInfo.setSignUser(userName);
                applyContractRepository.save(personalInfo);
                SignHelper.saveSignedByStream(userPersonSignResult1.getStream(), signedFolder, signedFileName);
            }
            return userPersonSignResult1;
            //风险告知书
        } else if("4".equals(type)){
            // 贵公司签署，签署方式,以文件流的方式传递pdf文档
//            FileDigestSignResult platformSignResult = SignHelper.platformSignByStreammConfirm(srcPdfFile);

            PosBean posBean = setKeyPosBeanConfirm1("客户（签署）");
            // 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档
            FileDigestSignResult userPersonSignResult = SignHelper.userPersonSignByStreamConfirm(FileHelper.getBytes(srcPdfFile),
                    userPersonAccountId, userPersonSealData.getSealData(), posBean);
            // 所有签署完成,将最终签署后的文件流保存到本地
            if (0 == userPersonSignResult.getErrCode()) {
                // 首次创建个人账号，保存个人账号信息
                personalInfo.setAccountId(userPersonAccountId);
                personalInfo.setSealData(userPersonSealData.getSealData());
                // 保存签署记录
                personalInfo.setSignRiskNotificationServiceId(userPersonSignResult.getSignServiceId());
                personalInfo.setRiskNotificationSignedPdf(downLoadUrl);
                personalInfo.setSignUser(userName);
                applyContractRepository.save(personalInfo);
                SignHelper.saveSignedByStream(userPersonSignResult.getStream(), signedFolder, signedFileName);
            }
            return userPersonSignResult;
        }
        return null;
    }

    /***
     * 关键字定位签署的PosBean
     */
    public static PosBean setKeyPosBean(String key) {
        PosBean posBean = new PosBean();
        // 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。
        posBean.setPosType(1);
        // 关键字签署时不可空 */
        posBean.setKey(key);
        // 关键字签署时会对整体pdf文档进行搜索，故设置签署页码无效
        // posBean.setPosPage("1");
        // 签署位置X坐标，以关键字所在位置为原点进行偏移，默认值为0，控制横向移动距离，单位为px
        posBean.setPosX(40);
        // 签署位置Y坐标，以关键字所在位置为原点进行偏移，默认值为0，控制纵向移动距离，单位为px
        posBean.setPosY(-25);
        // 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述
        posBean.setWidth(100);
        return posBean;
    }

    /***
     * 关键字定位签署的PosBean
     */
    public static PosBean setKeyPosBean1(String key) {
        PosBean posBean = new PosBean();
        // 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。
        posBean.setPosType(1);
        // 关键字签署时不可空 */
        posBean.setKey(key);
        // 关键字签署时会对整体pdf文档进行搜索，故设置签署页码无效
        // posBean.setPosPage("1");
        // 签署位置X坐标，以关键字所在位置为原点进行偏移，默认值为0，控制横向移动距离，单位为px
        posBean.setPosX(40);
        // 签署位置Y坐标，以关键字所在位置为原点进行偏移，默认值为0，控制纵向移动距离，单位为px
        posBean.setPosY(-30);
        // 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述
        posBean.setWidth(100);
        return posBean;
    }

    /***
     * 关键字定位签署的PosBean
     */
    public static PosBean setKeyPosBeanConfirm(String key) {
        PosBean posBean = new PosBean();
        // 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。
        posBean.setPosType(1);
        // 关键字签署时不可空 */
        posBean.setKey(key);
        // 关键字签署时会对整体pdf文档进行搜索，故设置签署页码无效
        // posBean.setPosPage("1");
        // 签署位置X坐标，以关键字所在位置为原点进行偏移，默认值为0，控制横向移动距离，单位为px
        posBean.setPosX(130);
        // 签署位置Y坐标，以关键字所在位置为原点进行偏移，默认值为0，控制纵向移动距离，单位为px
        posBean.setPosY(5);
        // 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述
        posBean.setWidth(100);
        return posBean;
    }


    /***
     * 关键字定位签署的PosBean
     */
    public static PosBean setKeyPosBeanConfirm1(String key) {
        PosBean posBean = new PosBean();
        // 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。
        posBean.setPosType(1);
        // 关键字签署时不可空 */
        posBean.setKey(key);
        // 关键字签署时会对整体pdf文档进行搜索，故设置签署页码无效
        // posBean.setPosPage("1");
        // 签署位置X坐标，以关键字所在位置为原点进行偏移，默认值为0，控制横向移动距离，单位为px
        posBean.setPosX(110);
        // 签署位置Y坐标，以关键字所在位置为原点进行偏移，默认值为0，控制纵向移动距离，单位为px
        posBean.setPosY(5);
        // 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述
        posBean.setWidth(100);
        return posBean;
    }

    /**
     * 合同提交接口
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> submitContract(String userName, String applyNum,String faceImageUrl, String idCardUrl){
        ApplyContract personalInfo = applyContractRepository.findByApplyNum(applyNum);
        JointContract jointContract = jointContractRepository.findByApplyNum(applyNum);
        GuaranteeContract guaranteeContract = guaranteeContractRepository.findByApplyNum(applyNum);
        //如果有共申人判断是否已经签约
        if(personalInfo != null && !ContractSignStatus.SUBMIT.code().equals(personalInfo.getState())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同提交失败，主贷人未签约"), HttpStatus.OK);
        }
//
//        //如果有共申人判断是否已经签约
//        if(jointContract != null && !ContractSignStatus.SUBMIT.code().equals(jointContract.getState())){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同提交失败，共申人未签约"), HttpStatus.OK);
//        }
//        //如果有担保人判断是否已经签约
//        if(guaranteeContract != null && !ContractSignStatus.SUBMIT.code().equals(guaranteeContract.getState())){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同提交失败，担保人未签约"), HttpStatus.OK);
//        }
//        ApplicantContractSignDto applicantContractSignDto = new ApplicantContractSignDto(personalInfo);
//        //调用确认函文档保全服务
//        boolean confirmationFlag = EviDocHelper.eviDoc(applicantContractSignDto, 0);
//        if(!confirmationFlag){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同提交失败，确认函文档保全错误"), HttpStatus.OK);
//        }
//        //调用合同文档保全服务
//        boolean contractFlag = EviDocHelper.eviDoc(applicantContractSignDto, 1);
//        if(!contractFlag){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同提交失败，合同文档保全错误"), HttpStatus.OK);
//        }
//        //调用抵押合同文档保全服务
//        boolean mortgageContractFlag = EviDocHelper.eviDoc(applicantContractSignDto, 2);
//        if(!mortgageContractFlag){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同提交失败，抵押合同保全错误"), HttpStatus.OK);
//        }
//        //调用还款计划表文档保全服务
//        boolean repaymentPlanFlag = EviDocHelper.eviDoc(applicantContractSignDto, 3);
//        if(!repaymentPlanFlag){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同提交失败，还款计划表文档保全错误"), HttpStatus.OK);
//        }
//        //调用风险告知书文档保全服务
//        boolean riskNotification = EviDocHelper.eviDoc(applicantContractSignDto, 1);
//        if(!riskNotification){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同提交失败，风险告知书保全错误"), HttpStatus.OK);
//        }
//
//        SignFileUrlDto signFileUrlDto = new SignFileUrlDto();
//        signFileUrlDto.setApplyNum(applyNum);
//        signFileUrlDto.setLeaseContract(personalInfo.getContactSignedPdf());
//        signFileUrlDto.setDeliveryReceitp(personalInfo.getConfirmationSignedPdf());
//        signFileUrlDto.setMortgageContract(personalInfo.getMortgageContractSignedPdf());
//        signFileUrlDto.setRepaymentPlan(personalInfo.getRepaymentPlanSignedPdf());
//        signFileUrlDto.setRiskNotification(personalInfo.getRiskNotificationSignedPdf());
//        CoreResult codeResult = new CoreResult();
//        try {
//            //提交到主系统
//            String result = coreSystemInterface.signAontractTo("SignAontractTo", signFileUrlDto);
//            logger.info("SignAontractTo={}", result);
//            codeResult = objectMapper.readValue(result, CoreResult.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
//        }
//        if("true".equals(codeResult.getResult().getIsSuccess())){
//            personalInfo.setSubmitTime(new Date());
//            personalInfo.setSubmitUser(userName);
//            personalInfo.setSubmitState(ContractSignStatus.SUBMIT.code());
//            applyContractRepository.save(personalInfo);
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
//        }
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }



    /**
     * 合同签约历史
     * @param userName 用户名
     * @return
     */
    public ResponseEntity<Message> getSignHistory(String userName, String condition){
        if(condition == null){
            condition = "";
        }
        List<ApplyContract> applyContractList = applyContractRepository.findBySubmitUserAndCondition(userName, CommonUtils.likePartten(condition));
        if(applyContractList == null || applyContractList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<ApplyContractLocalInfoDto> dtos = new ArrayList();
        ApplyContractLocalInfoDto dto;
        for(ApplyContract item : applyContractList){
            dto = new ApplyContractLocalInfoDto();
            dto.setName(item.getName());
            dto.setCreateUser(item.getSubmitUser());
            dto.setApplyNum(item.getApplyNum());
            dto.setCreateTime(DateUtils.getStrDate(item.getCreateTime(),DateUtils.simpleDateFormatyyyyMMdd));
            dtos.add(dto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dtos), HttpStatus.OK);
    }



    /**
     * 签约退回，重新签约
     * @param applyNum 用户名
     * @return
     */
    public ResponseEntity<Message> signBack(String applyNum){
        ApplyContract applyContract = applyContractRepository.findByApplyNum(applyNum);
        if(applyContract == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        //更新状态为未签约
        applyContract.setState(ContractSignStatus.NEW.code());
        applyContract.setSubmitState(ContractSignStatus.NEW.code());
        applyContract.setSignStatus(ContractSignStatus.NEW.code());
        applyContractRepository.save(applyContract);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, "退回签约成功", applyNum), HttpStatus.OK);
    }




}
