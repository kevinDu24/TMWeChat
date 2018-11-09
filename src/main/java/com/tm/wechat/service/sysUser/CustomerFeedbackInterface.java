package com.tm.wechat.service.sysUser;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.sysUser.FeedbackRecDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pengchao on 2016/11/21.
 */

@FeignClient(name = "customerInterface", url = "${request.adminServerUrl}")
public interface CustomerFeedbackInterface {
    @RequestMapping(value = "/feedback/feedback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> addFeedback(@RequestHeader("authorization") String auth, @RequestHeader("Header-Param") String headerParam, @RequestBody FeedbackRecDto feedbackRecDto, @RequestParam(value = "user") String user);

}
