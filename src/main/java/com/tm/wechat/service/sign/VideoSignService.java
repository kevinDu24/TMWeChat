package com.tm.wechat.service.sign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.dao.sign.VideoSignAccountRepository;
import com.tm.wechat.dao.sign.VideoSignRecordRepository;
import com.tm.wechat.domain.VideoSignAccount;
import com.tm.wechat.domain.VideoSignRecord;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.result.CoreResult;
import com.tm.wechat.dto.result.XftmAppServiceResult;
import com.tm.wechat.dto.sign.VideoSignAccountDto;
import com.tm.wechat.dto.sign.VideoSignStateDto;
import com.tm.wechat.dto.sysUser.SysUserInfoDto;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pengchao on 2018/8/28.
 */
@Service
public class VideoSignService {

    @Autowired
    CoreSystemInterface coreSystemInterface;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    VideoSignRecordRepository videoSignRecordRepository;

    @Autowired
    VideoSignAccountRepository videoSignAccountRepository;

    @Autowired
    XftmAppInterFace xftmAppInterFace;

    private static final Logger logger = LoggerFactory.getLogger(VideoSignService.class);


    /**
     * 查询是否需要视频面签
     *
     * @return
     */
    public ResponseEntity<Message> iSvisaInterview(String applyNum) {
        if (CommonUtils.isNull(applyNum)) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
        }
        //提交到主系统
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.iSvisaInterview("ISvisaInterview", applyNum);
            logger.info(applyNum+"iSvisaInterviewResult={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            VideoSignStateDto videoSignStateDto = new VideoSignStateDto();
            if(CommonUtils.isNull(codeResult.getSfxymq())){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到是否需要签约结果"), HttpStatus.OK);
            }
            videoSignStateDto.setSfxymq(codeResult.getSfxymq());
            videoSignStateDto.setSignState(codeResult.getSignState());
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, videoSignStateDto), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统："+codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }

    /**
     * 工行面签报告提交
     * @param videoSignAccountDto
     * @return
     */
    public ResponseEntity<Message> submitVisaInterview(VideoSignAccountDto videoSignAccountDto, String userName) {
        logger.info("videoSignAccountDto={}", JSONObject.fromObject(videoSignAccountDto).toString(2));
        if (CommonUtils.isNull(videoSignAccountDto.getApplyNum()) || CommonUtils.isNull(videoSignAccountDto.getSignNum())) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
        }
        //提交到主系统
        CoreResult codeResult = new CoreResult();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            videoSignAccountDto.setUserName(userName);
            videoSignAccountDto.setCreateTime(simpleDateFormat.format(new Date()));
            String coreResult = coreSystemInterface.submitVisaInterview("SignedReportSubmitted",videoSignAccountDto);
            logger.info("submitVisaInterviewResult={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统："+codeResult.getResult().getResultMsg()), HttpStatus.OK);
    }


    /**
     * 获取视频签约排队数量
     * 0：开始签约，1：签约结束
     * @return
     */
    public ResponseEntity<Message> getSignCount() {
        List<VideoSignRecord> videoSignRecordList = videoSignRecordRepository.findByState("0");
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, videoSignRecordList.size()), HttpStatus.OK);
    }

    /**
     *排队数量减一
     * @param applyNum
     * @param userName
     * @return
     */
    public ResponseEntity<Message> subtractionCount(String applyNum, String userName, String signAccount) {
        if(!CommonUtils.isNull(signAccount)){
            //更新账号为未使用状态
            VideoSignAccount videoSignAccount = videoSignAccountRepository.findTop1BySignAccount(signAccount);
            if(videoSignAccount == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到面签账号!"), HttpStatus.OK);
            }
            videoSignAccount.setState("0");
            videoSignAccount.setUpdateTime(new Date());
            videoSignAccountRepository.save(videoSignAccount);
        }
        //保存签约记录
        VideoSignRecord videoSignRecord = videoSignRecordRepository.findTop1ByApplyNumOrderByUpdateTimeDesc(applyNum);
        VideoSignRecord newVideoRecord = new VideoSignRecord();
        if(videoSignRecord != null){
            videoSignRecord.setSignAccount(signAccount);
            videoSignRecord.setUpdateTime(new Date());
            videoSignRecord.setUpdateUser(userName);
            videoSignRecord.setState("1");
            videoSignRecord.setEndTime(new Date());
            videoSignRecordRepository.save(videoSignRecord);
        } else {
            newVideoRecord.setSignAccount(signAccount);
            newVideoRecord.setUpdateTime(new Date());
            newVideoRecord.setState("1");
            newVideoRecord.setCreateTime(new Date());
            newVideoRecord.setCreateUser(userName);
            newVideoRecord.setApplyNum(applyNum);
            newVideoRecord.setEndTime(new Date());
            videoSignRecordRepository.save(newVideoRecord);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 排队数量加一
     * @param applyNum
     * @param userName
     * @return
     */
    public ResponseEntity<Message> addCount(String applyNum, String userName, String signAccount) {
        if(!CommonUtils.isNull(signAccount)) {
            //更新账号为正在使用状态
            VideoSignAccount videoSignAccount = videoSignAccountRepository.findTop1BySignAccount(signAccount);
            if (videoSignAccount == null) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到面签账号!"), HttpStatus.OK);
            }
            videoSignAccount.setState("1");
            videoSignAccount.setUpdateTime(new Date());
            videoSignAccountRepository.save(videoSignAccount);
        }
        //保存签约记录
        VideoSignRecord videoSignRecord = videoSignRecordRepository.findTop1ByApplyNumOrderByUpdateTimeDesc(applyNum);
        VideoSignRecord newVideoRecord = new VideoSignRecord();
        if(videoSignRecord != null){
            videoSignRecord.setSignAccount(signAccount);
            videoSignRecord.setUpdateTime(new Date());
            videoSignRecord.setUpdateUser(userName);
            videoSignRecord.setState("0");
            videoSignRecordRepository.save(videoSignRecord);
        } else {
            newVideoRecord.setSignAccount(signAccount);
            newVideoRecord.setState("0");
            newVideoRecord.setCreateTime(new Date());
            newVideoRecord.setUpdateTime(new Date());
            newVideoRecord.setCreateUser(userName);
            newVideoRecord.setApplyNum(applyNum);
            videoSignRecordRepository.save(newVideoRecord);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 查询面签账号
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getSignAccount(String userName) {
        String masterAccount = "";
        XftmAppServiceResult codeResult = new XftmAppServiceResult();
        //获取主账号
        try {
            String coreResult = xftmAppInterFace.queryMasterAccount(userName);
            logger.info("queryMasterAccountResult={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, XftmAppServiceResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getSuccess())) {
            JSONArray expressInfoDtoList = JSONArray.fromObject(codeResult.getObj());
            if (expressInfoDtoList == null || expressInfoDtoList.isEmpty()) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到签约账号信息"), HttpStatus.OK);
            }
            JSONObject jsonObject=JSONObject.fromObject(expressInfoDtoList.get(0));
            SysUserInfoDto sysUserInfoDto = (SysUserInfoDto)JSONObject.toBean(jsonObject, SysUserInfoDto.class);
            masterAccount = sysUserInfoDto.getXTJGDM();
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询主账号失败"), HttpStatus.OK);
        }
        //查询该主账号所有签约账号
        List<VideoSignAccount> videoSignAccountList = videoSignAccountRepository.findBySystemUserNameOrderByUpdateTimeAsc(masterAccount);
        if(videoSignAccountList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到面签账号"), HttpStatus.OK);
        }
        //如果有可用账号返回
        for (VideoSignAccount account : videoSignAccountList){
            if("0".equals(account.getState()) || CommonUtils.isNull(account.getState())){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, account), HttpStatus.OK);
            }
        }
        //如果没有返回时间最久的那个
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, videoSignAccountList.get(0)), HttpStatus.OK);
    }

    /**
     * 根据用户名获取主账号
     * @param userName
     * @return
     */
    public ResponseEntity<Message> queryMasterAccount(String userName) {
        if (CommonUtils.isNull(userName)) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
        }
        XftmAppServiceResult codeResult = new XftmAppServiceResult();
        try {
            String coreResult = xftmAppInterFace.queryMasterAccount(userName);
            logger.info("queryMasterAccountResult={}", coreResult);
            codeResult = objectMapper.readValue(coreResult, XftmAppServiceResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getSuccess())) {
            JSONArray expressInfoDtoList = JSONArray.fromObject(codeResult.getObj());
            Object o = expressInfoDtoList.get(0);
            JSONObject jsonObject=JSONObject.fromObject(o);
            SysUserInfoDto sysUserInfoDto = (SysUserInfoDto)JSONObject.toBean(jsonObject, SysUserInfoDto.class);
            if (expressInfoDtoList == null || expressInfoDtoList.isEmpty()) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到签约账号信息"), HttpStatus.OK);
            } else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, sysUserInfoDto.getXTJGDM()), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getMsg()), HttpStatus.OK);
        }
    }
}
