package com.tm.wechat.service.financeProduct;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 16/10/28.
 */
@FeignClient(name = "financeProductInterface", url = "${request.coreServerUrl}")
public interface FinanceProductInterface {
    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getFinancePlans(@RequestParam(value = ".url") String url,
                        @RequestParam(value = "operator") String operator,
                        @RequestParam(value = "timestamp") String timestamp,
                        @RequestParam(value = "sign") String sign,
                        @RequestParam(value = "BACXMC") String BACXMC);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getFinanceProduct(@RequestParam(value = ".url") String url,
                        @RequestParam(value = "operator") String operator,
                        @RequestParam(value = "timestamp") String timestamp,
                        @RequestParam(value = "sign") String sign,
                             @RequestParam(value = "cpxlid") String cpxlid);

    /**
     * 查询产品方案
     * @param url
     * @param operator
     * @param timestamp
     * @param sign
     * @param BACXMC
     * @param BACPDL
     * @return
     */
    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getProductCase(@RequestParam(value = ".url") String url,
                          @RequestParam(value = "operator") String operator,
                          @RequestParam(value = "timestamp") String timestamp,
                          @RequestParam(value = "sign") String sign,
                          @RequestParam(value = "BACXMC") String  BACXMC,
                          @RequestParam(value = "BACPDL") String BACPDL);

}
