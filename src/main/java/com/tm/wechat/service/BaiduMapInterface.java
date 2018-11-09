package com.tm.wechat.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/9/18.
 */
@FeignClient(name = "baiduMap", url = "http://api.map.baidu.com")
public interface BaiduMapInterface {

    @RequestMapping(value = "/ag/coord/convert", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getLocation(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to,
                       @RequestParam(value = "x") String x, @RequestParam(value = "y") String y);

    @RequestMapping(value = "/geocoder/v2/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String geoCoder(@RequestParam(value = "ak") String ak,
                    @RequestParam(value = "location") String location, @RequestParam(value = "output") String output,
                    @RequestParam(value = "pois") String pois);
}
