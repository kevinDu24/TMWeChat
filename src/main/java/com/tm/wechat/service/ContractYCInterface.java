package com.tm.wechat.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pengchao on 2017/3/7.
 */
@FeignClient(name = "contract", url = "${request.coreServerYCUrl}")
public interface ContractYCInterface {
    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String query(@RequestParam(value = ".url", required = false) String url,
                 @RequestParam(value = "operator", required = false) String operator,
                 @RequestParam(value = "timestamp", required = false) String timestamp,
                 @RequestParam(value = "sign", required = false) String sign,
                 @RequestParam(value = "basqbh", required = false) String applyNum,

                 //查询合同还款计划列表
                 @RequestParam(value = "BAKHMC", required = false) String bakhmc,
                 @RequestParam(value = "BAKSSJ", required = false) String bakssj,
                 @RequestParam(value = "BAJSSJ", required = false) String bajssj,
                 @RequestParam(value = "BAHKZT", required = false) String bahkzt,
                 @RequestParam(value = "BAPAGE", required = false) Integer bapage,

                 //3.1.	相询合同还款计划列表
                 @RequestParam(value = "BASQZT", required = false) String basqzt,

                 //查询合同还款计划明细表
                 @RequestParam(value = "BASQXM", required = false) String basqxm,
                 @RequestParam(value = "BAZJHM", required = false) String bazjhm
    );

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryApplyNum(@RequestParam(value = ".url", required = false) String url,
                         @RequestParam(value = "operator", required = false) String operator,
                         @RequestParam(value = "timestamp", required = false) String timestamp,
                         @RequestParam(value = "sign", required = false) String sign);
}
