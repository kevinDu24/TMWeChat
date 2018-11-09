package com.tm.wechat.controller.utils;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.WeChatService;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/8/26.
 */
@RestController
@RequestMapping("weChats")
public class WeChatController {

    @Autowired
    private WeChatService weChatService;

    @RequestMapping(value = "/push", method = RequestMethod.GET)
    public String eventListener(String signature, String timestamp, String nonce, String echostr){
        return echostr;
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public void getUserInfo(@RequestParam(required = false) String code){
        weChatService.getUserInfo(code);
    }

    /**
     * 获取微信url签名
     * @param url
     * @return
     */
    @RequestMapping(value = "/urlSignature", method = RequestMethod.GET)
    public ResponseEntity<Message> getUrlSignature(String url){
        return weChatService.getUrlSignature(url);
    }
}
