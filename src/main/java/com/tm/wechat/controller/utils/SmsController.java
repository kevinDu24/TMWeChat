package com.tm.wechat.controller.utils;

import com.tm.wechat.config.AccountProperties;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.sms.SmsMessage;
import com.tm.wechat.service.SmsInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

/**
 * Created by LEO on 16/11/3.
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsInterface smsInterface;

    @Autowired
    private AccountProperties accountProperties;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Message> getSmsList(String operator, Integer page, Integer size){
        return smsInterface.getSmsList(accountProperties.getAuth(), operator, page, size);
    }

    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
    public ResponseEntity<Message> sendMsg(@RequestBody SmsMessage smsMessage){
        return smsInterface.sendMsg(accountProperties.getAuth(), smsMessage);
    }

    @RequestMapping(value = "/smsMessages", method = RequestMethod.GET)
    public ResponseEntity<Message> smsMessage(String terminalId){
        return smsInterface.smsMessage(accountProperties.getAuth(), terminalId);
    }
}
