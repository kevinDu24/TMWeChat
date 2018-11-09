package com.tm.wechat.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/8/25.
 */
@FeignClient(name = "TM", url = "${request.coreServerUrl}")
public interface TMInterface {

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String addUserWx(@RequestParam(value = ".url", required = false) String url,
                      @RequestParam(value = "param", required = false) String param);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String addUserFromCode(@RequestParam(value = ".url", required = false) String url,
                           @RequestParam(value = "xtczmc", required = false) String xtczmc,
                           @RequestParam(value = "xtczmm", required = false) String xtczmm,
                           @RequestParam(value = "xtyhsj", required = false) String xtyhsj,
                           @RequestParam(value = "code", required = false) String code);

    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getSysUsers(@RequestParam(value = ".url", required = false) String url,
                              @RequestParam(value = "operator", required = false) String operator,
                              @RequestParam(value = "sign", required = false) String sign,
                              @RequestParam(value = "timestamp", required = false) String timestamp);

    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryForGet(@RequestParam(value = ".url", required = false) String url,
                       @RequestParam(value = "baczlb", required = false) String type,
                       @RequestParam(value = "bacjdm", required = false) String userName,
                       @RequestParam(value = "baqsrq", required = false) String startDate,
                       @RequestParam(value = "bajsrq", required = false) String endDate,
                       @RequestParam(value = "operator", required = false) String operator,
                       @RequestParam(value = "timestamp", required = false) String timestamp,
                       @RequestParam(value = "sign", required = false) String sign,
                       @RequestParam(value = "basqbh", required = false) String basqbh,

                       //查询合同还款计划明细表
                       @RequestParam(value = "basqxm", required = false) String basqxm,
                       @RequestParam(value = "bazjhm", required = false) String bazjhm
    );


    @RequestMapping(value = "/lyapi.htm!", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryForPost(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "operator", required = false) String operator,
                        @RequestParam(value = "timestamp", required = false) String timestamp,
                        @RequestParam(value = "sign", required = false) String sign,
                        @RequestParam(value = "basqbh", required = false) String basqbh,

                        //查询合同还款计划列表
                        @RequestParam(value = "BAKHMC", required = false) String bakhmc,
                        @RequestParam(value = "BAKSSJ", required = false) String bakssj,
                        @RequestParam(value = "BAJSSJ", required = false) String bajssj,
                        @RequestParam(value = "BAHKZT", required = false) String bahkzt,
                        @RequestParam(value = "BAPAGE", required = false) String bapage,

                        //3.1.	相询合同还款计划列表
                        @RequestParam(value = "BASQZT", required = false) String basqzt
    );


    @RequestMapping(value = "/lytmtjapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryForTmtj(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "userId", required = false) String userId,

                        /**
                        * 1.1	统计量按年/月查询
                        * url:http://happyleasing.cn/TMZL/app/tmtjapi.htm!?.url=qryStatByYearOrMonth&userId=admin&type=2&userLevel=81&year=2016&month=0
                        */
                       @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "userLevel", required = false) String userLevel,
                       @RequestParam(value = "year", required = false) String year,
                       @RequestParam(value = "month", required = false) String month,
                        /**
                         * 统计量按时间段查询
                         * localhost:8081/TM/tj/query?url=qryStatByTimeSlot&type=2&userLevel=81&beginTime=20160101&endTime=20170101
                         */
                       @RequestParam(value = "beginTime", required = false) String startDate,
                       @RequestParam(value = "endTime", required = false) String endDate
    );

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String addUserWx(@RequestParam(value = ".url", required = false) String url,
                     @RequestParam(value = "timestamp", required = false) String timestamp,
                     @RequestParam(value = "sign", required = false) String sign,
                     @RequestParam(value = "xTCZDM", required = false) String xTCZDM,  // 用户代码（新增）
                     @RequestParam(value = "xTCZMC", required = false) String xTCZMC, // 用户名称（新增）
                     @RequestParam(value = "pWD1", required = false) String pWD1,// 用户密码（新增）
                     @RequestParam(value = "xTJGDM", required = false) String xTJGDM,// APP操作人
                     @RequestParam(value = "xTBMMC", required = false) String xTBMMC, // 经销商部门
                     @RequestParam(value = "xTYHSJ", required = false) String xTYHSJ,// 用户手机
                     @RequestParam(value = "xTCZRQ", required = false) String xTCZRQ, // 操作日期
                     @RequestParam(value = "xTCZSJ", required = false) String xTCZSJ,// 操作时间
                     @RequestParam(value = "xTCZRY", required = false) String xTCZRY); // 操作员

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String deleteUserWx(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "timestamp", required = false) String timestamp,
                        @RequestParam(value = "sign", required = false) String sign,
                        @RequestParam(value = "XTCZDM", required = false) String XTCZDM,  // 用户代码（新增）
                        @RequestParam(value = "XTCZRY", required = false) String XTCZRY); // 操作员


    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String banUserWx(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "timestamp", required = false) String timestamp,
                        @RequestParam(value = "Sign", required = false) String sign,
                        @RequestParam(value = "xtczdm", required = false) String xtczdm,  // 用户代码
                        @RequestParam(value = "xtczry", required = false) String xtczry); // 操作员

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String updateUserWx(@RequestParam(value = ".url", required = false) String url,
                     @RequestParam(value = "timestamp", required = false) String timestamp,
                     @RequestParam(value = "sign", required = false) String sign,
                     @RequestParam(value = "XTCZDM", required = false) String XTCZDM,  // 用户代码（新增）
                     @RequestParam(value = "NEWPWD", required = false) String NEWPWD, // 修改后密码
                     @RequestParam(value = "XTCZRY", required = false) String XTCZRY); // 操作员

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String userWxList(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "timestamp", required = false) String timestamp,
                        @RequestParam(value = "sign", required = false) String sign,
                        @RequestParam(value = "XTCZDM", required = false) String XTCZDM);  // 用户代码（新增）

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getSysUserRole(@RequestParam(value = ".url", required = false) String url,
                       @RequestParam(value = "operator", required = false) String operator,
                       @RequestParam(value = "sign", required = false) String sign,
                       @RequestParam(value = "timestamp", required = false) String timestamp);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getUserInfo(@RequestParam(value = ".url", required = false) String url,
                           @RequestParam(value = "xtczdm", required = false) String xtczdm);
}
