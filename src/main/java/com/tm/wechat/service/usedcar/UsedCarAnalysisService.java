package com.tm.wechat.service.usedcar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.config.AccountProperties;
import com.tm.wechat.dao.*;
import com.tm.wechat.domain.UsedCarAnalysis;
import com.tm.wechat.domain.UsedCarAnalysisFile;
import com.tm.wechat.domain.UsedCarAnalysisRecord;
import com.tm.wechat.dto.approval.PageDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.result.UsedCarAnalysisResultDto;
import com.tm.wechat.dto.result.UsedCarCityListResultDto;
import com.tm.wechat.dto.result.UsedCarEvalPricesDto;
import com.tm.wechat.dto.usedcar.*;
import com.tm.wechat.service.InformationInterface;
import com.tm.wechat.service.sysUser.SysUserService;
import com.tm.wechat.utils.apply.CarInfoSortUtil;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.consts.UsedCarAnalysisStatus;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by pengchao on 2018/7/18.
 */
@Service
public class UsedCarAnalysisService {

    @Autowired
    private UsedCarAnalysisInterface usedCarAnalysisInterface;

    @Autowired
    private UsedCarAnalysisRepository usedCarAnalysisRepository;

    @Autowired
    private UsedCarAnalysisRecordRepository usedCarAnalysisRecordRepository;

    @Autowired
    private UsedCarAnalysisFileRepository usedCarAnalysisFileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarInfoSortUtil carInfoSortUtil;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private AccountProperties accountProperties;

    @Autowired
    private InformationInterface informationInterface;

    private static final Logger logger = LoggerFactory.getLogger(UsedCarAnalysisService.class);


    public ResponseEntity<Message> getAnalysisFileIsMust(){
        String isMust = "false";
        if(redisRepository.get("tmwechat_analysisFileIsMust") != null){
            isMust = (String) redisRepository.get("tmwechat_analysisFileIsMust");
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, isMust), HttpStatus.OK);
    }

    /**
     * 二手车估价
     * @return
     */
    public ResponseEntity<Message> getSpecifiedPriceAnalysis(UsedCarAnalysisDto dto, String userName) {
        if(dto == null || CommonUtils.isNull(dto.getColor()) ||
                CommonUtils.isNull(dto.getModelId()) ||
                CommonUtils.isNull(dto.getZone()) ||
                CommonUtils.isNull(dto.getVin()) ||
                CommonUtils.isNull(dto.getRegDate()) ||
                CommonUtils.isNull(dto.getOperator()) ||
                CommonUtils.isNull(dto.getOperatorPhoneNum()) ||
                dto.getMile() == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全车辆信息"), HttpStatus.OK);
        }
        if(CommonUtils.isNull(dto.getVin()) || dto.getVin().length() != 17){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请检查车架号是否正确"), HttpStatus.OK);
        }
        UsedCarFileDto usedCarFileDto = dto.getUsedCarFileDto();
        String isMust = "false";
        if(redisRepository.get("tmwechat_analysisFileIsMust") != null){
            isMust = (String) redisRepository.get("tmwechat_analysisFileIsMust");
        }
        if("true".equals(isMust)){
            if(usedCarFileDto == null  ||
                    CommonUtils.isNull(usedCarFileDto.getCenterConsole()) ||
                    CommonUtils.isNull(usedCarFileDto.getDrivingLicense()) ||
                    CommonUtils.isNull(usedCarFileDto.getRegistrationPage12()) ||
                    CommonUtils.isNull(usedCarFileDto.getRegistrationPage34()) ||
                    CommonUtils.isNull(usedCarFileDto.getLeftFront45()) ||
                    CommonUtils.isNull(usedCarFileDto.getLeftC()) ||
                    CommonUtils.isNull(usedCarFileDto.getRightRear45()) ||
                    CommonUtils.isNull(usedCarFileDto.getRightAAndRightB()) ||
                    CommonUtils.isNull(usedCarFileDto.getRightC()) ||
                    CommonUtils.isNull(usedCarFileDto.getSeatBelt()) ||
                    CommonUtils.isNull(usedCarFileDto.getDashboard()) ||
                    CommonUtils.isNull(usedCarFileDto.getCenterConsole()) ||
                    CommonUtils.isNull(usedCarFileDto.getTrunk()) ||
                    CommonUtils.isNull(usedCarFileDto.getTrunkFloor()) ||
                    CommonUtils.isNull(usedCarFileDto.getNameplate()) ||
                    CommonUtils.isNull(usedCarFileDto.getEngineCompartment()) ||
                    CommonUtils.isNull(usedCarFileDto.getVin()))
            {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全附件信息"), HttpStatus.OK);
            }
        }

        //校验短信验证码
        if(CommonUtils.isNull(dto.getMessageCode())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "短信验证码不可为空"), HttpStatus.OK);
        }
        String message = sysUserService.checkMsgCode(dto.getOperatorPhoneNum(), dto.getMessageCode());
        if(message != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
        }
        String uniqueMark = UUID.randomUUID().toString().replace("-", "");
        dto.setUniqueMark(uniqueMark);
        logger.info("usedCarAnalysisDto={}", JSONObject.fromObject(dto).toString(2));
        UsedCarAnalysisResultDto codeResult = new UsedCarAnalysisResultDto();
        UsedCarAnalysis usedCarAnalysis = new UsedCarAnalysis(dto);
        usedCarAnalysis.setCreateUser(userName);
        usedCarAnalysis.setStatus(UsedCarAnalysisStatus.UNSUCCESSFUL.code());
        usedCarAnalysisRepository.save(usedCarAnalysis);

        //保存附件信息
        if(usedCarFileDto != null){
            UsedCarAnalysisFile analysisFile = new UsedCarAnalysisFile(usedCarFileDto);
            analysisFile.setAnalysisId(uniqueMark);
            usedCarAnalysisFileRepository.save(analysisFile);
        }
        UsedCarAnalysisRecord usedCarAnalysisRecord = new UsedCarAnalysisRecord();
        try {
            String result = usedCarAnalysisInterface.getSpecifiedPriceAnalysis("getSpecifiedPriceAnalysis", dto.getModelId(), dto.getRegDate(),
                    dto.getMile(),dto.getZone(),CommonUtils.CARTHREEHUNDRED_TOKEN,dto.getColor(),dto.getInterior(),dto.getSurface(),
                    dto.getWorkState(), dto.getTransferTimes(),dto.getMakeDate());
            logger.info("getSpecifiedPriceAnalysis={}", result);
            codeResult = objectMapper.readValue(result, UsedCarAnalysisResultDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        usedCarAnalysisRecord.setAnalysisId(uniqueMark);
        usedCarAnalysisRecord.setStatus(codeResult.getStatus());
        //成功
        if("1".equals(codeResult.getStatus())){
            UsedCarEvalPricesDto usedCarEvalPricesDto = codeResult.getEval_prices();
            if(usedCarEvalPricesDto == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "未获取到评估价格"), HttpStatus.OK);
            }else {
                usedCarAnalysisRecord.setB2b_price(usedCarEvalPricesDto.getB2b_price());
                usedCarAnalysisRecord.setB2c_price(usedCarEvalPricesDto.getB2c_price());
                usedCarAnalysisRecord.setC2b_price(usedCarEvalPricesDto.getC2b_price());
                usedCarAnalysisRecord.setReport_url(codeResult.getReport_url());
                usedCarAnalysisRecordRepository.save(usedCarAnalysisRecord);
                //更新评估状态
                usedCarAnalysis.setStatus(UsedCarAnalysisStatus.SUCCESS.code());
                usedCarAnalysisRepository.save(usedCarAnalysis);
                usedCarEvalPricesDto.setReport_url(codeResult.getReport_url());
                usedCarEvalPricesDto.setAnalysis_id(usedCarAnalysis.getUniqueMark());
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarEvalPricesDto), HttpStatus.OK);
            }
        }else {
            usedCarAnalysisRecord.setError_msg(codeResult.getError_msg());
            usedCarAnalysisRecordRepository.save(usedCarAnalysisRecord);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getError_msg()), HttpStatus.OK);
        }
    }


    /**
     * 二手车估价测试
     * @return
     */
    public ResponseEntity<Message> getSpecifiedPriceAnalysis1(UsedCarAnalysisDto dto, String userName) {
        if(dto == null || CommonUtils.isNull(dto.getColor()) ||
                CommonUtils.isNull(dto.getModelId()) ||
                CommonUtils.isNull(dto.getZone()) ||
                CommonUtils.isNull(dto.getVin()) ||
                CommonUtils.isNull(dto.getRegDate()) ||
                CommonUtils.isNull(dto.getOperator()) ||
                CommonUtils.isNull(dto.getOperatorPhoneNum()) ||
                dto.getMile() == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全车辆信息"), HttpStatus.OK);
        }
        if(CommonUtils.isNull(dto.getVin()) || dto.getVin().length() != 17){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请检查车架号是否正确"), HttpStatus.OK);
        }
        UsedCarFileDto usedCarFileDto = dto.getUsedCarFileDto();
        ResponseEntity<Message> responseEntity = informationInterface.findByIsMust(accountProperties.getAuth(), "true");
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, responseEntity.getBody().getError()), HttpStatus.OK);
        }
//        List<UsedCarAnalysisFileMaintain> usedCarAnalysisFileMaintains = usedCarAnalysisFileMaintainRepository.findByIsMust("true");
        List<Map> usedCarAnalysisFileMaintains = (List<Map>) responseEntity.getBody().getData();
        if(usedCarFileDto == null  && !usedCarAnalysisFileMaintains.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请检查附件是否上传"), HttpStatus.OK);
        }
        if(usedCarFileDto != null){
            String key = "";
            List<String> noSubmits = new ArrayList<>();
            Field[] fields = usedCarFileDto.getClass().getDeclaredFields();
            String fieldName;
            String fieldValue;
            for (Map item : usedCarAnalysisFileMaintains) {
                key = item.get("fileType").toString();
                for (Field field : fields) {
                    fieldName = field.getName();
                    try {
                        field.setAccessible(true);
                        fieldValue = (String) field.get(usedCarFileDto);
                        if (fieldName.equals(key) && CommonUtils.isNull(fieldValue)) {
                            noSubmits.add(item.get("fileName").toString());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            String checkResult = "";
            if(!noSubmits.isEmpty()){
                for (int i = 0; i < noSubmits.size(); i++) {
                    if (i == 0) {
                        checkResult = noSubmits.get(0);
                    } else {
                        checkResult = checkResult + "," + noSubmits.get(i);
                    }
                }
                if(!"".equals(checkResult)){
                    String returnResult = "评估失败！请补全以下附件:" + checkResult;
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, returnResult), HttpStatus.OK);
                }
            }
        }


        //校验短信验证码
        if(CommonUtils.isNull(dto.getMessageCode())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "短信验证码不可为空"), HttpStatus.OK);
        }
        String message = sysUserService.checkMsgCode(dto.getOperatorPhoneNum(), dto.getMessageCode());
        if(message != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
        }
        String uniqueMark = UUID.randomUUID().toString().replace("-", "");
        dto.setUniqueMark(uniqueMark);
        logger.info("usedCarAnalysisDto={}", JSONObject.fromObject(dto).toString(2));
        UsedCarAnalysisResultDto codeResult = new UsedCarAnalysisResultDto();
        UsedCarAnalysis usedCarAnalysis = new UsedCarAnalysis(dto);
        usedCarAnalysis.setCreateUser(userName);
        usedCarAnalysis.setStatus(UsedCarAnalysisStatus.UNSUCCESSFUL.code());
        usedCarAnalysisRepository.save(usedCarAnalysis);

        //保存附件信息
        if(usedCarFileDto != null){
            UsedCarAnalysisFile analysisFile = new UsedCarAnalysisFile(usedCarFileDto);
            analysisFile.setAnalysisId(uniqueMark);
            usedCarAnalysisFileRepository.save(analysisFile);
        }
        UsedCarAnalysisRecord usedCarAnalysisRecord = new UsedCarAnalysisRecord();
        try {
            String result = usedCarAnalysisInterface.getSpecifiedPriceAnalysis("getSpecifiedPriceAnalysis", dto.getModelId(), dto.getRegDate(),
                    dto.getMile(),dto.getZone(),CommonUtils.CARTHREEHUNDRED_TOKEN,dto.getColor(),dto.getInterior(),dto.getSurface(),
                    dto.getWorkState(), dto.getTransferTimes(),dto.getMakeDate());
            logger.info("getSpecifiedPriceAnalysis={}", result);
            codeResult = objectMapper.readValue(result, UsedCarAnalysisResultDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        usedCarAnalysisRecord.setAnalysisId(uniqueMark);
        usedCarAnalysisRecord.setStatus(codeResult.getStatus());
        //成功
        if("1".equals(codeResult.getStatus())){
            UsedCarEvalPricesDto usedCarEvalPricesDto = codeResult.getEval_prices();
            if(usedCarEvalPricesDto == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "未获取到评估价格"), HttpStatus.OK);
            }else {
                usedCarAnalysisRecord.setB2b_price(usedCarEvalPricesDto.getB2b_price());
                usedCarAnalysisRecord.setB2c_price(usedCarEvalPricesDto.getB2c_price());
                usedCarAnalysisRecord.setC2b_price(usedCarEvalPricesDto.getC2b_price());
                usedCarAnalysisRecord.setReport_url(codeResult.getReport_url());
                usedCarAnalysisRecordRepository.save(usedCarAnalysisRecord);
                //更新评估状态
                usedCarAnalysis.setStatus(UsedCarAnalysisStatus.SUCCESS.code());
                usedCarAnalysisRepository.save(usedCarAnalysis);
                usedCarEvalPricesDto.setReport_url(codeResult.getReport_url());
                usedCarEvalPricesDto.setAnalysis_id(usedCarAnalysis.getUniqueMark());
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarEvalPricesDto), HttpStatus.OK);
            }
        }else {
            usedCarAnalysisRecord.setError_msg(codeResult.getError_msg());
            usedCarAnalysisRecordRepository.save(usedCarAnalysisRecord);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getError_msg()), HttpStatus.OK);
        }
    }


    /**
     * 二手车估价（web端）
     * @return
     */
    public ResponseEntity<Message> getSpecifiedPriceAnalysisUnverified(UsedCarAnalysisDto dto, String userName) {
        if(dto == null || CommonUtils.isNull(dto.getColor()) ||
                CommonUtils.isNull(dto.getModelId()) ||
                CommonUtils.isNull(dto.getZone()) ||
                CommonUtils.isNull(dto.getVin()) ||
                CommonUtils.isNull(dto.getRegDate()) ||
                CommonUtils.isNull(dto.getOperator()) ||
                CommonUtils.isNull(dto.getOperatorPhoneNum()) ||
                dto.getMile() == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全信息"), HttpStatus.OK);
        }
        if(CommonUtils.isNull(dto.getVin()) || dto.getVin().length() != 17){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请检查车架号是否正确"), HttpStatus.OK);
        }

        //校验短信验证码
        if(CommonUtils.isNull(dto.getMessageCode())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "短信验证码不可为空"), HttpStatus.OK);
        }
        String message = sysUserService.checkMsgCode(dto.getOperatorPhoneNum(), dto.getMessageCode());
        if(message != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
        }
        String uniqueMark = UUID.randomUUID().toString().replace("-", "");
        dto.setUniqueMark(uniqueMark);
        logger.info("usedCarAnalysisDto={}", JSONObject.fromObject(dto).toString(2));
        UsedCarAnalysisResultDto codeResult = new UsedCarAnalysisResultDto();
        UsedCarAnalysis usedCarAnalysis = new UsedCarAnalysis(dto);
        usedCarAnalysis.setCreateUser(userName);
        usedCarAnalysis.setStatus(UsedCarAnalysisStatus.UNSUCCESSFUL.code());
        usedCarAnalysisRepository.save(usedCarAnalysis);

        UsedCarAnalysisRecord usedCarAnalysisRecord = new UsedCarAnalysisRecord();
        try {
            String result = usedCarAnalysisInterface.getSpecifiedPriceAnalysis("getSpecifiedPriceAnalysis", dto.getModelId(), dto.getRegDate(),
                    dto.getMile(),dto.getZone(),CommonUtils.CARTHREEHUNDRED_TOKEN,dto.getColor(),dto.getInterior(),dto.getSurface(),
                    dto.getWorkState(), dto.getTransferTimes(),dto.getMakeDate());
            logger.info("getSpecifiedPriceAnalysis={}", result);
            codeResult = objectMapper.readValue(result, UsedCarAnalysisResultDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        usedCarAnalysisRecord.setAnalysisId(uniqueMark);
        usedCarAnalysisRecord.setStatus(codeResult.getStatus());
        //成功
        if("1".equals(codeResult.getStatus())){
            UsedCarEvalPricesDto usedCarEvalPricesDto = codeResult.getEval_prices();
            if(usedCarEvalPricesDto == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "未获取到评估价格"), HttpStatus.OK);
            }else {
                usedCarAnalysisRecord.setB2b_price(usedCarEvalPricesDto.getB2b_price());
                usedCarAnalysisRecord.setB2c_price(usedCarEvalPricesDto.getB2c_price());
                usedCarAnalysisRecord.setC2b_price(usedCarEvalPricesDto.getC2b_price());
                usedCarAnalysisRecord.setReport_url(codeResult.getReport_url());
                usedCarAnalysisRecordRepository.save(usedCarAnalysisRecord);
                //更新评估状态
                usedCarAnalysis.setStatus(UsedCarAnalysisStatus.SUCCESS.code());
                usedCarAnalysisRepository.save(usedCarAnalysis);
                usedCarEvalPricesDto.setReport_url(codeResult.getReport_url());
                usedCarEvalPricesDto.setAnalysis_id(usedCarAnalysis.getUniqueMark());
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarEvalPricesDto), HttpStatus.OK);
            }
        }else {
            usedCarAnalysisRecord.setError_msg(codeResult.getError_msg());
            usedCarAnalysisRecordRepository.save(usedCarAnalysisRecord);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getError_msg()), HttpStatus.OK);
        }
    }


    /**
     * 查询二手车评估记录
     * @param size
     * @param page
     * @return
     */
    public ResponseEntity<Message> findUsedCarAnalysisRecord(String userName, Integer size, Integer page, String condition) {
        condition = CommonUtils.likePartten(condition);
        List<Object> dataList = usedCarAnalysisRepository.findUsedCarAnalysisRecord(userName, size, (page -1) * size, condition);
        if(dataList == null || dataList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<UsedCarAnalysisRecordDto> resultList = new ArrayList();
        UsedCarAnalysisRecordDto result;
        Object[] objs;
        for (Object object : dataList) {
            objs = (Object[]) object;
            result = new UsedCarAnalysisRecordDto(objs);
            resultList.add(result);
        }
        PageDto pageDto = new PageDto();
        pageDto.setPage(page);
        pageDto.setSize(size);
        pageDto.setContent(resultList);
        List<Object> resultCount = usedCarAnalysisRepository.findUsedCarAnalysisRecordCount(userName, condition);
        pageDto.setTotalElements(resultCount.size());
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, pageDto), HttpStatus.OK);
    }


    /**
     * 查询评估车辆信息
     * @param unique_mark
     * @return
     */
    public ResponseEntity<Message> findCarInfoByUniqueMark(String unique_mark) {
        UsedCarAnalysis usedCarAnalysis = usedCarAnalysisRepository.findByUniqueMark(unique_mark);
        if(usedCarAnalysis == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        UsedCarAnalysisDto usedCarAnalysisDto = new UsedCarAnalysisDto(usedCarAnalysis);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarAnalysisDto), HttpStatus.OK);
    }


    /**
     * 查询评估附件信息
     * @param unique_mark
     * @return
     */
    public ResponseEntity<Message> findFileInfoByUniqueMark(String unique_mark) {
        UsedCarAnalysisFile usedCarAnalysisFile = usedCarAnalysisFileRepository.findByAnalysisId(unique_mark);
        if(usedCarAnalysisFile == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        UsedCarFileDto usedCarAnalysisDto = new UsedCarFileDto(usedCarAnalysisFile);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarAnalysisDto), HttpStatus.OK);
    }

    /**
     * 查询评估结果信息
     * @param unique_mark
     * @return
     */
    public ResponseEntity<Message> findPriceInfoByUniqueMark(String unique_mark) {
        UsedCarAnalysisRecord usedCarAnalysisRecord = usedCarAnalysisRecordRepository.findByAnalysisId(unique_mark);
        if(usedCarAnalysisRecord == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        UsedCarAnalysisRecordDto usedCarAnalysisRecordDto = new UsedCarAnalysisRecordDto(usedCarAnalysisRecord);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarAnalysisRecordDto), HttpStatus.OK);
    }


    /**
     * VIN识别车型接口
     * @return
     */
    public ResponseEntity<Message> identifyModelByVIN(String vin) {
        logger.info("identifyModelByVINParam={}", vin);
        if(CommonUtils.isNull(vin) || vin.length() != 17){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请检查车架号是否正确"), HttpStatus.OK);
        }
        UsedCarAnalysisResultDto codeResult = new UsedCarAnalysisResultDto();
        try {
            String result = usedCarAnalysisInterface.identifyModelByVIN(vin, CommonUtils.CARTHREEHUNDRED_TOKEN);
            logger.info("identifyModelByVINResult={}", result);
            codeResult = objectMapper.readValue(result, UsedCarAnalysisResultDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        //成功
        if("1".equals(codeResult.getStatus())){
            if(codeResult.getModelInfo() == null || codeResult.getModelInfo().isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "未获取到车型"), HttpStatus.OK);
            }else {
                UsedCarInfoDto usedCarInfoDto = codeResult.getModelInfo().get(0);
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarInfoDto), HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getError_msg()), HttpStatus.OK);
        }
    }

    /**
     * 行驶证OCR
     * @return
     */
    public ResponseEntity<Message> identifyDriverCard(String driveData) {
        if(CommonUtils.isNull(driveData)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "行驶证照片不能为空"), HttpStatus.OK);
        }
        UsedCarAnalysisResultDto codeResult = new UsedCarAnalysisResultDto();
        try {
            OkHttpClient mOkHttpClient = new OkHttpClient();
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("oper", "identifyDriverCard")
                    .addFormDataPart("token", CommonUtils.CARTHREEHUNDRED_TOKEN)
                    .addFormDataPart("driveData", driveData)
                    .build();
            Request request = new Request.Builder()
                    //todo
//                    .url("http://testapi.che300.com/service//common/eval")
                    .url("http://api.che300.com/service//common/eval")
                    .post(requestBody)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            Response response = call.execute();
            String result = response.body().string();
            logger.info("identifyDriverCardResult={}", result);
            codeResult = objectMapper.readValue(result, UsedCarAnalysisResultDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        //成功
        if("1".equals(codeResult.getStatus())){
            if(codeResult.getData() == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "行驶证识别失败"), HttpStatus.OK);
            }else {
                IdentifyDriverCardResultDto identifyDriverCardResultDto = codeResult.getData();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, identifyDriverCardResultDto), HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getError_msg()), HttpStatus.OK);
        }
    }


    /**
     * 获取城市列表
     * @return
     */
    public ResponseEntity<Message> getAllCity() {
        UsedCarCityListResultDto codeResult = new UsedCarCityListResultDto();
        try {
            String result = usedCarAnalysisInterface.getAllCity(CommonUtils.CARTHREEHUNDRED_TOKEN);
            codeResult = objectMapper.readValue(result, UsedCarCityListResultDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        //成功
        if("1".equals(codeResult.getStatus())){
            if(codeResult.getCity_list() == null || codeResult.getCity_list().isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "未获取到城市"), HttpStatus.OK);
            }else {
                List<UsedCarCityDto> carTypePriceDtoList = new ArrayList<>();
                Map<String, List<UsedCarCityListDto>> groupedMap = codeResult.getCity_list().stream().collect(Collectors.groupingBy(UsedCarCityListDto::getProv_name));
                groupedMap.forEach((key, value) -> {
                    UsedCarCityDto usedCarCityDto = new UsedCarCityDto();
                    usedCarCityDto.setGroup(key);
                    usedCarCityDto.setDataRows(value);
                    carTypePriceDtoList.add(usedCarCityDto);
                });
                codeResult.setCityListResult(carTypePriceDtoList);
                codeResult.setCity_list(null);
//                List<UsedCarCityDto> carTypePriceDtoList = carInfoSortUtil.sortProv(codeResult.getCity_list());
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, codeResult), HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getError_msg()), HttpStatus.OK);
        }
    }
}
