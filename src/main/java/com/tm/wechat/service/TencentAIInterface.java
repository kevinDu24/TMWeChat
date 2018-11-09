package com.tm.wechat.service;

import com.tm.wechat.dto.ocr.PostCardDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by pengchao on 2017/3/6.
 */
@FeignClient(name = "tencent", url = "https://api.ai.qq.com/fcgi-bin/ocr")
public interface TencentAIInterface {

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String businessCardJson(@RequestHeader("Host") String host,
                       @RequestHeader("Authorization") String authorization,
                       @RequestBody List<Object> contacts);


    @RequestMapping(value = "/ocr_bcocr", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    String postCardOcr(@RequestBody PostCardDto postCardDto);


    @RequestMapping(value = "/ocr_bcocr", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String businessCard(
                       @RequestParam(value = "app_id") int app_id,
                       @RequestParam(value = "time_stamp") int time_stamp,
                       @RequestParam(value = "nonce_str") String nonce_str,
                       @RequestParam(value = "image") String image,
                       @RequestParam(value = "sign") String sign);
}
