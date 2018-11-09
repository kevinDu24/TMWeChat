package com.tm.wechat.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/11/2.
 */
@FeignClient(name = "map", url = "http://apis.map.qq.com/")
public interface MapInterface {
    @RequestMapping(value = "ws/geocoder/v1/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getLocation(@RequestParam(value = "location") String location, @RequestParam(value = "key") String key,
                       @RequestParam(value = "get_poi") String get_poi);
}
