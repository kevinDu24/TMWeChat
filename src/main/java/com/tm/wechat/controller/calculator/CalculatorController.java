package com.tm.wechat.controller.calculator;

import com.tm.wechat.dto.calculator.CalculatorRecDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * 计算器
 * Created by LEO on 16/9/12.
 */
@RestController
@RequestMapping(value = "/calculators")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> calculator(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                              @RequestBody CalculatorRecDto calculatorRecDto, Principal user){
        return calculatorService.calculator(calculatorRecDto, user.getName(), headerParam);
    }

    @CrossOrigin(origins="*")
    @RequestMapping(value = "/web",method = RequestMethod.POST)
    public ResponseEntity<Message> calculatorForWeb(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                    @RequestBody CalculatorRecDto calculatorRecDto){
        return calculatorService.calculator(calculatorRecDto, "website", headerParam);
    }

    /**
     * 获取计算历史记录
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/records", method = RequestMethod.GET)
    public ResponseEntity<Message> getCalculatorRecords(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                        @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
                                                        Integer page, Integer size, Principal user){
        return calculatorService.getCalculatorRecords(startDate, endDate, page, size, user.getName(), headerParam);
    }
}
