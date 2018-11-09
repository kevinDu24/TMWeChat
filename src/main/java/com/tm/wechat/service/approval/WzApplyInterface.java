package com.tm.wechat.service.approval;

import com.tm.wechat.dto.webank.ApplyStateQueryDto;
import com.tm.wechat.dto.webank.FirstApplyDto;
import com.tm.wechat.dto.webank.SignInfoQueryDto;
import com.tm.wechat.dto.webank.SignSubmitDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pengchao on 2018/1/8.
 */
@FeignClient(name = "contract", url = "http://happyleasing.cn/TMZL")
public interface WzApplyInterface {

    @RequestMapping(value = "/WZHttpReturnRep", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String applySubmit(@RequestBody FirstApplyDto firstApplyDto);

    @RequestMapping(value = "/WZHttpReturnRep", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String signSubmit(@RequestBody SignSubmitDto signSubmitDto);

    @RequestMapping(value = "/WZHttpReturnRep", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryApplyState(@RequestBody ApplyStateQueryDto applyStateQueryDto);


    @RequestMapping(value = "/WZHttpReturnRep", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String querySignInfo(@RequestBody SignInfoQueryDto signInfoQueryDto);

}
