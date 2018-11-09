package com.tm.wechat.controller.yc;

import com.tm.wechat.dto.calculator.yc.CalculateReqDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.car.yc.CalculatorYCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by HJYang on 2016/12/3.
 */
@RestController
@RequestMapping(value = "/calculator")
public class CalculatorYCController {
    @Autowired
    private CalculatorYCService calculatorYCService;

    @CrossOrigin(origins="*")
    @RequestMapping(value = "/yc",method = RequestMethod.POST)
    public ResponseEntity<Message> calculatorForWeb(@RequestBody CalculateReqDto calculateReqDto){
        return calculatorYCService.calculator(calculateReqDto, "website");
    }
}
