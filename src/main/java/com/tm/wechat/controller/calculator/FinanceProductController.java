package com.tm.wechat.controller.calculator;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.financeProduct.FinanceProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

/**
 * 计算器
 * Created by LEO on 16/9/13.
 */
@RestController
@RequestMapping(value = "financeProducts")
public class FinanceProductController {

    @Autowired
    private FinanceProductService financeProductService;


    /**
     * 获取融资方案(非核心服务器接口)
     * @return
     */
    @CrossOrigin(origins="*")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Message> getFinancePlans(){
        return financeProductService.getFinancePlans();
    }

    /**
     * 获取融资产品大类(核心服务器)
     * @param user 当前登录用户
     * @return
     */
    @RequestMapping(value = "/core", method = RequestMethod.GET)
    public ResponseEntity<Message> getFinancePlans(String carType, Principal user){
        return financeProductService.getFinancePlans(carType, user.getName());
    }

    /**
     * 获取产品方案
     * @param carType 车型
     * @param typeId 产品大类ID
     * @param user 当前登录用户
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/productCase", method = RequestMethod.GET)
    public  ResponseEntity<Message> getProductCase(@RequestParam String carType, @RequestParam String typeId, Principal user) throws IOException {
        return  financeProductService.getProductCase(carType,typeId, user.getName());
    }


    /**
     * 获取融资产品详情
     * @param financeProductId 融资产品ID
     * @param user 当前登录用户
     * @return
     */
    @RequestMapping(value = "/{financeProductId}", method = RequestMethod.GET)
    public ResponseEntity<Message> getFinanceProduct(@PathVariable String financeProductId, Principal user){
        return financeProductService.getFinanceProduct(financeProductId, user.getName());
    }
}
