//package com.tm.wechat.service.gxb;
//
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
///**
// * Created by huzongcheng on 2017/7/28.
// * 百度地图请求接口
// */
//@FeignClient(name = "baiduMapInterface", url = "${request.baiduUrl}")
//public interface BaiduMapInterface {
//
//    @RequestMapping(value = "/geocoder/v2/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    String getLonLatByAddress(@RequestParam("address") String address,
//                              @RequestParam("output") String output,
//                              @RequestParam("ret_coordtype") String ret_coordtype,
//                              @RequestParam("ak") String ak);
//
//}
