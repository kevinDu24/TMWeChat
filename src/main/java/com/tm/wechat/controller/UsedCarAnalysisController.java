package com.tm.wechat.controller;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.ocr.Base64Dto;
import com.tm.wechat.dto.usedcar.UsedCarAnalysisDto;
import com.tm.wechat.dto.usedcar.UsedCarAnalysisMaintainDto;
import com.tm.wechat.service.usedcar.UsedCarAnalysisMaintainService;
import com.tm.wechat.service.usedcar.UsedCarAnalysisService;
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
 * Created by pengchao on 2018/7/18.
 * 车300
 */
@RestController
@RequestMapping("/usedCarAnalysis")
@PreAuthorize("@permission.isHaveUsedCarAnalysis1Power(authentication.principal.username)")
public class UsedCarAnalysisController {

    @Autowired
    private UsedCarAnalysisService usedCarAnalysisService;

    @Autowired
    private UsedCarAnalysisMaintainService usedCarAnalysisMaintainService;



    private static final Logger logger = LoggerFactory.getLogger(UsedCarAnalysisController.class);


    /**
     * 获取附件是否必传
     * @return
     */
    @RequestMapping(value = "/getAnalysisFileIsMust", method = RequestMethod.GET)
    public ResponseEntity<Message> getSpecifiedPriceAnalysis(){
        try {
            return usedCarAnalysisService.getAnalysisFileIsMust();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取附件是否必传异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     *  添加二手车评估附件维护
     * @return
     */
    @RequestMapping(value = "/addUsedCarAnalysisFileMaintain", method = RequestMethod.POST)
    public ResponseEntity<Message> addUsedCarAnalysisFileMaintain(@RequestBody UsedCarAnalysisMaintainDto usedCarAnalysisMaintainDto, Principal user){
        try {
            return usedCarAnalysisMaintainService.addUsedCarAnalysisFileMaintain(user.getName(), usedCarAnalysisMaintainDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("添加二手车评估附件异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 二手车估价
     * @return
     */
    @RequestMapping(value = "/getSpecifiedPriceAnalysis", method = RequestMethod.POST)
    public ResponseEntity<Message> getSpecifiedPriceAnalysis(@RequestBody UsedCarAnalysisDto usedCarAnalysisDto, Principal user){
        try {
            return usedCarAnalysisService.getSpecifiedPriceAnalysis(usedCarAnalysisDto, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("二手车估价异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 二手车估价（web端）
     * @return
     */
    @RequestMapping(value = "/getSpecifiedPriceAnalysisUnverified", method = RequestMethod.POST)
    public ResponseEntity<Message> getSpecifiedPriceAnalysisUnverified(@RequestBody UsedCarAnalysisDto usedCarAnalysisDto, Principal user){
        try {
            return usedCarAnalysisService.getSpecifiedPriceAnalysisUnverified(usedCarAnalysisDto, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("二手车估价异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     *  VIN识别车型接口
     * @return
     */
    @RequestMapping(value = "/identifyModelByVIN", method = RequestMethod.GET)
    public ResponseEntity<Message> identifyModelByVIN(@RequestParam(required = true) String vin){
        try {
            return usedCarAnalysisService.identifyModelByVIN(vin);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("VIN识别车型异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     *  行驶证OCR
     * @return
     */
    @RequestMapping(value = "/identifyDriverCard", method = RequestMethod.POST)
    public ResponseEntity<Message> identifyDriverCard(@RequestBody Base64Dto driveData){
        try {
            return usedCarAnalysisService.identifyDriverCard(driveData.getDriverCardBase64());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("行驶证识别异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     *  获取城市列表
     * @return
     */
    @RequestMapping(value = "/getAllCity", method = RequestMethod.GET)
    public ResponseEntity<Message> getAllCity(){
        try {
            return usedCarAnalysisService.getAllCity();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取城市列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 查询二手车评估记录
     * @param user
     * @param condition
     * @param size
     * @param page
     * @return
     */
    @RequestMapping(value = "/findUsedCarAnalysisRecord", method = RequestMethod.GET)
    public ResponseEntity<Message> findUsedCarAnalysisRecord(Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition,
                                                             @RequestParam(value = "size", required = true) Integer size, @RequestParam(value = "page", required = true)Integer page){
        try {
            return usedCarAnalysisService.findUsedCarAnalysisRecord(user.getName(), size, page, condition);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询评估记录异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询评估车辆信息
     * @param unique_mark
     * @return
     */
    @RequestMapping(value = "/findCarInfoByUniqueMark", method = RequestMethod.GET)
    public ResponseEntity<Message> findCarInfoByUniqueMark(String unique_mark){
        try {
            return usedCarAnalysisService.findCarInfoByUniqueMark(unique_mark);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询评估车辆信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询评估附件信息
     * @param unique_mark
     * @return
     */
    @RequestMapping(value = "/findFileInfoByUniqueMark", method = RequestMethod.GET)
    public ResponseEntity<Message> findFileInfoByUniqueMark(String unique_mark){
        try {
            return usedCarAnalysisService.findFileInfoByUniqueMark(unique_mark);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询评估附件信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询评估结果信息
     * @param unique_mark
     * @return
     */
    @RequestMapping(value = "/findPriceInfoByUniqueMark", method = RequestMethod.GET)
    public ResponseEntity<Message> findPriceInfoByUniqueMark(String unique_mark){
        try {
            return usedCarAnalysisService.findPriceInfoByUniqueMark(unique_mark);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询评估结果异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }
}
