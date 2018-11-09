package com.tm.wechat.controller.utils;


import com.tm.wechat.utils.FaceIdHttpsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by LEO on 16/10/8.
 */
@RequestMapping("/tenure")
@RestController
public class FunctionController {

    @Autowired
    private FaceIdHttpsUtil faceIdHttpsUtil;

    private static final Logger logger = LoggerFactory.getLogger(FunctionController.class);


    /**
     * 身份证ocr识别
     * @return
     */
    @RequestMapping(value = "/ocrIdCard",method = RequestMethod.POST)
    public Object ocrIdCard(MultipartFile image){
        try {
            return faceIdHttpsUtil.ocrIdCard(image);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * face++验证接口
     * @return
     */
    @RequestMapping(value = "/verify",method = RequestMethod.POST)
    public Object verify(MultipartFile image, MultipartFile image_best, @RequestParam Map<String,String> map){
        try {
            if(image == null && image_best != null){
                return faceIdHttpsUtil.verify(image_best, "2", map);
            } else if(image_best == null && image != null){
                return faceIdHttpsUtil.verify(image, "1", map);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
