package com.tm.wechat.service.approval;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;



/**
 * Created by LEO on 17/5/16.
 */
//@FeignClient(name = "approval", url = "http://116.228.224.59:8766/TMZL/")
@FeignClient(name = "approval", url = "${request.coreServerUrl}")
public interface ApprovalInterface {
    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String coreServer(@RequestParam(value = ".url", required = false) String url,
                      @RequestParam(value = "param", required = false) String param);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryApplyStates(@RequestParam(value = ".url", required = false) String url,
                      @RequestParam(value = "applyIdOfMasterList", required = false) String applyIdOfMasterList);

}