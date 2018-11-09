package com.tm.wechat.service.requestpayment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.config.VersionProperties;
import com.tm.wechat.dao.SysUserRepository;
import com.tm.wechat.dao.requestpayment.FileInfoRepository;
import com.tm.wechat.dao.requestpayment.InsurancePolicyRepository;
import com.tm.wechat.dao.requestpayment.RequestPaymentRepository;
import com.tm.wechat.dao.sign.ApplyContractRepository;
import com.tm.wechat.domain.ApplyContract;
import com.tm.wechat.domain.FileInfo;
import com.tm.wechat.domain.InsurancePolicy;
import com.tm.wechat.domain.RequestPayment;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.requestpayment.*;
import com.tm.wechat.dto.result.CoreResult;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
import com.tm.wechat.service.applyOnline.CoreSystemRequestPayoutInterface;
import com.tm.wechat.service.sysUser.SysUserService;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.utils.commons.DateUtils;
import com.tm.wechat.consts.ContractSignStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by pengchao on 2018/3/22.
 */
@Service
public class RequestPaymentService {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;

    @Autowired
    VersionProperties versionProperties;

    @Autowired
    SysUserRepository sysUserRepository;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    CoreSystemInterface coreSystemInterface;

    @Autowired
    CoreSystemRequestPayoutInterface coreSystemRequestPayoutInterface;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RequestPaymentRepository requestPaymentRepository;

    @Autowired
    ApplyContractRepository applyContractRepository;

    private static final Logger logger = LoggerFactory.getLogger(RequestPaymentService.class);


    /**
     * 查询请款列表
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
        List<RequestPaymentListDto> resultList = new ArrayList();
        CoreResult codeResult = new CoreResult();
       String coreResult1 = "{\n" +
               "    \"payoutList\": [\n" +
               "        {\n" +
               "            \"applyNum\": \"38156974\",\n" +
               "            \"applyResult\": \"51000\",\n" +
               "            \"applyResultReason\": \"\",\n" +
               "            \"carType\": \"2\",\n" +
               "            \"createTime\": \"20180126\",\n" +
               "            \"name\": \"何珍瑞\",\n" +
               "            \"state\": \"待请款\",\n" +
               "            \"userName\": \"SH006\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"applyNum\": \"38157452\",\n" +
               "            \"applyResult\": \"5100\",\n" +
               "            \"applyResultReason\": \"\",\n" +
               "            \"carType\": \"1\",\n" +
               "            \"createTime\": \"20180414\",\n" +
               "            \"name\": \"zr_20180414_1\",\n" +
               "            \"state\": \"待文件审核\",\n" +
               "            \"userName\": \"SH006\"\n" +
               "        },\n" +
               "                {\n" +
               "            \"applyNum\": \"38154533\",\n" +
               "            \"applyResult\": \"52000\",\n" +
               "            \"applyResultReason\": \"待修改\",\n" +
               "            \"carType\": \"3\",\n" +
               "            \"createTime\": \"20180126\",\n" +
               "            \"name\": \"何珍瑞\",\n" +
               "            \"state\": \"文件审核待修改\",\n" +
               "            \"userName\": \"SH006\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"applyNum\": \"38157454\",\n" +
               "            \"applyResult\": \"61000\",\n" +
               "            \"applyResultReason\": \"\",\n" +
               "            \"carType\": \"4\",\n" +
               "            \"createTime\": \"20180414\",\n" +
               "            \"name\": \"zr_20180414_1\",\n" +
               "            \"state\": \"文件审核通过待保单上传\",\n" +
               "            \"userName\": \"SH006\"\n" +
               "        },\n" +
               "                {\n" +
               "            \"applyNum\": \"38154535\",\n" +
               "            \"applyResult\": \"6100\",\n" +
               "            \"applyResultReason\": \"\",\n" +
               "            \"carType\": \"2\",\n" +
               "            \"createTime\": \"20180126\",\n" +
               "            \"name\": \"何珍瑞\",\n" +
               "            \"state\": \"待保单审核\",\n" +
               "            \"userName\": \"SH006\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"applyNum\": \"38157456\",\n" +
               "            \"applyResult\": \"6200\",\n" +
               "            \"applyResultReason\": \"退回\",\n" +
               "            \"carType\": \"1\",\n" +
               "            \"createTime\": \"20180414\",\n" +
               "            \"name\": \"zr_20180414_1\",\n" +
               "            \"state\": \"保单审核退回\",\n" +
               "            \"userName\": \"SH006\"\n" +
               "        },        {\n" +
               "            \"applyNum\": \"38154537\",\n" +
               "            \"applyResult\": \"63000\",\n" +
               "            \"applyResultReason\": \"\",\n" +
               "            \"carType\": \"2\",\n" +
               "            \"createTime\": \"20180126\",\n" +
               "            \"name\": \"何珍瑞\",\n" +
               "            \"state\": \"审核通过\",\n" +
               "            \"userName\": \"SH006\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"applyNum\": \"38157458\",\n" +
               "            \"applyResult\": \"51100\",\n" +
               "            \"applyResultReason\": \"232222\",\n" +
               "            \"carType\": \"1\",\n" +
               "            \"createTime\": \"20180414\",\n" +
               "            \"name\": \"zr_20180414_1\",\n" +
               "            \"state\": \"文件审核拒绝\",\n" +
               "            \"userName\": \"SH006\"\n" +
               "        },        {\n" +
               "            \"applyNum\": \"38154539\",\n" +
               "            \"applyResult\": \"61100\",\n" +
               "            \"applyResultReason\": \"23333333\",\n" +
               "            \"carType\": \"4\",\n" +
               "            \"createTime\": \"20180126\",\n" +
               "            \"name\": \"何珍瑞\",\n" +
               "            \"state\": \"保单审核拒绝\",\n" +
               "            \"userName\": \"SH006\"\n" +
               "        }" +
               "    ],\n" +
               "    \"result\": {\n" +
               "        \"isSuccess\": \"true\",\n" +
               "        \"resultCode\": \"000\",\n" +
               "        \"resultMsg\": \"成功!\"\n" +
               "    }\n" +
               "}";
        try {
            // 调用主系统获取待请款列表 -- 2018/9/20 16:13 By ChengQiChuan
            String coreResult = coreSystemInterface.forPaymentList("ForPaymentList", userName, beginTime, endTime, condition);
            if(coreResult == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
            }
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            resultList = codeResult.getPayoutList();
            if(resultList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
            }
            //签约状态
            // 这个地方查出来如果没有签约，将会在前端提示无法请款，需要先去签约 -- 2018/9/20 16:19 By ChengQiChuan  TODO 这个地方现在是按照申请编号一个一个的去查，有时间的时候可以优化
            ApplyContract applyContract;
            for(RequestPaymentListDto requestPaymentListDto : resultList){
                applyContract = applyContractRepository.findByApplyNum(requestPaymentListDto.getApplyNum());
                requestPaymentListDto.setSignState("1");
                if(applyContract != null){
                    requestPaymentListDto.setSignState(applyContract.getSubmitState());
                }

                //处理请款状态，如果为1则为之前已退回过
                if("1".equals(requestPaymentListDto.getIsReturn())){
                    //如果目前是待请款的状态
                    if("51000".equals(requestPaymentListDto.getApplyResult())){
                        requestPaymentListDto.setApplyResult("51001");
                        requestPaymentListDto.setState("退回，待请款");
                    }
                    //如果是待保单上传的状态
                    if("61000".equals(requestPaymentListDto.getApplyResult())){
                        requestPaymentListDto.setApplyResult("61001");
                        requestPaymentListDto.setState("退回，待首保上传");
                    }
                }
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);

    }




    /**
     * 查询请款文件所需附件列表
     * @param applyNum 申请编号
     * @return
     */
    public ResponseEntity<Message> getPleaseDocument(String applyNum){
        List<GetPleaseDocumentDto> resultList = new ArrayList();
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.getPleaseDocument("GetPleaseDocument", applyNum);
            if(coreResult == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
            }
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            resultList = codeResult.getFileList();
            FileInfo fileInfo = null;
            if(!resultList.isEmpty()){
                //查询已上传的文件
                // TODO 这个地方在for循环中查询数据库，后期需要修改 -- 2018/9/20 18:35 By ChengQiChuan
                for(GetPleaseDocumentDto getPleaseDocumentDto : resultList){
                    if(getPleaseDocumentDto.getUrlList() == null){
                        getPleaseDocumentDto.setUrlList(new ArrayList<>());
                    }
                    fileInfo = fileInfoRepository.findTop1ByApplyNumAndName(applyNum, getPleaseDocumentDto.getName());
                    if(fileInfo != null){
                        getPleaseDocumentDto.setUploadState(ContractSignStatus.SUBMIT.code());
                        String fileUrls = fileInfo.getFileList();
                        if(!CommonUtils.isNull(fileUrls)){
                            String[] fileUrlArray = fileUrls.split(",");
                            List<String> fileUrlList = Arrays.asList(fileUrlArray);
                            getPleaseDocumentDto.setUrlList(fileUrlList);
                        }
                    }
                }
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);

    }

    //查询文件信息所需附件
    public List<GetPleaseDocumentDto> getFileListInfo(String applyNum){
        List<GetPleaseDocumentDto> resultList = new ArrayList();
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.getPleaseDocument("GetPleaseDocument", applyNum);
            if(coreResult == null){
                return resultList;
            }
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return resultList;
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            resultList = codeResult.getFileList();
        }
        return resultList;
    }

    //查询报单信息所需附件
    public List<GetPleaseDocumentDto> getInsurancePolicy(String applyNum){
        List<GetPleaseDocumentDto> resultList = new ArrayList();
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.getPleaseDocument("GetPleaseKindOfPolicy", applyNum);
            if(coreResult == null){
                return resultList;
            }
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return resultList;
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            resultList = codeResult.getFileList();
        }
        return resultList;
    }

    /**
     * 提交请款文件和保单信息
     * @param type  提交类型 0 提交文件信息  1 提交保单信息
     * @param applyNum  申请编号
     * @return
     */
    public String submitRequestPaymentInfoCheck(String type, String applyNum){
        String message = "";
        //文件信息
        if("0".equals(type)){
            //查询文件信息所需要的附件
            List<GetPleaseDocumentDto> resultList = getFileListInfo(applyNum);
            if(resultList.isEmpty()){
                message = "提交失败，未查询到订单信息";
                return message;
            }
            //查询文件信息附件列表
            List<FileInfo> fileInfoList = fileInfoRepository.findByApplyNum(applyNum);
            if(fileInfoList.isEmpty()){
                message = "请上传文件信息附件";
                return  message;
            }
            String name = "";
            FileInfo fileInfo = null;
            for(GetPleaseDocumentDto getPleaseDocumentDto : resultList){
                //查询所有必须附件
                if("true".equals(getPleaseDocumentDto.getRequired())){
                    name = getPleaseDocumentDto.getName();
                    if(CommonUtils.isNull(name)){
                        message = "获取请款文件信息异常";
                        return message;
                    }
                    fileInfo = fileInfoRepository.findTop1ByApplyNumAndName(applyNum, name);
                    if(fileInfo == null || !ContractSignStatus.SUBMIT.code().equals(fileInfo.getState())){
                        message = "请上传"+ name +"附件";
                        return message;
                    }
    //                for(FileInfo fileInfo : fileInfoList){
    //                    //为提交附件
    //                    if(name.equals(fileInfo.getName()) && !ContractSignStatus.SUBMIT.code().equals(fileInfo.getState())){
    //                        message = "请上传"+ name +"附件";
    //                        return message;
    //                    }
    //                }
                }
            }
        }
        //保单信息
        if("1".equals(type)){
            //查询保单信息所需要的附件
            List<GetPleaseDocumentDto> resultList = getInsurancePolicy(applyNum);
            if(resultList.isEmpty()){
                message = "提交失败，未查询到订单信息";
                return message;
            }
            List<InsurancePolicy> fileInfoList = insurancePolicyRepository.findByApplyNum(applyNum);
            if(fileInfoList.isEmpty()){
                message = "请上传保单信息附件";
                return  message;
            }
            String name = "";
            InsurancePolicy insurancePolicy = null;
            for(GetPleaseDocumentDto getPleaseDocumentDto : resultList){
                //查询所有必须附件
                if("true".equals(getPleaseDocumentDto.getRequired())){
                    name = getPleaseDocumentDto.getName();
                    if(CommonUtils.isNull(name)){
                        message = "获取请款保单信息异常";
                        return message;
                    }
                    //查询保单信息
                    insurancePolicy = insurancePolicyRepository.findTop1ByApplyNumAndName(applyNum, name);
                    if(insurancePolicy == null || !ContractSignStatus.SUBMIT.code().equals(insurancePolicy.getState())){
                        message = "请上传保单"+ name +"附件";
                        return message;
                    }
                }
            }
        }
        return message;
    }




    /**
     * 查询请款保单所需附件列表
     * @param applyNum 申请编号
     * @return
     */
    public ResponseEntity<Message> getPleaseKindOfPolicy(String applyNum){
        List<GetPleaseDocumentDto> resultList = new ArrayList();
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.getPleaseDocument("GetPleaseKindOfPolicy", applyNum);
            if(coreResult == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
            }
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            resultList = codeResult.getFileList();
            InsurancePolicy fileInfo = null;
            if(!resultList.isEmpty()){
                //查询已上传的文件
                for(GetPleaseDocumentDto getPleaseDocumentDto : resultList){
                    if(getPleaseDocumentDto.getUrlList() == null){
                        getPleaseDocumentDto.setUrlList(new ArrayList<>());
                    }
                    fileInfo = insurancePolicyRepository.findTop1ByApplyNumAndName(applyNum, getPleaseDocumentDto.getName());
                    if(fileInfo != null){
                        String fileUrls = fileInfo.getFileList();
                        getPleaseDocumentDto.setUploadState(ContractSignStatus.SUBMIT.code());
                        if(!CommonUtils.isNull(fileUrls)){
                            String[] fileUrlArray = fileUrls.split(",");
                            List<String> fileUrlList = Arrays.asList(fileUrlArray);
                            getPleaseDocumentDto.setUrlList(fileUrlList);
                        }
                    }
                }
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }

    /**
     * 获取SN号
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> getSn(String applyNum){
        CoreResult codeResult = new CoreResult();
        String coreResult = coreSystemInterface.gpsActivationConfirmationJK("GpsActivationConfirmationJK", applyNum);
        logger.info("GpsActivationConfirmationJK={}", coreResult);
        try {
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            //返回SN号
            String sn = codeResult.getResult().getResultMsg();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,null, sn), HttpStatus.OK);
        }else {
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,null, "13456"), HttpStatus.OK);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    /**
     * 获取请款是否已保存
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> getRequestPaymentState(String applyNum){
        RequestPaymentStatusDto requestPaymentStatusDto = new RequestPaymentStatusDto();
        Object result = requestPaymentRepository.getRequestStatus(applyNum);
        if(result == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, requestPaymentStatusDto), HttpStatus.OK);
        }
        Object[] objects = (Object[]) result;
        requestPaymentStatusDto = new RequestPaymentStatusDto(objects);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, requestPaymentStatusDto), HttpStatus.OK);
    }

    /**
     * 提交GPS激活状态
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> submitGpsInfo(String applyNum, String name, String sn, String userName){
        if(sn == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "SN号不能为空"), HttpStatus.OK);
        }
        RequestPayment requestPayment = requestPaymentRepository.findByApplyNum(applyNum);
        if(requestPayment == null){
            RequestPayment newRequestPayment = new RequestPayment();
            newRequestPayment.setApplyNum(applyNum);
            newRequestPayment.setSn(sn);
            newRequestPayment.setName(name);
            newRequestPayment.setCreateUser(userName);
            newRequestPayment.setGpsState(ContractSignStatus.SUBMIT.code());
            requestPaymentRepository.save(newRequestPayment);
        }else {
            requestPayment.setGpsState(ContractSignStatus.SUBMIT.code());
            requestPayment.setSn(sn);
            requestPayment.setName(name);
            requestPayment.setCreateUser(userName);
            requestPaymentRepository.save(requestPayment);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 获取请款文件信息
     * @param applyNum 申请编号
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getFileInfo(String applyNum, String userName) {
        RequestPayment requestPayment = requestPaymentRepository.findByApplyNum(applyNum);
//        if(requestPayment == null || !ContractSignStatus.SUBMIT.code().equals(requestPayment.getGpsState())){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请先完成GPS激活确认"), HttpStatus.OK);
//        }
        List<FileInfo> fileInfo = fileInfoRepository.findByApplyNum(applyNum);
        FileInfoDto fileInfoDto = new FileInfoDto();
        if(fileInfo != null){
            BeanUtils.copyProperties(fileInfo, fileInfoDto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, fileInfoDto), HttpStatus.OK);
    }

    /**
     * 提交请款文件和保单信息
     * @param type 提交类型 0 提交文件信息  1 提交保单信息
     * @param applyNum 申请编号
     * @return
     */
    @Transactional
    public ResponseEntity<Message> submitRequestPaymentInfo(String type, String applyNum, String userName, String name){
        //提交文件信息
        if("0".equals(type)){
            //提交请款文件和保单信息检查
            String message = submitRequestPaymentInfoCheck(type, applyNum);
            if(!CommonUtils.isNull(message)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
            }
            //查询文件信息附件列表
            List<FileInfo> fileInfoList = fileInfoRepository.findByApplyNum(applyNum);
            FileInfoDto fileInfoDto = new FileInfoDto();
            List<SingleFileDto> fileList = new ArrayList<>();
            List<String> urlList = new ArrayList<>();
            if(fileInfoList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败未查询到文件信息"), HttpStatus.OK);
            }
            SingleFileDto singleFileDto;
            for (FileInfo fileInfo : fileInfoList){
                 singleFileDto = new SingleFileDto();
                 singleFileDto.setName(fileInfo.getName());
                 String fileUrls = fileInfo.getFileList();
                 if(!CommonUtils.isNull(fileUrls)){
                    String[] fileUrlArray = fileUrls.split(",");
                    List<String> fileUrlList = Arrays.asList(fileUrlArray);
                    singleFileDto.setUrlList(fileUrlList);
                 }else {
                    singleFileDto.setUrlList(new ArrayList<>());
                 }
                 fileList.add(singleFileDto);
            }
            fileInfoDto.setApplyNum(applyNum);
            fileInfoDto.setFileList(fileList);
            CoreResult codeResult = new CoreResult();
            try {
                //提交到主系统
                String coreResult = coreSystemInterface.requestFileSubmitJK("RequestFileSubmitJK", fileInfoDto);
                logger.info("RequestFileSubmitJK={}", coreResult);
                codeResult = objectMapper.readValue(coreResult, CoreResult.class);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            if("true".equals(codeResult.getResult().getIsSuccess())){
                //更新文件信息为提交状态
                RequestPayment requestPayment = requestPaymentRepository.findByApplyNum(applyNum);
                if(requestPayment == null){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "保存文件信息失败，未找到订单信息!"), HttpStatus.OK);
                }
                requestPayment.setFileInfoSubmitState(ContractSignStatus.SUBMIT.code());
                requestPayment.setSubmitTime(new Date());
                requestPayment.setSubmitUser(userName);
                requestPayment.setName(name);
                requestPaymentRepository.save(requestPayment);
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
        //提交保单信息
        if("1".equals(type)){
            String message = submitRequestPaymentInfoCheck(type, applyNum);
            if(!CommonUtils.isNull(message)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
            }
            List<InsurancePolicy> insurancePolicyList = insurancePolicyRepository.findByApplyNum(applyNum);
            InsurancePolicyDto insurancePolicyDto = new InsurancePolicyDto();
            List<SingleFileDto> fileList = new ArrayList<>();
            List<String> urlList = new ArrayList<>();
            if(insurancePolicyList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，未查询到保单信息"), HttpStatus.OK);
            }
            SingleFileDto singleFileDto;
            for(InsurancePolicy insurancePolicy : insurancePolicyList){
                singleFileDto = new SingleFileDto();
                singleFileDto.setName(insurancePolicy.getName());
                String fileUrls = insurancePolicy.getFileList();
                if(!CommonUtils.isNull(fileUrls)){
                    String[] fileUrlArray = fileUrls.split(",");
                    List<String> fileUrlList = Arrays.asList(fileUrlArray);
                    singleFileDto.setUrlList(fileUrlList);
                }else {
                    singleFileDto.setUrlList(new ArrayList<>());
                }
                fileList.add(singleFileDto);
            }
            insurancePolicyDto.setApplyNum(applyNum);
            insurancePolicyDto.setPolicyList(fileList);
            CoreResult codeResult = new CoreResult();
            try {
                //提交到主系统
                String coreResult = coreSystemInterface.policyInformationSubmissionJK("PolicyInformationSubmissionJK", insurancePolicyDto);
                logger.info("RequestFileSubmitJK={}", coreResult);
                codeResult = objectMapper.readValue(coreResult, CoreResult.class);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            if("true".equals(codeResult.getResult().getIsSuccess())){
                //更新保单信息为已提交状态
                RequestPayment requestPayment = requestPaymentRepository.findByApplyNum(applyNum);
                if(requestPayment == null){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "保存保单失败，未找到订单信息!"), HttpStatus.OK);
                }
                requestPayment.setInsuranceInfoSubmitState(ContractSignStatus.SUBMIT.code());
                requestPayment.setSubmitTime(new Date());
                requestPayment.setSubmitUser(userName);
                requestPayment.setName(name);
                requestPaymentRepository.save(requestPayment);
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交类型出错"), HttpStatus.OK);
    }


    /**
     * 保存请款文件信息
     * @param fileInfoDto
     * @return
     */
    @Transactional
    public ResponseEntity<Message> saveFileInfo(FileInfoDto fileInfoDto, String userName){
        if(CommonUtils.isNull(fileInfoDto.getApplyNum())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "申请编号不可为空"), HttpStatus.OK);
        }
        RequestPayment requestPayment = requestPaymentRepository.findByApplyNum(fileInfoDto.getApplyNum());
//        if(requestPayment == null || !ContractSignStatus.SUBMIT.code().equals(requestPayment.getGpsState())){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请先完成GPS激活确认"), HttpStatus.OK);
//        }
        List<SingleFileDto> fileList = fileInfoDto.getFileList();
        if(fileList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "附件不可为空"), HttpStatus.OK);
        }
        FileInfo fileInfoList = fileInfoRepository.findTop1ByApplyNumAndName(fileInfoDto.getApplyNum(), fileList.get(0).getName());
        FileInfo newFileInfo =null;
        List<FileInfo> newFileInfoList = new ArrayList<>();
        if(fileInfoList != null){
            fileInfoRepository.delete(fileInfoList);
        }
        for(SingleFileDto singleFileDto : fileList){
            newFileInfo = new FileInfo();
            newFileInfo.setApplyNum(fileInfoDto.getApplyNum());
            newFileInfo.setRequired(singleFileDto.getRequired());
            newFileInfo.setName(singleFileDto.getName());
            if(singleFileDto.getUrlList()== null || singleFileDto.getUrlList().isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, singleFileDto.getName() + "附件不可为空"), HttpStatus.OK);
            }
            String fileUrls = null;
            if (singleFileDto.getUrlList() != null && !singleFileDto.getUrlList().isEmpty()) {
                for (int i = 0; i < singleFileDto.getUrlList().size(); i++) {
                   if (i == 0) {
                       fileUrls = singleFileDto.getUrlList().get(0);
                    } else {
                       fileUrls = fileUrls + "," + singleFileDto.getUrlList().get(i);
                    }
                }
                newFileInfo.setFileList(fileUrls);
                newFileInfo.setState(ContractSignStatus.SUBMIT.code());
            }
            newFileInfoList.add(newFileInfo);
        }
        fileInfoRepository.save(newFileInfoList);
        if(requestPayment == null){
            RequestPayment newRequestPayment = new RequestPayment();
            newRequestPayment.setApplyNum(fileInfoDto.getApplyNum());
            newRequestPayment.setCreateUser(userName);
            newRequestPayment.setFileInfoState(ContractSignStatus.SUBMIT.code());
            requestPaymentRepository.save(newRequestPayment);
        }else {
            requestPayment.setFileInfoState(ContractSignStatus.SUBMIT.code());
            requestPaymentRepository.save(requestPayment);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 获取请款保单信息
     * @param applyNum 申请编号
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getInsurancePolicyInfo(String applyNum, String userName) {
        RequestPayment requestPayment = requestPaymentRepository.findByApplyNum(applyNum);
//        if(requestPayment == null || !ContractSignStatus.SUBMIT.code().equals(requestPayment.getGpsState())){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请先完成GPS激活确认"), HttpStatus.OK);
//        }
        List<InsurancePolicy> insurancePolicy = insurancePolicyRepository.findByApplyNum(applyNum);
        InsurancePolicyDto insurancePolicyDto = new InsurancePolicyDto();
        if(insurancePolicy != null){
            BeanUtils.copyProperties(insurancePolicy, insurancePolicyDto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, insurancePolicyDto), HttpStatus.OK);
    }


    /**
     * 保存请款保单信息
     * @param insurancePolicyDto
     * @return
     */
    @Transactional
    public ResponseEntity<Message> saveInsurancePolicyInfo(InsurancePolicyDto insurancePolicyDto, String userName){
        if(CommonUtils.isNull(insurancePolicyDto.getApplyNum())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "申请编号不可为空"), HttpStatus.OK);
        }
        RequestPayment requestPayment = requestPaymentRepository.findByApplyNum(insurancePolicyDto.getApplyNum());
//        if(requestPayment == null || !ContractSignStatus.SUBMIT.code().equals(requestPayment.getGpsState())){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请先完成GPS激活确认"), HttpStatus.OK);
//        }
        List<SingleFileDto> fileList = insurancePolicyDto.getPolicyList();
        if(fileList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "附件不可为空"), HttpStatus.OK);
        }
        InsurancePolicy insurancePolicy = insurancePolicyRepository.findTop1ByApplyNumAndName(insurancePolicyDto.getApplyNum(), fileList.get(0).getName());
        if(insurancePolicy != null){
            insurancePolicyRepository.delete(insurancePolicy);
        }
        InsurancePolicy newFileInfo = null;
        List<InsurancePolicy> newFileInfoList = new ArrayList<>();
        for(SingleFileDto singleFileDto : fileList){
            newFileInfo = new InsurancePolicy();
            newFileInfo.setApplyNum(insurancePolicyDto.getApplyNum());
            newFileInfo.setRequired(singleFileDto.getRequired());
            newFileInfo.setName(singleFileDto.getName());
            if(singleFileDto.getUrlList()== null || singleFileDto.getUrlList().isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, singleFileDto.getName() + "附件不可为空"), HttpStatus.OK);
            }
            String fileUrls = null;
            if (singleFileDto.getUrlList() != null && !singleFileDto.getUrlList().isEmpty()) {
                for (int i = 0; i < singleFileDto.getUrlList().size(); i++) {
                    if (i == 0) {
                        fileUrls = singleFileDto.getUrlList().get(0);
                    } else {
                        fileUrls = fileUrls + "," + singleFileDto.getUrlList().get(i);
                    }
                }
                newFileInfo.setFileList(fileUrls);
                newFileInfo.setState(ContractSignStatus.SUBMIT.code());
            }
            newFileInfoList.add(newFileInfo);
        }
        insurancePolicyRepository.save(newFileInfoList);
        if(requestPayment == null){
            RequestPayment newRequestPayment = new RequestPayment();
            newRequestPayment.setApplyNum(insurancePolicyDto.getApplyNum());
            newRequestPayment.setCreateUser(userName);
            newRequestPayment.setInsuranceInfoState(ContractSignStatus.SUBMIT.code());
            requestPaymentRepository.save(newRequestPayment);
        }else {
            requestPayment.setInsuranceInfoState(ContractSignStatus.SUBMIT.code());
            requestPaymentRepository.save(requestPayment);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 请款历史
     * @param userName 用户名
     * @return
     */
    public ResponseEntity<Message> getRequestPaymentHistory(String userName, String condition){
        if(condition == null){
            condition = "";
        }
        List<RequestPayment> requestPaymentHistory = requestPaymentRepository.findBySubmitUserAndCondition(userName, CommonUtils.likePartten(condition));
        if(requestPaymentHistory == null || requestPaymentHistory.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<RequestPaymentListDto> dtos = new ArrayList();
        RequestPaymentListDto dto;
        for(RequestPayment item : requestPaymentHistory){
            dto = new RequestPaymentListDto();
            dto.setName(item.getName());
            dto.setUserName(item.getSubmitUser());
            dto.setApplyNum(item.getApplyNum());
            dto.setCreateTime(DateUtils.getStrDate(item.getCreateTime(),DateUtils.simpleDateFormatyyyyMMdd));
            dtos.add(dto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dtos), HttpStatus.OK);
    }


}

