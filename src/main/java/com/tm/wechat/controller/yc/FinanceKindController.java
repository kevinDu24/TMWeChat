package com.tm.wechat.controller.yc;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.financeProduct.yc.FinanceKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by HJYang on 2016/12/1.
 */
@RestController
@RequestMapping(value = "financeKind")
public class FinanceKindController {

    @Autowired
    private FinanceKindService financeKindService;

    @CrossOrigin(origins="*")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Message> getFinancePlans(){
        return financeKindService.getFinancePlans();
    }
}
