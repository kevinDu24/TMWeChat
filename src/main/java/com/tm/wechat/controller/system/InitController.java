package com.tm.wechat.controller.system;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.system.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/11/2.
 */
@RestController
@RequestMapping("/init")
public class InitController {

    @Autowired
    private InitService initService;

    /**
     * app端首页初始化
     * @param bannerNum
     * @param infoNum
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Message> init(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                        Integer bannerNum, Integer infoNum, Integer noticeNum){
        return initService.init(headerParam, bannerNum, infoNum, noticeNum);
    }



    /**
     * app查询是否能够在线完善申请
     * @return
     */
    @RequestMapping(value = "/getApplyState",method = RequestMethod.GET)
    public ResponseEntity<Message> getApplyState(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam){
        return initService.getApplyState(headerParam);
    }
}
