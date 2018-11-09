package com.tm.wechat.service;

import com.tm.wechat.dto.baidu.ShortUrlDto;
import com.tm.wechat.dto.sysUser.UserAddDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pengchao on 2018/7/20.
 */

@FeignClient(name = "baiduShortUrl", url = "http://dwz.cn")
public interface BaiduShortUrlInterface {
    //上级开设下级
    @RequestMapping(value = "/admin/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getShortUrl(@RequestBody ShortUrlDto shortUrlDto);
}
