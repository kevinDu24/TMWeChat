package com.tm.wechat.service.applyOnline;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pengchao on 2018/6/30.
 */
//@FeignClient(name = "coreSystemInterface", url = "http://116.228.224.60:8788/XFTM_ZL/app/")
//@FeignClient(name = "coreSystemInterface", url = "http://116.228.224.59:8781/XFTM_KF/app")
@FeignClient(name = "coreSystemInterface", url = "${request.coreServerUrl}")
public interface XftmSystemInterface {

    //核实工行放款卡提交
    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String submitVerificationInfo(@RequestParam(value = ".url", required = true) String url,
                                  @RequestParam(value = "userName", required = true) String userName,
                                  @RequestParam(value = "bankCardNum", required = true) String bankCardNum);

    //获取放款卡信息
    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getVerificationInfo(@RequestParam(value = ".url", required = true) String url,
                                  @RequestParam(value = "userName", required = true) String userName,
                                  @RequestParam(value = "bankCardNum", required = true) String bankCardNum);


    //获取放款卡物流信息
    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getExpressInfoByCardNO(@RequestParam(value = ".url", required = true) String url,
                               @RequestParam(value = "cardNo", required = true) String cardNo);

    //获取放款卡物流列表信息
    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String listCardStatusByCondition(@RequestParam(value = ".url", required = true) String url,
                                     @RequestParam(value = "queryType", required = false) String queryType,
                                     @RequestParam(value = "userName", required = false) String userName,
                                     @RequestParam(value = "startTime", required = false) String startTime,
                                     @RequestParam(value = "endTime", required = false) String endTime,
                                     @RequestParam(value = "verificationCount", required = false) String verificationCount,
                                     @RequestParam(value = "condition", required = false) String condition,
                                     @RequestParam(value = "pageIndex", required = false) String pageIndex,
                                     @RequestParam(value = "pageSize", required = false) String pageSize);

}
