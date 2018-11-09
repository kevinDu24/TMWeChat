package com.tm.wechat.service;

import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.dao.RedisRepository;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by LEO on 16/9/12.
 */
@Service
public class WeChatService {

    @Autowired
    private WeChatInterface weChatInterface;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WechatProperties wechatProperties;

    /**
     * 通过code获取微信用户openid
     * @param code
     */
    public void getUserInfo(String code){
        if(StringUtils.isEmpty(code)){
            return;
        }
        String result = weChatInterface.getAccessToken(wechatProperties.getAppid(),
                wechatProperties.getAppsecret(), code, "authorization_code");
    }

    /**
     * 获取微信url签名
     * @param url
     * @return
     */
    public ResponseEntity<Message> getUrlSignature(String url){
//        WxJsapiSignature wxJsapiSignature = (WxJsapiSignature) redisRepository.get(url);
        WxJsapiSignature wxJsapiSignature = null;
        if(wxJsapiSignature == null){
            try {
                wxJsapiSignature = wxMpService.createJsapiSignature(url);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
//            redisRepository.save(url, wxJsapiSignature, 7200);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, wxJsapiSignature), HttpStatus.OK);
    }
}
