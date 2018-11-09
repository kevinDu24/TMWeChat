package com.tm.wechat.service;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.sms.SmsMessage;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/11/3.
 */
@FeignClient(name = "smsInterface", url = "${request.adminServerUrl}")
public interface SmsInterface {
    @RequestMapping(value = "/sms/wechat/smsList", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getSmsList(@RequestHeader("authorization") String auth, @RequestParam(value = "operator") String operator, @RequestParam(value = "page") Integer page, @RequestParam(value = "size") Integer size);

    @RequestMapping(value = "/sms/sendMsg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> sendMsg(@RequestHeader("authorization") String auth, @RequestBody SmsMessage smsMessage);

    @RequestMapping(value = "/sms/smsMessages", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> smsMessage(@RequestHeader("authorization") String auth, @RequestParam(value = "terminalId") String terminalId);
}
