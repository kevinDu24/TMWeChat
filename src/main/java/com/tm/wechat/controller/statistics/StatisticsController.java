package com.tm.wechat.controller.statistics;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value = "/getChildApplyInfoByFpName", method = RequestMethod.GET)
    public ResponseEntity<Message> getChildApplyInfoByFpName(String fpName){
        return statisticsService.getChildApplyInfoByFpName(fpName);
    }
}
