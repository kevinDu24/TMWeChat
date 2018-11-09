package com.tm.wechat.service;

import com.tm.wechat.dto.message.Message;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/10/14.
 */
//@FeignClient(name = "companyFileInterface", url = "http://222.73.56.22/companyFiles")
@FeignClient(name = "companyFileInterface", url = "${request.adminServerUrl}")
public interface CompanyFileInterface {

    @RequestMapping(value = "/companyFiles", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getCompanyFiles(
            @RequestHeader("authorization") String auth,
            @RequestHeader("Header-Param") String headerParam,
            @RequestParam(value = "type") Integer type,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size);

    @RequestMapping(value = "/companyFiles/{companyFileId}/download", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    void download(
            @RequestHeader("authorization") String auth,
            @PathVariable(value = "companyFileId") Long companyFileId);
}
