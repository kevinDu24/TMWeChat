package com.tm.wechat.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/9/12.
 */
@FeignClient(name = "wechat", url = "https://api.weixin.qq.com")
public interface WeChatInterface {

    @RequestMapping(value = "/sns/oauth2/access_token", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getAccessToken(@RequestParam(value = "appid") String appid, @RequestParam(value = "secret") String secret, @RequestParam(value = "code") String code, @RequestParam(value = "grant_type") String grant_type);
}
