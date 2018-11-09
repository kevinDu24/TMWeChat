package com.tm.wechat.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pengchao on 2017/3/6.
 */
@FeignClient(name = "YC", url = "${request.coreServerYCUrl}")
public interface YCInterface {

    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getSysUsers(@RequestParam(value = ".url", required = false) String url,
                       @RequestParam(value = "operator", required = false) String operator,
                       @RequestParam(value = "sign", required = false) String sign,
                       @RequestParam(value = "timestamp", required = false) String timestamp);
}
