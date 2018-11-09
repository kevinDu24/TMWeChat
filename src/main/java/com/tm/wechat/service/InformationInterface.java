package com.tm.wechat.service;

import com.tm.wechat.dto.message.Message;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/10/10.
 */
@FeignClient(name = "informationInterface", url = "${request.adminServerUrl}")
//@FeignClient(name = "informationInterface", url = "http://localhost:8080")
public interface InformationInterface {

    @RequestMapping(value = "/informations", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getInfos(
            @RequestHeader("Header-Param") String headerParam,
            @RequestParam(value = "type") Integer type,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size);

    @RequestMapping(value = "/informations/{infoId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getInfoDetail(@PathVariable(value = "infoId") Long infoId);

    @RequestMapping(value = "/informations/banners", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getBanners(
            @RequestHeader("Header-Param") String headerParam,
            @RequestParam(value = "size") Integer size);

    @RequestMapping(value = "/appVersions/latest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getAppVersions(
            @RequestHeader("Header-Param") String headerParam,
            @RequestParam(value = "type") Integer type);

    @RequestMapping(value = "/usedCarAnalysis/findByIsMust", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> findByIsMust(
            @RequestHeader("authorization") String auth,
            @RequestParam(value = "isMust") String isMust);
}
