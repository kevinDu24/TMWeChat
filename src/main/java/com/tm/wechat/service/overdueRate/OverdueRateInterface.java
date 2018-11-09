package com.tm.wechat.service.overdueRate;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangbiao on 2016/11/9.
 */
@FeignClient(name = "overdueRateInterface", url = "${request.coreServerUrl}")
public interface OverdueRateInterface {

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getOverDueRateByDate(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "type") int type,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "levelId") int levelId,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getOverDueRateByArea(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "date") String date,
            @RequestParam(value = "levelId") int levelId,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp);


}
