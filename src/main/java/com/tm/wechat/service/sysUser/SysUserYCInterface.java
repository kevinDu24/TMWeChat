package com.tm.wechat.service.sysUser;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pengchao on 2017/3/7.
 */
@FeignClient(name = "sysUserInterface", url = "${request.coreServerYCUrl}")
public interface SysUserYCInterface {
    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String updateSysUserInfo(@RequestParam(value = ".url", required = false) String url,
                             @RequestParam(value = "operator", required = false) String operator,
                             @RequestParam(value = "timestamp", required = false) String timestamp,
                             @RequestParam(value = "sign", required = false) String sign,
                             @RequestParam(value = "TXURL", required = false) String TXURL,
                             @RequestParam(value = "XTYHYX", required = false) String XTYHYX,
                             @RequestParam(value = "XTYHSJ", required = false) String XTYHSJ);

    @RequestMapping(value = "/lytmtjapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String uploadCreditMsg(@RequestParam(value = ".url", required = false) String url,
                           @RequestParam(value = "list", required = false) String list);
}
