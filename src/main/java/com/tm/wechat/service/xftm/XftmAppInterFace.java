package com.tm.wechat.service.xftm;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pengchao on 2018/7/20.
 */
@FeignClient(name = "XftmApp", url = "http://116.228.224.59:8767/XftmApp")
//@FeignClient(name = "XftmApp", url = "${request.coreServerUrl}")
public interface XftmAppInterFace {

    //获取放款卡物流信息
    @RequestMapping(value = "/getExpress/{courierNumber}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getExpress(@PathVariable(value = "courierNumber") String courierNumber);


    //查询主账号
    @RequestMapping(value = "/queryAccount", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryMasterAccount(@RequestParam(value = "acctNo") String acctNo);
}
