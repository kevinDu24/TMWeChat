package com.tm.wechat.service.usedcar;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pengchao on 2018/7/18.
 */
//@FeignClient(name = "usedCarAnalysisInterface", url = "https://testapi.che300.com/service")
@FeignClient(name = "usedCarAnalysisInterface", url = "https://api.che300.com/service")
public interface UsedCarAnalysisInterface {

    @RequestMapping(value = "/common/eval", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getSpecifiedPriceAnalysis(@RequestParam(value = "oper", required = true) String oper,
                             @RequestParam(value = "modelId", required = true) String modelId,
                             @RequestParam(value = "regDate", required = true) String regDate,
                             @RequestParam(value = "mile", required = true) double mile,
                             @RequestParam(value = "zone", required = true) String zone,
                             @RequestParam(value = "token", required = true) String token,
                             @RequestParam(value = "color", required = true) String color,
                             @RequestParam(value = "interior", required = true) String interior,
                             @RequestParam(value = "surface", required = false) String surface,
                             @RequestParam(value = "workState", required = false) String workState,
                             @RequestParam(value = "transferTimes", required = false) Integer transferTimes,
                             @RequestParam(value = "makeDate", required = false) String makeDate);

    @RequestMapping(value = "/identifyModelByVIN", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String identifyModelByVIN(@RequestParam(value = "vin", required = true) String modelId,
                                     @RequestParam(value = "token", required = true) String token);


    @RequestMapping(value = "/getAllCity", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getAllCity(@RequestParam(value = "token", required = true) String token);


}
