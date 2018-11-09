package com.tm.wechat.service.gpsconvention;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.dao.GpsConventionHistoryRepository;
import com.tm.wechat.domain.GpsConventionHistory;
import com.tm.wechat.dto.gpsconvention.*;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.resu.Resul;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.utils.commons.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GPS邀约Service
 * Created by pengchao on 2018/4/9.
 */
@Service
public class GpsConventionService {

    @Autowired
    CoreSystemInterface coreSystemInterface;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GpsConventionHistoryRepository gpsConventionHistoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(GpsConventionService.class);


    /**
     * 获取GPS邀约列表
     * @param userName
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    public ResponseEntity<Message> getGpsList(String userName, String condition, String beginTime, String endTime){
        if(condition == null){
            condition = "";
        }
        if(beginTime == null){
            beginTime = "";
        }
        if(endTime == null){
            endTime = "";
        }
        // 接受主系统结果dto
        GpsConventionDto result = new GpsConventionDto();
        String coreResult = coreSystemInterface.getGpsList("GpsSolicitationList", userName, condition, beginTime, endTime);
        try {
            result = objectMapper.readValue(coreResult, GpsConventionDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        List<GpsConventionListResult> gpsConventionListResults = new ArrayList();
        if("true".equals(result.getResult().getIsSuccess().toString())){
            gpsConventionListResults = result.getGpsYqLyList();
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, result.getResult().getResultMsg()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, gpsConventionListResults), HttpStatus.OK);
    }

    /**
     * 获取省、市、县列表
     * @param type 获取类型 [“0”:所有省份] [“1”:所有当前省的所有城市] [“2”:所有当前市的所有县]
     * @param id 查询参数 [“0”:null] [“1”:省id] [“2”:市id]
     * @return
     */
    public ResponseEntity<Message> getAreaList(String type, String id){
        if(id == null){
            id = "";
        }
        // 接受主系统结果dto
        GpsAreaDto result = new GpsAreaDto();
        String coreResult = coreSystemInterface.getAreaList("GetGPSInstallProvincesAndCities", type, id);
        try {
            result = objectMapper.readValue(coreResult, GpsAreaDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(result.getResult().getIsSuccess().toString())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, result.getGpssfLys()), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, result.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    /**
     * 获取GPS安装品牌、方式
     * @param applyNum
     * @param installProvince
     * @param installProvince
     * @param username
     * @return
     */
    public ResponseEntity<Message> getGpsInfo(String applyNum, String installProvince, String installCity, String username){
        // 接受主系统结果dto
        GpInfoRes result = new GpInfoRes();
        String coreResult = coreSystemInterface.getGpsInfo("GpsBrandInstallationMode", applyNum, installProvince, installCity, username);
        try {
            result = objectMapper.readValue(coreResult, GpInfoRes.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(result.getResult().getIsSuccess().toString())){
            GpInfoDto dto = new GpInfoDto();
            dto.setInstallBrand(result.getInstallBrand());
            dto.setInstallWay(result.getInstallWay());
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, result.getResult().getResultMsg()), HttpStatus.OK);
        }
    }


    /**
     * GPS邀约信息提交
     * @param userName
     * @return
     */
    public ResponseEntity<Message> submitGpsInfo(String userName, GpsSubmitInfoDto submitInfo){
        // 主系统返回结果集
        Resul res = new Resul();
        String result = coreSystemInterface.submitGpsInfo("GpsSolicitationSubmit", submitInfo);
        logger.info("GpsSolicitationSubmit={}", result);
        try {
            res = objectMapper.readValue(result, Resul.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(res.getResult().getIsSuccess().toString())){
            //登录GPS邀约提交历史表
            GpsConventionHistory gpsConventionHistory = new GpsConventionHistory();
            BeanUtils.copyProperties(submitInfo,gpsConventionHistory);
            gpsConventionHistory.setCreateUser(userName);//提交人
            gpsConventionHistory.setUpdateUser(userName);
            gpsConventionHistoryRepository.save(gpsConventionHistory);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, res.getResult().getResultMsg()), HttpStatus.OK);
        }
    }



    /**
     * GPS邀约提交历史记录查询
     * @param userName 登录人
     * @return
     */
    public ResponseEntity<Message> getGpsHistory(String userName, String condition){
        if(condition == null){
            condition = "";
        }
        List<GpsConventionHistory> gpsConventionHistories = gpsConventionHistoryRepository.findByUserAndCondition(userName, CommonUtils.likePartten(condition));
        if(gpsConventionHistories == null || gpsConventionHistories.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<GpsHistoryDto> dtos = new ArrayList();
        GpsHistoryDto dto;
        for(GpsConventionHistory item : gpsConventionHistories){
            dto = new GpsHistoryDto();
            dto.setApplyNum(item.getApplyNum());
            dto.setContactsName(item.getContactsName());
            dto.setCreateUser(item.getCreateUser());
            dto.setCreateTime(DateUtils.getStrDate(item.getCreateTime(),DateUtils.simpleDateFormatyyyyMMdd));
            dtos.add(dto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dtos), HttpStatus.OK);
    }

}
