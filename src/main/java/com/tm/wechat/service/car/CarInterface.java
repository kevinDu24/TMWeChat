package com.tm.wechat.service.car;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/10/28.
 */
@FeignClient(name = "carInterface", url = "${request.coreServerUrl}")
public interface CarInterface {
    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCars(@RequestParam(value = ".url") String url,
                        @RequestParam(value = "operator") String operator,
                        @RequestParam(value = "timestamp") String timestamp,
                        @RequestParam(value = "sign") String sign,
                        @RequestParam(value = "type") Integer type,
                           @RequestParam(value = "bacpid") String bacpid,
                           @RequestParam(value = "balbid") String balbid);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCarDetail(@RequestParam(value = ".url") String url,
                        @RequestParam(value = "operator") String operator,
                        @RequestParam(value = "timestamp") String timestamp,
                        @RequestParam(value = "sign") String sign,
                        @RequestParam(value = "bacxid") String bacxid);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getFinanceCar(@RequestParam(value = ".url") String url,
                        @RequestParam(value = "operator") String operator,
                        @RequestParam(value = "timestamp") String timestamp,
                        @RequestParam(value = "sign") String sign);
}
