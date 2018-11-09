package com.tm.wechat.controller;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/11/2.
 */
@RestController
@RequestMapping("/industries")
public class IndustryController {

    @Autowired
    private IndustryService industryService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Message> getIndustryList(){
        return industryService.getIndustryList();
    }
}
