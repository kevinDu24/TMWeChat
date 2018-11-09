package com.tm.wechat.service.requestpayment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.config.VersionProperties;
import com.tm.wechat.dao.SysUserRepository;
import com.tm.wechat.dao.requestpayment.FileInfoRepository;
import com.tm.wechat.dao.requestpayment.InsurancePolicyRepository;
import com.tm.wechat.dao.requestpayment.RequestPaymentRepository;
import com.tm.wechat.dao.sign.ApplyContractRepository;
import com.tm.wechat.domain.FileInfo;
import com.tm.wechat.domain.InsurancePolicy;
import com.tm.wechat.domain.RequestPayment;
import com.tm.wechat.dto.approval.ApprovalCountDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.requestpayment.*;
import com.tm.wechat.dto.result.CoreResult;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
import com.tm.wechat.service.applyOnline.CoreSystemRequestPayoutInterface;
import com.tm.wechat.service.approval.ApprovalService;
import com.tm.wechat.service.sysUser.SysUserService;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.consts.ContractSignStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ChengQC on 2018/9/27.
 */
@Service
public class RequestPaymentNService {

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
    RequestPaymentService requestPaymentService;

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

    @Autowired
    ApprovalService approvalService;


    private static final Logger logger = LoggerFactory.getLogger(RequestPaymentNService.class);



    /**
     * 获取首页任务列表数量
     * @param xtczdm
     * @return
     */
    public ResponseEntity<Message> getHomeListNum(String xtczdm) {
        try {
            //在线申请任务列表数量
            ResponseEntity<Message> approvalCount = approvalService.getApprovalCount(xtczdm);
            ApprovalCountDto approvalCountDto = (ApprovalCountDto) approvalCount.getBody().getData();
            //判断如果为空
            if(approvalCountDto == null){
                approvalCountDto = new ApprovalCountDto();
                approvalCountDto.setApprovalListCount(0);
                approvalCountDto.setBackListCount(0);
                approvalCountDto.setPassListCount(0);
                approvalCountDto.setToSubmitListCount(0);
            }
            //邀约，签约，请款 任务列表数量
            String resultStr = coreSystemRequestPayoutInterface.getFpPendingCount(xtczdm);
            JSONObject jsonObject = JSON.parseObject(resultStr);

            //如果主系统返回失败
            if (!jsonObject.getBoolean("success")){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, jsonObject.getString("msg")), HttpStatus.OK);
            }

            HomeListNumDto homeListNumDto = objectMapper.readValue(jsonObject.getString("obj"),HomeListNumDto.class);
            homeListNumDto.setApprovalCountDto(approvalCountDto);

            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, homeListNumDto), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取首页任务列表数量异常"), HttpStatus.OK);
        }

    }

    /**
     * 查询请款详细信息
     * @param applyNum 申请编号
     * @return
     */
    public ResponseEntity<Message> getRequestPaymentInfo(String applyNum) {
        if(applyNum == null || applyNum.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "申请编号为空"), HttpStatus.OK);
        }

        try {
            RequestPaymentDetailDto requestPaymentDetailDto = new RequestPaymentDetailDto();


            //调用主系统接口 获取到请款详细信息
            String resultStr = coreSystemRequestPayoutInterface.getRequestPaymentInfo(applyNum);
            JSONObject jsonObject = JSON.parseObject(resultStr);
            //如果主系统返回失败
            if (!jsonObject.getBoolean("success")){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, jsonObject.getString("msg")), HttpStatus.OK);
            }

            try{
                requestPaymentDetailDto = objectMapper.readValue(jsonObject.getString("obj"),RequestPaymentDetailDto.class);
            }catch (Exception e){
                logger.error("查询请款详细信息 getLocalInfoDetails ， 主系统返回数据解析异常",e);

                //测试数据
                requestPaymentDetailDto.setApplyNum(applyNum);
                requestPaymentDetailDto.setIsReturn("1");
                requestPaymentDetailDto.setReturnReason("信息错误");
                requestPaymentDetailDto.setSingleFileDtos(new ArrayList<>());

                List<SingleItemDto> singleItemDtoList = new ArrayList<>();
                String [] singleItemDtoArray = {"车辆发票号","车辆发票代码","车辆纳税人识别号","车辆发票电话","请款备注"};
                for (int i = 0; i < singleItemDtoArray.length; i++) {
                    SingleItemDto singleItemDto = new SingleItemDto();
                    singleItemDto.setName(singleItemDtoArray[i]);
                    singleItemDto.setValue("主系统返回数据解析异常");
                    if("请款备注".equals(singleItemDtoArray[i])){
                        singleItemDto.setRequired("0");
                    }else{
                        singleItemDto.setRequired("1");
                    }
                    singleItemDtoList.add(singleItemDto);
                }
                requestPaymentDetailDto.setItemList(singleItemDtoList);

            }

            //查询请款需要上传哪些文件
            CoreResult codeResult = null;
            try{
                //调用主系统
                String coreResult = coreSystemInterface.getPleaseDocument("GetPleaseDocument", applyNum);
                if(coreResult != null){ codeResult = objectMapper.readValue(coreResult, CoreResult.class); }
            }catch (IOException e){
                e.printStackTrace();
                logger.error("查询请款详细信息 getLocalInfoDetails ， 主系统接口 获取需要传哪些文件 GetPleaseDocument 异常",e);
            }
            //主系统接口调用成功查询文件是否都已经上传
            if("true".equals(codeResult.getResult().getIsSuccess())){
                //主系统返回的需要上传的文件类型列表
                List<GetPleaseDocumentDto> resultList = codeResult.getFileList();
                if(!resultList.isEmpty()){
                    //查询太盟宝数据库中当前申请编号上传的所有文件
                    List<FileInfo> fileInfos = fileInfoRepository.findByApplyNum(applyNum);
                    if(!fileInfos.isEmpty()){
                        //遍历主系统返回的文件列表，与太盟宝数据中申请编号上传的所有文件进行对比：是否所有需要上传的文件都已经上传
                        //设置两个数值： 必须上传文件数量，已上传的必须上传文件数量
                        int rquiredfileNum = 0;
                        int uploadRquiredFileNum = 0;
                        for(GetPleaseDocumentDto getPleaseDocumentDto : resultList){
                            //如果是必须传的才进行判断
                            if ("true".equals(getPleaseDocumentDto.getRequired())){
                                rquiredfileNum++;
                                for (FileInfo fileInfo : fileInfos) {
                                    if(getPleaseDocumentDto.getName().equals(fileInfo.getName())){
                                        uploadRquiredFileNum++;
                                        break;
                                    }
                                }
                            }
                        }
                        //如果两个数值相等，说明必须要上传的文件已经全部上传完毕
                        if(rquiredfileNum == uploadRquiredFileNum){
                            requestPaymentDetailDto.setIsAllFileUpload("1");
                        }
                    }
                }
            }

            requestPaymentDetailDto.setApplyNum(applyNum);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, requestPaymentDetailDto), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询请款详细信息异常"), HttpStatus.OK);
        }



    }

    /**
     * 验证车架号返回车辆信息
     * @param vin
     * @return
     */
    public ResponseEntity<Message> getVehicleInfo(String vin) {
        CarInfoDto carInfoDto = null;
        try {

            //调用主系统接口 验证车架号返回车辆信息
            String resultStr = coreSystemRequestPayoutInterface.getVehicleInfo(vin);
            CoreSystemResultDto coreSystemResultDto = objectMapper.readValue(resultStr,CoreSystemResultDto.class);
            if (coreSystemResultDto.getSuccess()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, coreSystemResultDto.getObj()), HttpStatus.OK);
            }else{
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, coreSystemResultDto.getMsg()), HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 首保上传详情
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> getFirstInformation(String applyNum) {
        FirstInformationDto firstInformationDto = null;
        try {
            String resultStr = coreSystemRequestPayoutInterface.getFirstInformation(applyNum);

            JSONObject jsonObject = JSON.parseObject(resultStr);
            //如果主系统返回失败
            if (!jsonObject.getBoolean("success")){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, jsonObject.getString("msg")), HttpStatus.OK);
            }

            try{
                firstInformationDto = objectMapper.readValue(jsonObject.getString("obj"),FirstInformationDto.class);
            }catch (Exception e){
                firstInformationDto = new FirstInformationDto();
                firstInformationDto.setGpsScheme("主系统返回数据解析异常");
                firstInformationDto.setSalesPrice("");
                firstInformationDto.setSn("主系统返回数据解析异常");
                firstInformationDto.setRecordDate("");
                firstInformationDto.setPlateNumber("");
                firstInformationDto.setReturnReason("");
                firstInformationDto.setFileList(new ArrayList<>());
            }

            //查询提交保单需要上传哪些文件
            CoreResult codeResult = null;
            try{
                //调用主系统
                String coreResult = coreSystemInterface.getPleaseDocument("GetPleaseKindOfPolicy", applyNum);
                if(coreResult != null){ codeResult = objectMapper.readValue(coreResult, CoreResult.class); }
            }catch (IOException e){
                e.printStackTrace();
                logger.error("查询请款详细信息 getLocalInfoDetails ， 主系统接口 获取需要传哪些文件 GetPleaseDocument 异常",e);
            }
            //主系统接口调用成功查询文件是否都已经上传
            if("true".equals(codeResult.getResult().getIsSuccess())){
                //主系统返回的需要上传的文件类型列表
                List<GetPleaseDocumentDto> resultList = codeResult.getFileList();
                if(!resultList.isEmpty()){
                    //查询太盟宝数据库中当前申请编号上传的所有文件
                    List<InsurancePolicy> fileInfos = insurancePolicyRepository.findByApplyNum(applyNum);
                    if(!fileInfos.isEmpty()){
                        //遍历主系统返回的文件列表，与太盟宝数据中申请编号上传的所有文件进行对比：是否所有需要上传的文件都已经上传
                        //设置两个数值： 必须上传文件数量，已上传的必须上传文件数量
                        int RquiredfileNum = 0;
                        int uploadRquiredFileNum = 0;
                        for(GetPleaseDocumentDto getPleaseDocumentDto : resultList){
                            //如果是必须传的才进行判断
                            if ("true".equals(getPleaseDocumentDto.getRequired())){
                                RquiredfileNum++;
                                for (InsurancePolicy fileInfo : fileInfos) {
                                    if(getPleaseDocumentDto.getName().equals(fileInfo.getName())){
                                        uploadRquiredFileNum++;
                                        break;
                                    }
                                }
                            }
                        }
                        //如果两个数值相等，说明必须要上传的文件已经全部上传完毕
                        if(RquiredfileNum == uploadRquiredFileNum){
                            firstInformationDto.setIsAllFileUpload("1");
                        }
                    }
                }
            }

            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, firstInformationDto), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 获取保险公司列表
     * @return
     */
    public ResponseEntity<Message> getInsuranceCompanyList(){
        List<InsuranceCompanyInfoDto> insuranceCompanyInfoDtolist = null;
        try {
            String resultStr = coreSystemRequestPayoutInterface.getInsuranceCompanyList();

            JSONObject jsonObject = JSON.parseObject(resultStr);
            //如果主系统返回失败
            if (!jsonObject.getBoolean("success")){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, jsonObject.getString("msg")), HttpStatus.OK);
            }

            try{
                insuranceCompanyInfoDtolist = jsonObject.getJSONArray("obj").toJavaList(InsuranceCompanyInfoDto.class);
            }catch (Exception e){
                logger.error("解析主系统返回数据异常",e);
                insuranceCompanyInfoDtolist = new ArrayList<>();
                insuranceCompanyInfoDtolist.add(new InsuranceCompanyInfoDto("1","主系统返回数据解析异常"));
            }


            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, insuranceCompanyInfoDtolist), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }

    }

    /**
     * 获取商业险保单信息
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> getCommInsurancePolicyInfo(String applyNum) {
        List<CommInsurancePolicyInfoDto> commInsurancePolicyInfoDtoList = new ArrayList<>();
        CommInsurancePolicyInfoDto commInsurancePolicyInfoDto = new CommInsurancePolicyInfoDto();
        try {
            String resultStr = coreSystemRequestPayoutInterface.getCommInsurancePolicyInfo(applyNum);

            JSONObject jsonObject = JSON.parseObject(resultStr);
            //如果主系统返回失败
            if (!jsonObject.getBoolean("success")){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, jsonObject.getString("msg")), HttpStatus.OK);
            }

            try{
                commInsurancePolicyInfoDtoList = jsonObject.getJSONArray("obj").toJavaList(CommInsurancePolicyInfoDto.class);
                if(!commInsurancePolicyInfoDtoList.isEmpty()){
                    commInsurancePolicyInfoDto = commInsurancePolicyInfoDtoList.get(0);
                }
//                commInsurancePolicyInfoDto = objectMapper.readValue(jsonObject.get("obj").toString(), CommInsurancePolicyInfoDto.class);
            }catch (Exception e){
                logger.error("解析主系统返回数据异常",e);
                commInsurancePolicyInfoDto.setInsurancecompanyName("保险公司1");
                commInsurancePolicyInfoDto.setPolicyNumber("BX0001");
                commInsurancePolicyInfoDto.setPracticalAmount("10000.00");
                commInsurancePolicyInfoDto.setReferAmount("15000.00");
                commInsurancePolicyInfoDto.setStartTime("2018-08-11");
                commInsurancePolicyInfoDto.setEndTime("2018-12-11");
                commInsurancePolicyInfoDto.setIsStraightToCompensate("1");
                commInsurancePolicyInfoDto.setStraightToCompensateAmount("100000.00");

            }

            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, commInsurancePolicyInfoDto), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 提交商业险保单信息
     * @param applyNum
     * @param commInsurancePolicyInfoDto    商业险保单信息
     * @return
     */
    public ResponseEntity<Message> submitCommInsurancePolicyInfo(String applyNum, CommInsurancePolicyInfoDto commInsurancePolicyInfoDto) {
        try{
            JSONObject json = JSONObject.parseObject(JSON.toJSONString(commInsurancePolicyInfoDto));
            json.put("applyNum",applyNum);

            JSONObject jsonRequestObject = new JSONObject();
            jsonRequestObject.put("args",json);

            logger.info("submitCommInsurancePolicyInfo={}", jsonRequestObject);
            //调用主系统提交接口
            String resultStr = coreSystemRequestPayoutInterface.submitCommInsurancePolicyInfo(jsonRequestObject.toJSONString());
            logger.info("submitCommInsurancePolicyInfoResult={}", resultStr);
            JSONObject jsonObject = JSON.parseObject(resultStr);
            //如果主系统返回失败
            if (!jsonObject.getBoolean("success")){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, jsonObject.getString("msg")), HttpStatus.OK);
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, jsonObject.getString("msg")), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 获取交强险保单信息
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> getSaliInsurancePolicyInfo(String applyNum) {
        List<SaliIsInsurancePolicyInfoDto> saliIsInsurancePolicyInfoDtos = new ArrayList<>();
        SaliIsInsurancePolicyInfoDto saliIsInsurancePolicyInfoDto = new SaliIsInsurancePolicyInfoDto();
        try {
            String resultStr = coreSystemRequestPayoutInterface.getSaliInsurancePolicyInfo(applyNum);
            logger.info("getSaliInsurancePolicyInfoResult={}", resultStr);
            JSONObject jsonObject = JSON.parseObject(resultStr);
            //如果主系统返回失败
            if (!jsonObject.getBoolean("success")){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, jsonObject.getString("msg")), HttpStatus.OK);
            }

            try{
                saliIsInsurancePolicyInfoDtos = jsonObject.getJSONArray("obj").toJavaList(SaliIsInsurancePolicyInfoDto.class);
                if(!saliIsInsurancePolicyInfoDtos.isEmpty()){
                    saliIsInsurancePolicyInfoDto = saliIsInsurancePolicyInfoDtos.get(0);
                }
//                saliIsInsurancePolicyInfoDto = objectMapper.readValue(jsonObject.get("obj").toString(), SaliIsInsurancePolicyInfoDto.class);
            }catch (Exception e){
                logger.error("解析主系统返回数据异常",e);
                saliIsInsurancePolicyInfoDto = new SaliIsInsurancePolicyInfoDto();

                saliIsInsurancePolicyInfoDto.setInsurancecompanyName("保险公司1");
                saliIsInsurancePolicyInfoDto.setPolicyNumber("BX0001");
                saliIsInsurancePolicyInfoDto.setPracticalAmount("10000.00");
                saliIsInsurancePolicyInfoDto.setReferAmount("15000.00");
                saliIsInsurancePolicyInfoDto.setStartTime("2018-08-11");
                saliIsInsurancePolicyInfoDto.setEndTime("2018-12-11");
                saliIsInsurancePolicyInfoDto.setTaxreferAmount("2000.00");
                saliIsInsurancePolicyInfoDto.setTaxstraightToCompensateAmount("3000.00");

            }

            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, saliIsInsurancePolicyInfoDto), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 提交交强险保单信息
     * @param applyNum
     * @param saliIsInsurancePolicyInfoDto      交强险保单信息
     * @return
     */
    public ResponseEntity<Message> submitSaliInsurancePolicyInfo(String applyNum, SaliIsInsurancePolicyInfoDto saliIsInsurancePolicyInfoDto) {
        try {
            JSONObject json = JSONObject.parseObject(JSON.toJSONString(saliIsInsurancePolicyInfoDto));
            json.put("applyNum",applyNum);

            JSONObject jsonRequestObject = new JSONObject();
            jsonRequestObject.put("args",json);

            logger.info("submitSaliInsurancePolicyInfo={}", jsonRequestObject);
            //调用主系统提交接口
            String resultStr = coreSystemRequestPayoutInterface.submitSaliInsurancePolicyInfo(jsonRequestObject.toJSONString());
            JSONObject jsonObject = JSON.parseObject(resultStr);
            //如果主系统返回失败
            if (!jsonObject.getBoolean("success")){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, jsonObject.getString("msg")), HttpStatus.OK);
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, jsonObject.getString("msg")), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 提交合同请款
     * @param submitRequestPaymentInfoDto
     * @return
     */
    public ResponseEntity<Message> submitRequestPaymentInfoN(SubmitRequestPaymentInfoDto submitRequestPaymentInfoDto,String userName) {
            logger.info("submitRequestPaymentInfoN={}", submitRequestPaymentInfoDto);
            if(submitRequestPaymentInfoDto == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，参数为空"), HttpStatus.OK);
            }

            //提交请款文件检查
            String message = requestPaymentService.submitRequestPaymentInfoCheck("0", submitRequestPaymentInfoDto.getApplyNum());
            if(!CommonUtils.isNull(message)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
            }


            RequestPaymentDetailDto requestPaymentDetailDto = new RequestPaymentDetailDto();
            requestPaymentDetailDto.setApplyNum(submitRequestPaymentInfoDto.getApplyNum());

            //查询项目信息列表
            requestPaymentDetailDto.setItemList(submitRequestPaymentInfoDto.getItemListJsonStr());

            //查询文件信息附件列表
            List<FileInfo> fileInfoList = fileInfoRepository.findByApplyNum(submitRequestPaymentInfoDto.getApplyNum());
            List<SingleFileDto> fileList = new ArrayList<>();
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
            requestPaymentDetailDto.setSingleFileDtos(fileList);


            JSONObject jsonObject = new JSONObject();
            try {

                JSONObject jsonRequestObject = new JSONObject();
                jsonRequestObject.put("applyNum",requestPaymentDetailDto.getApplyNum());
                jsonRequestObject.put("itemList",requestPaymentDetailDto.getItemList());
                jsonRequestObject.put("fileList",requestPaymentDetailDto.getSingleFileDtos());


                //提交请款信息
                String resultStr = coreSystemRequestPayoutInterface.submitRequestPaymentInfo(jsonRequestObject.toJSONString());

                jsonObject = JSON.parseObject(resultStr);

            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }

            //如果主系统返回成功
            if (jsonObject.getBoolean("success")){
                //更新文件信息为提交状态
                RequestPayment requestPayment = requestPaymentRepository.findByApplyNum(requestPaymentDetailDto.getApplyNum());
                if(requestPayment == null){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "保存文件信息失败，未找到订单信息!"), HttpStatus.OK);
                }
                requestPayment.setFileInfoSubmitState(ContractSignStatus.SUBMIT.code());
                requestPayment.setSubmitTime(new Date());
                requestPayment.setSubmitUser(userName);
                requestPayment.setName(submitRequestPaymentInfoDto.getName());
                requestPaymentRepository.save(requestPayment);
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
            }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,jsonObject.getString("msg")), HttpStatus.OK);
    }

    /**
     * 提交首保上传
     * @param firstInformationDto
     * @param userName
     * @return
     */
    public ResponseEntity<Message> submitFirstInformation(FirstInformationDto firstInformationDto, String userName) {
        logger.info("submitFirstInformation={}", firstInformationDto);
        //提交保单文件检查
        String message = requestPaymentService.submitRequestPaymentInfoCheck("1", firstInformationDto.getApplyNum());
        if(!CommonUtils.isNull(message)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
        }
        List<InsurancePolicy> insurancePolicyList = insurancePolicyRepository.findByApplyNum(firstInformationDto.getApplyNum());


        if(insurancePolicyList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，未查询到保单信息"), HttpStatus.OK);
        }
        List<SingleFileDto> fileList = new ArrayList<>();
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

        firstInformationDto.setApplyNum(firstInformationDto.getApplyNum());
        firstInformationDto.setFileList(fileList);

        JSONObject jsonObject = new JSONObject();
        try {

            JSONObject jsonRequestObject = JSONObject.parseObject(JSON.toJSONString(firstInformationDto));

            //提交到主系统
            String resultStr = coreSystemRequestPayoutInterface.submitFirstInformation(jsonRequestObject.toJSONString());
            jsonObject = JSON.parseObject(resultStr);

            logger.debug(resultStr);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }

        //如果主系统返回成功
        if (jsonObject.getBoolean("success")){
            //更新保单信息为已提交状态
            RequestPayment requestPayment = requestPaymentRepository.findByApplyNum(firstInformationDto.getApplyNum());
            if(requestPayment == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "保存保单失败，未找到订单信息!"), HttpStatus.OK);
            }
            requestPayment.setInsuranceInfoSubmitState(ContractSignStatus.SUBMIT.code());
            requestPayment.setSubmitTime(new Date());
            requestPayment.setSubmitUser(userName);
            requestPayment.setName(firstInformationDto.getName());
            requestPaymentRepository.save(requestPayment);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, jsonObject.getString("msg")), HttpStatus.OK);
    }


}

