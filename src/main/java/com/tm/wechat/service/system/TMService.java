package com.tm.wechat.service.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.dao.BudgetRepository;
import com.tm.wechat.domain.Budget;
import com.tm.wechat.dto.contract.ContractMsg;
import com.tm.wechat.dto.contract.ContractRec;
import com.tm.wechat.dto.contract.Contracts;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.sale.CallBackDto;
import com.tm.wechat.dto.sale.SalePlan;
import com.tm.wechat.dto.sale.SalePlanDto;
import com.tm.wechat.dto.sale.SaleRec;
import com.tm.wechat.service.ContractService;
import com.tm.wechat.service.ContractYCInterface;
import com.tm.wechat.service.sale.SaleInterface;
import com.tm.wechat.service.sale.SaleService;
import com.tm.wechat.service.sale.SaleYCInterface;
import com.tm.wechat.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by LEO on 16/8/26.
 */
@Service
public class TMService {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContractService contractService;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private SaleInterface saleInterface;

    @Autowired
    private SaleYCInterface saleYCInterface;

    @Autowired
    private ContractYCInterface contractYCInterface;

    @Autowired
    private SaleService saleService;

    @Autowired
    private WechatProperties wechatProperties;

    /**
     * 查询合同审批日志
     *
     * @param userName
     * @param url
     * @param condition
     * @return
     */
    public ContractMsg getContractApproveLog(String userName, String url, String condition, String headerParam) {
        ContractMsg contractMsg;
        String customer = JsonPath.from(headerParam).get("systemflag");
        if ("yachi".equals(customer)) {
            contractMsg = getYCContractMsg(userName, url, condition);
        } else {
            contractMsg = getContractMsg(userName, url, condition);
        }
        Map map = (Map) contractMsg.getContractinfo();
        List<Map> list = (List<Map>) map.get("contractstatelist");
        Collections.sort(list, (arg0, arg1) -> Long.parseLong(arg0.get("BASHRQ").toString()) < Long.parseLong(arg1.get("BASHRQ").toString()) ? 1 : -1);
        return contractMsg;
    }

    /**
     * 查询还款计划详情(太盟)
     *
     * @param userName
     * @param url
     * @param condition
     * @return
     */
    public ContractMsg getContractMsg(String userName, String url, String condition) {
        String result = null;
        if (Utils.isCardId(condition)) {
            result = contractService.query(url, userName, wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), null, null, null, null, null, null, null, null, condition);
        } else if (Utils.isNumber(condition)) {
            result = contractService.query(url, userName, wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), condition, null, null, null, null, null, null, null, null);
        } else {
            result = contractService.query(url, userName, wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), null, null, null, null, null, null, null, condition, null);
        }
        ContractMsg contractMsg = null;
        try {
            contractMsg = objectMapper.readValue(result, ContractMsg.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contractMsg;
    }

    /**
     * 查询还款计划详情(亚驰)
     *
     * @param userName
     * @param url
     * @param condition
     * @return
     */
    public ContractMsg getYCContractMsg(String userName, String url, String condition) {
        String result;
        if (Utils.isCardId(condition)) {
            result = contractYCInterface.query(url, userName, wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), null, null, null, null, null, null, null, null, condition);
        } else if (Utils.isNumber(condition)) {
            result = contractYCInterface.query(url, userName, wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), condition, null, null, null, null, null, null, null, null);
        } else {
            result = contractYCInterface.query(url, userName, wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), null, null, null, null, null, null, null, condition, null);
        }
        ContractMsg contractMsg = null;
        try {
            contractMsg = objectMapper.readValue(result, ContractMsg.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contractMsg;
    }


    /**
     * 获取还款计划列表并排序
     *
     * @param userName
     * @param contractRec
     * @return
     */
    public Contracts getRepayPlans(String userName, ContractRec contractRec, String headerParam) {
        String result;
        String customer = JsonPath.from(headerParam).get("systemflag");
        if ("yachi".equals(customer)) {
            result = contractYCInterface.query("queryContractRepayplanList", userName, wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), contractRec.getApplyNum(), contractRec.getFpName(), contractRec.getStartDate(),
                    contractRec.getEndDate(), contractRec.getState(), contractRec.getPage(), null, null, null);

        } else {
            result = contractService.query("queryContractRepayplanList", userName, wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), contractRec.getApplyNum(), contractRec.getFpName(), contractRec.getStartDate(),
                    contractRec.getEndDate(), contractRec.getState(), contractRec.getPage(), null, null, null);
        }
        Contracts contracts = null;
        try {
            contracts = objectMapper.readValue(result, Contracts.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map> list = (List<Map>) contracts.getContractrepayplanlist();
        Collections.sort(list, (arg0, arg1) -> Long.parseLong(arg0.get("BASQRQ").toString()) < Long.parseLong(arg1.get("BASQRQ").toString()) ? 1 : -1);
        return contracts;
    }

    /**
     * 查询销售计划
     *
     * @param saleRec
     * @return
     */
    public ResponseEntity<Message> querySalePlan(String headerParam, SaleRec saleRec, String userName) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse(saleRec.getStartDate().substring(0, 6) + "01");
            endDate = dateFormat.parse(saleRec.getEndDate().substring(0, 6) + "27");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String customer = JsonPath.from(headerParam).get("systemflag");
        List<Budget> budgets = budgetRepository.findByParentIdAndTypeAndPlanDateBetweenAndCustomer(saleRec.getParentId(), saleRec.getPlanType(),
                startDate, endDate, customer);
        // 根据levelId分组
        Map<Long, List<Budget>> groupedMap = budgets.stream().collect(Collectors.groupingBy(Budget::getLevelId));
        List<SalePlan> salePlans = Lists.newArrayList();
        CallBackDto statistics = null;
        try {
            if ("taimeng".equals(customer)) {
                statistics = objectMapper.readValue(saleInterface.queryStatisticsByDate("qryStatByTimeSlot", userName,
                        saleRec.getPlanType() == 7 ? 4 : 2, saleRec.getParentId().toString(), saleRec.getStartDate(), saleRec.getEndDate(), "是"), CallBackDto.class);
            } else {
                statistics = objectMapper.readValue(saleYCInterface.queryStatisticsByDate("qryStatByTimeSlot", userName,
                        saleRec.getPlanType() == 7 ? 4 : 2, saleRec.getParentId().toString(), saleRec.getStartDate(), saleRec.getEndDate()), CallBackDto.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map data = (Map) statistics.getData();
        List<Map> tablegrid = (List<Map>) data.get("tableGrid");
        groupedMap.forEach((key, value) -> {
            SalePlan salePlan = new SalePlan();
            salePlan.setLevelId(key);
            salePlan.setArea(value.get(0).getArea());
            final Long[] planCount = {0l};
            value.forEach(budget -> {
                planCount[0] += budget.getBudget();
            });
            salePlan.setPlanCount(planCount[0]);
            if (saleRec.getPlanType().equals(6)) {
                tablegrid.forEach(map -> {
                    if (map.get("levelId").equals(key.toString())) {
                        if (map.get("planCount") == null || "".equals(map.get("planCount"))) {
                            salePlan.setRealCount(0l);
                        } else {
                            salePlan.setRealCount(Long.parseLong(map.get("planCount").toString()));
                        }
                    }
                });
            } else {
                tablegrid.forEach(map -> {
                    if (map.get("levelId").equals(key.toString())) {
                        if (map.get("realCount") == null || "".equals(map.get("realCount"))) {
                            salePlan.setRealCount(0l);
                        } else {
                            salePlan.setRealCount(Long.parseLong(map.get("realCount").toString()));
                        }
                    }
                });
            }
            salePlans.add(salePlan);
        });
        Message message = new Message(MessageType.MSG_TYPE_SUCCESS, salePlans);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    public ResponseEntity<Message> queryNewSalePlan(String headerParam, SaleRec saleRec, String userName) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse(saleRec.getStartDate().substring(0, 6) + "01");
            endDate = dateFormat.parse(saleRec.getEndDate().substring(0, 6) + "27");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String customer = JsonPath.from(headerParam).get("systemflag");
        List<Budget> budgets = budgetRepository.findByParentIdAndTypeAndPlanDateBetweenAndCustomer(saleRec.getParentId(), saleRec.getPlanType(),
                startDate, endDate, customer);
        // 根据levelId分组
        Map<Long, List<Budget>> groupedMap = budgets.stream().collect(Collectors.groupingBy(Budget::getLevelId));
        List<SalePlan> salePlans = Lists.newArrayList();
        CallBackDto statistics = null;
        try {
            if ("taimeng".equals(customer)) {
                statistics = objectMapper.readValue(saleInterface.queryStatisticsByDate("qryStatByTimeSlot", userName,
                        saleRec.getPlanType() == 7 ? 4 : 2, saleRec.getParentId().toString(), saleRec.getStartDate(), saleRec.getEndDate(), "是"), CallBackDto.class);
            } else {
                statistics = objectMapper.readValue(saleYCInterface.queryStatisticsByDate("qryStatByTimeSlot", userName,
                        saleRec.getPlanType() == 7 ? 4 : 2, saleRec.getParentId().toString(), saleRec.getStartDate(), saleRec.getEndDate()), CallBackDto.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map data = (Map) statistics.getData();
        List<Map> tablegrid = (List<Map>) data.get("tableGrid");
        groupedMap.forEach((key, value) -> {
            SalePlan salePlan = new SalePlan();
            salePlan.setLevelId(key);
            salePlan.setArea(value.get(0).getArea());
            final Long[] planCount = {0l};
            value.forEach(budget -> {
                planCount[0] += budget.getBudget();
            });
            salePlan.setPlanCount(planCount[0]);
            if (saleRec.getPlanType().equals(6)) {
                tablegrid.forEach(map -> {
                    if (map.get("levelId").equals(key.toString())) {
                        if (map.get("planCount") == null || "".equals(map.get("planCount"))) {
                            salePlan.setRealCount(0l);
                        } else {
                            salePlan.setRealCount(Long.parseLong(map.get("planCount").toString()));
                        }
                    }
                });
            } else {
                tablegrid.forEach(map -> {
                    if (map.get("levelId").equals(key.toString())) {
                        if (map.get("realCount") == null || "".equals(map.get("realCount"))) {
                            salePlan.setRealCount(0l);
                        } else {
                            salePlan.setRealCount(Long.parseLong(map.get("realCount").toString()));
                        }
                    }
                });
            }
            salePlans.add(salePlan);
        });
        SalePlanDto parentPalePlan = new SalePlanDto();
        parentPalePlan.setLevelId(saleRec.getParentId());
        parentPalePlan.setSalePlans(salePlans);
        ResponseEntity<Message> responseEntity = null;
        Map<String, String> result = Maps.newHashMap();
        responseEntity = saleService.getChildLevel(headerParam, saleRec.getParentId().toString(), userName);
        result =(Map) responseEntity.getBody().getData();
        String areaName = result.get("areaName");
        String parentId = result.get("sjjgid");
        if("81".equals(saleRec.getParentId().toString())){
            final Long[] parentPlanCount = {0l};
            final Long[] parenRealCount = {0l};
            for(SalePlan salePlan : salePlans){
                if(salePlan.getRealCount() == null || "".equals(salePlan.getRealCount().toString())){
                    salePlan.setRealCount(0l);
                }
                parentPlanCount[0] += salePlan.getPlanCount();
                parenRealCount[0] += salePlan.getRealCount();
            }
            parentPalePlan.setPlanCount(parentPlanCount[0]);
            parentPalePlan.setRealCount(parenRealCount[0]);
            parentPalePlan.setArea(areaName);
        }else {
            List<Budget> budgetList = budgetRepository.findByLevelIdAndTypeAndPlanDateBetweenAndCustomer(saleRec.getParentId(), saleRec.getPlanType(),
                    startDate, endDate, customer);
            final Long[] parentPlanCount = {0l};
            for(Budget budget: budgetList){
                parentPlanCount[0] += budget.getBudget();
            }
            parentPalePlan.setArea(budgetList.get(0).getArea());
            parentPalePlan.setPlanCount(parentPlanCount[0]);
            CallBackDto parentStatistics = null;
            try {
                if ("taimeng".equals(customer)) {
                    parentStatistics = objectMapper.readValue(saleInterface.queryStatisticsByDate("qryStatByTimeSlot", userName,
                            saleRec.getPlanType() == 7 ? 4 : 2, parentId, saleRec.getStartDate(), saleRec.getEndDate(), "是"), CallBackDto.class);
                } else {
                    parentStatistics = objectMapper.readValue(saleYCInterface.queryStatisticsByDate("qryStatByTimeSlot", userName,
                            saleRec.getPlanType() == 7 ? 4 : 2, saleRec.getParentId().toString(), saleRec.getStartDate(), saleRec.getEndDate()), CallBackDto.class);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Map parentdata = (Map) parentStatistics.getData();
            List<Map> parentTablegrid = (List<Map>) parentdata.get("tableGrid");
            if (saleRec.getPlanType().equals(6)) {
                parentTablegrid.forEach(map -> {
                    if (map.get("levelId").equals(saleRec.getParentId().toString())) {
                        if (map.get("planCount") == null || "".equals(map.get("planCount"))) {
                            parentPalePlan.setRealCount(0l);
                        } else {
                            parentPalePlan.setRealCount(Long.parseLong(map.get("planCount").toString()));
                        }
                    }
                });
            } else {
                parentTablegrid.forEach(map -> {
                    if (map.get("levelId").equals(saleRec.getParentId().toString())) {
                        if (map.get("realCount") == null || "".equals(map.get("realCount"))) {
                            parentPalePlan.setRealCount(0l);
                        } else {
                            parentPalePlan.setRealCount(Long.parseLong(map.get("realCount").toString()));
                        }
                    }
                });
            }
        }
        Message message = new Message(MessageType.MSG_TYPE_SUCCESS, parentPalePlan);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }
}
