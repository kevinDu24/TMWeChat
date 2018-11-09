package com.tm.wechat.controller;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.overdueRate.OverdueRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by wangbiao on 2016/11/9.
 */
@RestController
@RequestMapping("/overdueRate")
public class OverdueRateController {

    @Autowired
    private OverdueRateService overdueRateService;

    /**
     * 按日期查询逾期率统计
     * @param type 类型：1.按年查询 2.按月查询 3.按日查询
     * @param startDate 开始日期 按年查询：2016 按月查询201601 按日查询20160101
     * @param endDate 结束日期 按年查询：2016 按月查询201601 按日查询20160101
     * @param levelId 区域id
     * @param principal 当前登录用户
     * @return
     */
    @RequestMapping(value = "/getOverDueRateByDate", method = RequestMethod.GET)
    public ResponseEntity<Message> getOverDueRateByDate(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                        @RequestParam("type") int type,
                                                        @RequestParam("startDate") String startDate,
                                                        @RequestParam("endDate") String endDate,
                                                        @RequestParam("levelId") int levelId,
                                                        Principal principal) {
        return overdueRateService.getOverDueRateByDate(type, startDate, endDate, levelId, principal.getName(), headerParam);
    }

    /**
     * 按区域查询逾期率统计
     * @param date 查询日期，如20160101
     * @param levelId 区域id
     * @param principal 当前登录用户
     * @return
     */
    @RequestMapping(value = "/getOverDueRateByArea", method = RequestMethod.GET)
    public ResponseEntity<Message> getOverDueRateByArea(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                        @RequestParam("date") String date,
                                                        @RequestParam("levelId") int levelId,
                                                        Principal principal) {
        return overdueRateService.getOverDueRateByArea(date, levelId, principal.getName(), headerParam);
    }

}
