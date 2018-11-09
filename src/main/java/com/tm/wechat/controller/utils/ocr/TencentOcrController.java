package com.tm.wechat.controller.utils.ocr;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.ocr.Base64Dto;
import com.tm.wechat.service.applyOnline.ApplyService;
import com.tm.wechat.service.ocr.TencentAIService;
import com.tm.wechat.utils.AuthMailRecord;
import com.tm.wechat.utils.ImageBase64Util;
import com.tm.wechat.utils.commons.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pengchao on 2018/3/5.
 */
@RestController
@RequestMapping("/tencent")
public class TencentOcrController {

    @Autowired
    TencentAIService tencentAIService;

    private static final Logger logger = LoggerFactory.getLogger(ApplyService.class);

    /**
     * 腾讯明信片扫描
     * @param base64image
     * @return
     */
    @RequestMapping(value = "/postCardOcr", method = RequestMethod.POST)
    public ResponseEntity<Message> postCardOcr(@RequestBody Base64Dto base64image){
        try {
            return tencentAIService.postCardOcr(base64image.getBase64image());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("明信片扫描异常", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/postCardOcr1", method = RequestMethod.POST)
    public ResponseEntity<Message> postCardOcr1(@RequestBody AuthMailRecord authMailRecord){
        try {
            return tencentAIService.postCardOcr1(authMailRecord);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("明信片扫描异常", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/postCardOcrByPath", method = RequestMethod.POST)
    public ResponseEntity<Message> postCardOcr1(String imagePath){
        try {
            return tencentAIService.postCardOcr(ImageBase64Util.getImageStr(imagePath));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("明信片扫描异常", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 驾驶证，行驶证识别
     * @param imagePath
     * @param type
     * @return
     */
    @RequestMapping(value = "/driverLicenseOcr", method = RequestMethod.POST)
    public ResponseEntity<Message> postCardOcr(String imagePath, String type){
        try {
            return tencentAIService.driverLicenseOcr(ImageBase64Util.getImageStr(imagePath), type);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("行驶证扫描异常", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 人脸美妆
     * @param imagePath
     * @param cosmetic
     * @return
     */
    @RequestMapping(value = "/faceCosmetic", method = RequestMethod.POST)
    public ResponseEntity<Message> faceCosmetic(String imagePath, String cosmetic){
        try {
            return tencentAIService.faceCosmetic(ImageBase64Util.getImageStr(imagePath), cosmetic);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("人脸美妆异常", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 人脸变妆
     * @param imagePath
     * @param decoration
     * @return
     */
    @RequestMapping(value = "/faceDecoration", method = RequestMethod.POST)
    public ResponseEntity<Message> faceDecoration(String imagePath, String decoration){
        try {
            return tencentAIService.faceDecoration(ImageBase64Util.getImageStr(imagePath), decoration);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("人脸变妆异常", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 人脸融合
     * @param imagePath
     * @param model
     * @return
     */
    @RequestMapping(value = "/faceMerge", method = RequestMethod.POST)
    public ResponseEntity<Message> faceMerge(String imagePath, String model){
        try {
            return tencentAIService.faceMerge(ImageBase64Util.getImageStr(imagePath), model);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("人脸融合异常", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

}
