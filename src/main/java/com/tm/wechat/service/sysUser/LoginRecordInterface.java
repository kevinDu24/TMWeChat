package com.tm.wechat.service.sysUser;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.sysUser.ContactDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by pengchao on 2016/11/21.
 */

@FeignClient(name = "loginRecordInterface", url = "${request.adminServerUrl}")
public interface LoginRecordInterface {
    @RequestMapping(value = "/tm/saveLoginInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> saveLoginInfo(@RequestHeader("authorization") String auth,
                                        @RequestParam(value = "userName") String userName,
                                        @RequestParam(value = "lat") Double lat,
                                        @RequestParam(value = "lon") Double lon,
                                        @RequestParam(value = "address") String address,
                                        @RequestParam(value = "companyName") String companyName
    );

    @RequestMapping(value = "/tm/uploadContacts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> uploadContacts(@RequestHeader("authorization") String auth,
                                           @RequestBody List<ContactDto> contacts,
                                           @RequestParam(value = "userName") String userName
    );

}
