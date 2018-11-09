package com.tm.wechat.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tm.wechat.dao.SysUserRepository;
import com.tm.wechat.domain.SysUser;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pengchao on 2018/4/24.
 */
@Service
public class CreateQRCodeService {

    @Autowired
    SysUserRepository sysUserRepository;

    public ResponseEntity<Message> getQRCode(){
        String timeStamp = String.valueOf(new Date().getTime());
        List<SysUser> list = sysUserRepository.getSaller("taimeng");
        String userName = "";
        int width = 300;
        int height = 300;
        String format = "png";
        String content = "http://fwh.xftm.com/#/wx/wzApply?applyCode=applyCodeValue&timeStamp=timeStampValue";
        //定义二维码的参数
        HashMap map = new HashMap();
        //设置编码
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //设置纠错等级
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN, 2);
        String applyQrCode = "";
        for (SysUser sysUser : list){
            userName = sysUser.getXtczdm();
             applyQrCode  = content.replace("applyCodeValue", userName).replace("timeStampValue", timeStamp);
            try {
                //生成二维码
                BitMatrix bitMatrix = new MultiFormatWriter().encode(applyQrCode, BarcodeFormat.QR_CODE, width, height);
                File file = new File("/var/www/html/device/"+ userName + "_" + timeStamp + ".png");
                MatrixToImageWriter.writeToFile(bitMatrix, format, file);
            } catch (WriterException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, applyQrCode), HttpStatus.OK);
    }


    public ResponseEntity<Message> getQRCodeByUserName(String userName){
        String timeStamp = String.valueOf(new Date().getTime());
        List<SysUser> list = sysUserRepository.getSaller("taimeng");
        int width = 300;
        int height = 300;
        String format = "png";
        String content = "http://fwh.xftm.com/#/wx/wzApply?applyCode=applyCodeValue&timeStamp=timeStampValue";
        //定义二维码的参数
        HashMap map = new HashMap();
        //设置编码
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //设置纠错等级
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN, 2);
        String applyQrCode = content.replace("applyCodeValue", userName).replace("timeStampValue", timeStamp);
            try {
                //生成二维码
                BitMatrix bitMatrix = new MultiFormatWriter().encode(applyQrCode, BarcodeFormat.QR_CODE, width, height);
                File file = new File("/var/www/html/device/"+ userName + "_" + timeStamp + ".png");
                MatrixToImageWriter.writeToFile(bitMatrix, format, file);
            } catch (WriterException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, applyQrCode), HttpStatus.OK);
    }
}
