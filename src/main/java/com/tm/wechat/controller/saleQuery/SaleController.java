package com.tm.wechat.controller.saleQuery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.sale.ApplyRecDto;
import com.tm.wechat.dto.sale.CallBackDto;
import com.tm.wechat.dto.sale.InsuranceCalculateRecDto;
import com.tm.wechat.dto.sale.SaleRec;
import com.tm.wechat.service.system.TMService;
import com.tm.wechat.service.sale.SaleInterface;
import com.tm.wechat.service.sale.SaleService;
import com.tm.wechat.service.sale.SaleYCInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

/**
 * 销售查询
 * Created by PengChao on 16/9/1.
 */
@RestController
@RequestMapping("/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TMService tmService;

    @Autowired
    private  SaleInterface saleInterface;

    @Autowired
    private SaleYCInterface saleYCInterface;

    /**
     * 查询当前用户层级下的所有权限地区
     * @param principal:当前登录的用户信息
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/queryAreaLevel", method = RequestMethod.GET)
    public ResponseEntity<Message> queryAreaLevel(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                  Principal principal) throws IOException {
        CallBackDto areaLevelCallBackDto;
        if("taimeng".equals(JsonPath.from(headerParam).get("systemflag"))){
            areaLevelCallBackDto = objectMapper.readValue(saleInterface.queryAreaLevel(principal.getName(), "qryAreaLevel"), CallBackDto.class);
        }else {
            areaLevelCallBackDto = objectMapper.readValue(saleYCInterface.queryAreaLevel(principal.getName(), "qryAreaLevel"), CallBackDto.class);
        }
        Message message = new Message(MessageType.MSG_TYPE_SUCCESS, areaLevelCallBackDto.getData());
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    /**
     * 根据时间段查询统计量-人员统计报表
     * @param type:查询类别(1代表账号统计，2代表申请量统计，3代表CA人员审核量，4代表合同量统计，5融资额统计
     * @param userLevel:用户层级（选择的地区levelID)
     * @param startDate:日期开始(格式:20150601)
     * @param endDate:日期结束(格式:20150601)
     * @param principal:当前登录的用户信息
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/queryStatisticsByDate", method = RequestMethod.GET)
    public ResponseEntity<Message> queryStatisticsByDate(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                         @RequestParam("type") int type,
                                                         @RequestParam("userLevel") String userLevel,
                                                         @RequestParam("startDate") String startDate,
                                                         @RequestParam("endDate") String endDate,
                                                         @RequestParam(value = "userId",required = false) String userId,
                                                         @RequestParam(value = "queryAll", required = false, defaultValue="是") String queryAll,
                                                         Principal principal) throws IOException {
        userId = (userId == null || "".equals(userId)) ? principal.getName() : userId;
        CallBackDto statistics;
        // 判断是太盟还是亚驰 -- 2018/9/18 18:45 By ChengQiChuan
        if("taimeng".equals(JsonPath.from(headerParam).get("systemflag"))){
            statistics = objectMapper.readValue(saleInterface.queryStatisticsByDate("qryStatByTimeSlot", userId,
                    type, userLevel, startDate, endDate, queryAll), CallBackDto.class);
        }else {
            statistics = objectMapper.readValue(saleYCInterface.queryStatisticsByDate("qryStatByTimeSlot", userId,
                    type, userLevel, startDate, endDate), CallBackDto.class);
        }
        Message message = new Message(MessageType.MSG_TYPE_SUCCESS, statistics.getData());
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    /**
     * 按年查询销售统计-人员统计报表stringify()
     * @param type:查询类别(1代表账号统计，2代表申请量统计，3代表CA人员审核量，4代表合同量统计，5融资额统计
     * @param userLevel:用户层级（选择的地区levelID)
     * @param year:年份(格式:2015)
     * @param principal:当前登录的用户信息
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/queryStatisticsByYear", method = RequestMethod.GET)
    public ResponseEntity<Message> queryStatisticsByYear(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                         @RequestParam("type") int type,
                                                         @RequestParam("userLevel") String userLevel,
                                                         @RequestParam("year") int year,
                                                         Principal principal) throws IOException {
        CallBackDto statistics;
        if("taimeng".equals(JsonPath.from(headerParam).get("systemflag"))){
            statistics = objectMapper.readValue(saleInterface.queryStatisticsByYearOrMonth("qryStatByYearOrMonth", principal.getName(),
                    type, userLevel, year, 0), CallBackDto.class);
        }else {
            statistics = objectMapper.readValue(saleYCInterface.queryStatisticsByYearOrMonth("qryStatByYearOrMonth", principal.getName(),
                    type, userLevel, year, 0), CallBackDto.class);
        }
        Message message = new Message(MessageType.MSG_TYPE_SUCCESS, statistics.getData());
        return new ResponseEntity<Message>(message, HttpStatus.OK);

    }

    /**
     * 按月查询销售统计-人员统计报表
     * @param type:查询类别(1代表账号统计，2代表申请量统计，3代表CA人员审核量，4代表合同量统计，5融资额统计
     * @param userLevel:用户层级（选择的地区levelID)
     * @param year:年份(格式:2015)
     * @param month:月份(格式:1)
     * @param principal:当前登录的用户信息
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/queryStatisticsByMonth", method = RequestMethod.GET)
    public ResponseEntity<Message> queryStatisticsByYear(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                         @RequestParam("type") int type,
                                                         @RequestParam("userLevel") String userLevel,
                                                         @RequestParam("year") int year,
                                                         @RequestParam("month") int month,
                                                         Principal principal) throws IOException {
        CallBackDto statistics;
        if("taimeng".equals(JsonPath.from(headerParam).get("systemflag"))){
            statistics = objectMapper.readValue(saleInterface.queryStatisticsByYearOrMonth("qryStatByYearOrMonth", principal.getName(),
                    type, userLevel, year, month), CallBackDto.class);
        }else {
            statistics = objectMapper.readValue(saleYCInterface.queryStatisticsByYearOrMonth("qryStatByYearOrMonth", principal.getName(),
                    type, userLevel, year, month), CallBackDto.class);
        }
        Message message = new Message(MessageType.MSG_TYPE_SUCCESS, statistics.getData());
        return new ResponseEntity<Message>(message, HttpStatus.OK);

    }

    /**
     * 销售计划查询
     * @param saleRec
     * @return
     */
    @PreAuthorize("@permission.isHplUser(authentication.principal.username, #headerParam)")
    @RequestMapping(value = "/plan", method = RequestMethod.GET)
    public ResponseEntity<Message> querySalePlan(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                 SaleRec saleRec, Principal user){
        return tmService.querySalePlan(headerParam, saleRec, user.getName());
    }

    @PreAuthorize("@permission.isHplUser(authentication.principal.username, #headerParam)")
    @RequestMapping(value = "/newPlan", method = RequestMethod.GET)
    public ResponseEntity<Message> queryNewSalePlan(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                 SaleRec saleRec, Principal user){
        return tmService.queryNewSalePlan(headerParam, saleRec, user.getName());
    }



    /**
     * 查询GPS硬件价格
     * @param user 当前登录的用户
     * @return
     */
    @RequestMapping(value = "/gpsPrice", method = RequestMethod.GET)
    public ResponseEntity<Message> getGpsPrice(Principal user, @RequestParam String typeId, @RequestParam String productCase ){
        return saleService.getGpsPrice(user.getName(),typeId,productCase);
    }

    /**
     * 查询经销商
     * @param user 当前登录的用户
     * @param type 类型：1、FP市场助理  2、FP销售经理
     * @return
     */
    @RequestMapping(value = "/fp", method = RequestMethod.GET)
    public ResponseEntity<Message> getDealers(Principal user, Integer type){
        return  saleService.getDealers(type, user.getName());
    }

    /**
     * 获取回租抵押城市
     * @param user 当前登录的用户
     * @return
     */
    @RequestMapping(value = "/pledgeCities", method = RequestMethod.GET)
    public ResponseEntity<Message> getPledgeCities(Principal user){
        return saleService.getPledgeCities(user.getName());
    }

    /**
     * 获取还款借记卡开户行
     * @param user 当前登录的用户
     * @return
     */
    @RequestMapping(value = "/banks", method = RequestMethod.GET)
    public ResponseEntity<Message> getBanks(Principal user){
        return saleService.getBanks(user.getName());
    }

    /**
     * 销售保险测算
     * @param insuranceCalculateRecDto 测算数据
     * @param user 当前登录的用户
     * @return
     */
    @RequestMapping(value = "/insurance", method = RequestMethod.POST)
    public ResponseEntity<Message> insuranceCalculate(@RequestBody InsuranceCalculateRecDto insuranceCalculateRecDto, Principal user){
        return saleService.insuranceCalculate(insuranceCalculateRecDto, user.getName());
    }

    /**
     * 销售申请提交
     * @param applyRecDto 提交数据
     * @param user 当前登录的用户
     * @return
     */
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public ResponseEntity<Message> apply(@RequestBody ApplyRecDto applyRecDto, Principal user){
        return saleService.apply(applyRecDto, user.getName());
    }

    /**
     * 查询延保大类
     * @param user
     * @return
     */
    @RequestMapping(value = "/warranty", method = RequestMethod.GET)
    public ResponseEntity<Message> getWarranty(Principal user){
        return saleService.getWarranty(user.getName());
    }

    /**
     * 查询延保小类
     * @param id
     * @param user
     * @return
     */
    @RequestMapping(value = "/warranty/{id}", method = RequestMethod.GET)
    public ResponseEntity<Message> getWarrantyPlan(@PathVariable String id, Principal user){
        return saleService.getWarrantyPlan(id, user.getName());
    }

    /**
     * 查询直属下级区域
     * @param levelId
     * @param user
     * @return
     */
    @RequestMapping(value = "/childLevel", method = RequestMethod.GET)
    public ResponseEntity<Message> getChildLevel(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                 String levelId, Principal user){
        return saleService.getChildLevel(headerParam, levelId, user.getName());
    }

    /**
     *获取购置税
     * @param productCaseId 产品方案ID
     * @param productCaseName 产品方案名称
     * @param sellingPrice 车辆售价
     * @param isFinance 是否融资
     * @param user 登录用户
     * @return
     */
    @RequestMapping(value = "/purchaseTax", method = RequestMethod.GET)
    public ResponseEntity<Message> getPurchaseTax(@RequestParam String productCaseId, @RequestParam String productCaseName, @RequestParam String sellingPrice, @RequestParam String isFinance, Principal user){
        return saleService.getPurchaseTax(productCaseId, productCaseName, sellingPrice, isFinance, user.getName());
    }

    /**
     * 获取人身意外保障服务
     * @param grade 融资档位
     * @param period 融资期限
     * @param deposit 保证金
     * @param downPay 首付
     * @param sellingPrice 车辆售价
     * @param isFinance 是否融资
     * @param user 登录用户
     * @return
     */
    @RequestMapping(value = "/accidentProtection", method = RequestMethod.GET)
    public ResponseEntity<Message> getAccidentProtection(@RequestParam String grade, @RequestParam String period, @RequestParam String deposit, @RequestParam String downPay, @RequestParam String sellingPrice, @RequestParam String isFinance, Principal user){
        return saleService.getAccidentProtection(grade, period, deposit, downPay, sellingPrice, isFinance, user.getName());
    }

    /**
     * 获取先锋卫士价格
     * @param isFinance 是否融资
     * @param period 融资期限
     * @param sellingPrice 车辆售价
     * @param user 当前登录用户
     * @return
     */
    @RequestMapping(value = "/xfws", method = RequestMethod.GET)
    public ResponseEntity<Message> getXfws(@RequestParam String isFinance, @RequestParam String period, @RequestParam String sellingPrice, Principal user){
        return saleService.getXfws(isFinance, period, sellingPrice, user.getName());
    }
}
