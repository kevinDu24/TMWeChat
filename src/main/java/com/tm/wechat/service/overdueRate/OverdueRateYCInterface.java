package com.tm.wechat.service.overdueRate;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pengchao on 2017/3/8.
 */
@FeignClient(name = "overdueRateYCInterface", url = "${request.coreServerYCUrl}")
public interface OverdueRateYCInterface {
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
