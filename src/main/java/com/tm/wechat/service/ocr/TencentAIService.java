package com.tm.wechat.service.ocr;

import com.tm.wechat.config.FileUploadProperties;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.TencentAIInterface;

import com.tm.wechat.utils.AuthMailRecord;
import com.tm.wechat.utils.MailUtils;
import com.tm.wechat.utils.TencentOcrHttpUtil;
import com.tm.wechat.utils.sign.TencentAISignUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengchao on 2018/3/5.
 */
@Service
public class TencentAIService {

    @Autowired
    private FileUploadProperties fileUploadProperties;

    @Autowired
    private TencentAISignUtil tencentAISignUtil;

    @Autowired
    private TencentAIInterface tencentAIInterface;

    @Autowired
    private MailUtils mailUtils;

    /**
     * 明信片扫描
     * @param base64image
     * @return
     */
    public ResponseEntity<Message> postCardOcr(String base64image){
        int time_stamp = (int) (System.currentTimeMillis()/1000);
        String nonce_str = tencentAISignUtil.getRandomString(10);
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("app_id", String.valueOf(tencentAISignUtil.tencentAppId));
        paramMap.put("time_stamp",String.valueOf(time_stamp));
        paramMap.put("nonce_str", nonce_str);
        paramMap.put("image", base64image);
        String sign = "";
        try {
            sign = tencentAISignUtil.getSignature(paramMap);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "签名失败"), HttpStatus.OK);
        }
        paramMap.put("sign",sign);
//        String result = tencentAIInterface.businessCard(tencentAISignUtil.appId, time_stamp, nonce_str, base64image, sign);
        //发送请求
        String result = TencentOcrHttpUtil.httpPost(tencentAISignUtil.bcocrUrl, paramMap);
        JSONObject jsonObject = JSONObject.fromObject(result);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, jsonObject), HttpStatus.OK);
    }

    public ResponseEntity<Message> postCardOcr1(AuthMailRecord authMailRecord){
        try {
            mailUtils.sendAuthMail(authMailRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    public ResponseEntity<Message> driverLicenseOcr(String base64image, String type){
        int time_stamp = (int) (System.currentTimeMillis()/1000);
        String nonce_str = tencentAISignUtil.getRandomString(10);
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("app_id", String.valueOf(tencentAISignUtil.tencentAppId));
        paramMap.put("time_stamp",String.valueOf(time_stamp));
        paramMap.put("nonce_str", nonce_str);
        paramMap.put("image", base64image);
        paramMap.put("type", type);
        String sign = "";
        try {
            sign = tencentAISignUtil.getSignature(paramMap);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "签名失败"), HttpStatus.OK);
        }
        paramMap.put("sign",sign);
//        String result = tencentAIInterface.driverLicenseOcr(tencentAISignUtil.tencentAppId, time_stamp, nonce_str, base64image, sign, type);
        //发送请求
        String result = TencentOcrHttpUtil.httpPost(tencentAISignUtil.driverLicenseOcrUrl, paramMap);
        JSONObject jsonObject = JSONObject.fromObject(result);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, jsonObject), HttpStatus.OK);
    }

    public ResponseEntity<Message> faceCosmetic(String base64image, String cosmetic){
        int time_stamp = (int) (System.currentTimeMillis()/1000);
        String nonce_str = tencentAISignUtil.getRandomString(10);
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("app_id", String.valueOf(tencentAISignUtil.tencentAppId));
        paramMap.put("time_stamp",String.valueOf(time_stamp));
        paramMap.put("nonce_str", nonce_str);
        paramMap.put("image", base64image);
        paramMap.put("cosmetic", cosmetic);
        String sign = "";
        try {
            sign = tencentAISignUtil.getSignature(paramMap);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "签名失败"), HttpStatus.OK);
        }
        paramMap.put("sign",sign);
        //发送请求
        String result = TencentOcrHttpUtil.httpPost(tencentAISignUtil.facecosmetic, paramMap);
        JSONObject jsonObject = JSONObject.fromObject(result);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, jsonObject), HttpStatus.OK);
    }


    public ResponseEntity<Message> faceDecoration(String base64image, String decoration){
        System.out.println(base64image.length());
        int time_stamp = (int) (System.currentTimeMillis()/1000);
        String nonce_str = tencentAISignUtil.getRandomString(10);
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("app_id", String.valueOf(tencentAISignUtil.tencentAppId));
        paramMap.put("time_stamp",String.valueOf(time_stamp));
        paramMap.put("nonce_str", nonce_str);
        paramMap.put("image", base64image);
        paramMap.put("decoration", decoration);
        String sign = "";
        try {
            sign = tencentAISignUtil.getSignature(paramMap);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "签名失败"), HttpStatus.OK);
        }
        paramMap.put("sign",sign);
        //发送请求
        String result = TencentOcrHttpUtil.httpPost(tencentAISignUtil.facedecoration, paramMap);
        JSONObject jsonObject = JSONObject.fromObject(result);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, jsonObject), HttpStatus.OK);
    }


    public ResponseEntity<Message> faceMerge(String base64image, String model){
        int time_stamp = (int) (System.currentTimeMillis()/1000);
        String nonce_str = tencentAISignUtil.getRandomString(10);
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("app_id", String.valueOf(tencentAISignUtil.tencentAppId));
        paramMap.put("time_stamp",String.valueOf(time_stamp));
        paramMap.put("nonce_str", nonce_str);
        paramMap.put("image", base64image);
        paramMap.put("model", model);
        String sign = "";
        try {
            sign = tencentAISignUtil.getSignature(paramMap);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "签名失败"), HttpStatus.OK);
        }
        paramMap.put("sign",sign);
//        String result = tencentAIInterface.driverLicenseOcr(tencentAISignUtil.tencentAppId, time_stamp, nonce_str, base64image, sign, type);
        //发送请求
        String result = TencentOcrHttpUtil.httpPost(tencentAISignUtil.facemerge, paramMap);
        JSONObject jsonObject = JSONObject.fromObject(result);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, jsonObject), HttpStatus.OK);
    }



}
