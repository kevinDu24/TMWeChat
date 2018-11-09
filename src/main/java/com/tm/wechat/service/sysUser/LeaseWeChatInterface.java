package com.tm.wechat.service.sysUser;

import com.tm.wechat.domain.WzApplyInfo;
import com.tm.wechat.dto.approval.AppUserBasicInfoDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.sysUser.PushOrderDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by pengchao on 2016/11/21.
 */

@FeignClient(name = "leaseWeChatInterface", url = "${request.wechatServerUrl}")
public interface LeaseWeChatInterface {

    @RequestMapping(value = "/pushModel/share", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> pushOrder(@RequestBody PushOrderDto pushOrderDto);



    @RequestMapping(value = "/pushModel/appUserInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> pushUserInfo(@RequestBody AppUserBasicInfoDto appUserBasicInfoDto);

    @RequestMapping(value = "/apply/getApplyInfoList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getApplyInfoList(@RequestBody List<String> uniqueMarkList);

    @RequestMapping(value = "/icsoc/userInfo", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Object getUserInfo(@RequestParam(value = "openid") String openid, @RequestParam(value = "lang") String lang);


    /**
     * 根据身份证id获取到微众预审批记录
     * @param idCard
     * @return
     */
    @RequestMapping(value = "/apply/getwzApplyInfoByIdCard",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getwzApplyInfoByIdCard(@RequestParam(value = "idCard") String idCard);
}
