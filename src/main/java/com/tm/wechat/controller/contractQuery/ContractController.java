package com.tm.wechat.controller.contractQuery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.dto.common.CommonDto;
import com.tm.wechat.dto.contract.ContractRec;
import com.tm.wechat.dto.contract.Contracts;
import com.tm.wechat.dto.contract.ProductInfo;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.ContractService;
import com.tm.wechat.service.ContractYCInterface;
import com.tm.wechat.service.system.TMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * 合同查询
 * Created by LEO on 16/9/1.
 */
@RequestMapping("/contracts")
@RestController
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TMService tmService;

    @Autowired
    private ContractYCInterface contractYCInterface;

    @Autowired
    private WechatProperties wechatProperties;


    /**
     * 根据合同状态查询合同查询列表
     * url:localhost:8081/contracts/state?applyNum=&fpName=&startDate&endDate&state=&page=1
     * @param user 登录用户
     * @param contractRec
     * @return
     */
    @RequestMapping(value = "/state", method = RequestMethod.GET)
    public ResponseEntity<Message> getContractsByState(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                       Principal user, ContractRec contractRec){
        String customer = JsonPath.from(headerParam).get("systemflag");
        String result;
        if("yachi".equals(customer)){
            result = contractYCInterface.query("queryContractStateList", user.getName(), wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), contractRec.getApplyNum(), contractRec.getFpName(), contractRec.getStartDate(),
                    contractRec.getEndDate(), null, contractRec.getPage(), contractRec.getState(), null, null);
        }else {
            result = contractService.query("queryContractStateList", user.getName(), wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), contractRec.getApplyNum(), contractRec.getFpName(), contractRec.getStartDate(),
                    contractRec.getEndDate(), null, contractRec.getPage(), contractRec.getState(), null, null);
        }
        Contracts contracts = null;
        try {
             contracts = objectMapper.readValue(result, Contracts.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, contracts), HttpStatus.OK);
    }

    /**
     * 根据还款状态查询合同列表
     * url:localhost:8081/contracts/repayment?applyNum=&fpName=&startDate&endDate&repayState=&page=1
     * @param user
     * @param contractRec
     * @return
     */
    @RequestMapping(value = "/repayment", method = RequestMethod.GET)
    public ResponseEntity<Message> getContractsByRepayment(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                           Principal user, ContractRec contractRec){
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, tmService.getRepayPlans(user.getName(), contractRec, headerParam)), HttpStatus.OK);
    }

    /**
     * 合同审批日志查询
     * url:localhost:8081/contracts/36157040/log
     * @param user
     * @param condition
     * @return
     */
    @RequestMapping(value = "/{condition}/log", method = RequestMethod.GET)
    public ResponseEntity<Message> getContract(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                               Principal user, @PathVariable String condition){
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, tmService.getContractApproveLog(user.getName(), "queryContractState", condition, headerParam)), HttpStatus.OK);
    }

    /**
     * 合同还款计划明细查询
     * url:localhost:8081/contracts/36157040/repayDetail
     * @param user
     * @param condition
     * @return
     */
    @RequestMapping(value = "/{condition}/repayDetail", method = RequestMethod.GET)
    public ResponseEntity<Message> getRepayDetail(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                  Principal user, @PathVariable String condition){
        String customer = JsonPath.from(headerParam).get("systemflag");
        if("yachi".equals(customer)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, tmService.getYCContractMsg(user.getName(), "queryContractRepayplan", condition)), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, tmService.getContractMsg(user.getName(), "queryContractRepayplan", condition)), HttpStatus.OK);
        }
    }


    /**
     * 产品详情
     * url:localhost:8081/contracts/36157040/details
     * @param user
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/{applyNum}/details", method = RequestMethod.GET)
    public ResponseEntity<Message> getOverView(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                               Principal user, @PathVariable String applyNum){
        String result;
        String customer = JsonPath.from(headerParam).get("systemflag");
        if("yachi".equals(customer)){
            result = contractYCInterface.query("queryContractInfo", user.getName(), wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), applyNum, null, null, null, null, null, null, null, null);

        }else {
            result = contractService.query("queryContractInfo", user.getName(), wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), applyNum, null, null, null, null, null, null, null, null);
        }
        ProductInfo productInfo = null;
        try {
            productInfo = objectMapper.readValue(result, ProductInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, productInfo), HttpStatus.OK);
    }

    /**
     * 按省份查询申请量
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/applyNum", method = RequestMethod.GET)
    public ResponseEntity<Message> applyNum() throws IOException {
        String result = contractService.queryApplyNum("querySqltj", "admin", wechatProperties.getTimestamp(), wechatProperties.getSign());
//        String result = hplRepository.findOne(1L).getData();
        result = result.replaceAll("省", "").replaceAll("市", "").replaceAll("回族自治区", "").replaceAll("维吾尔自治区", "").replaceAll("壮族自治区", "").replaceAll("自治区", "");
        CommonDto commonDto = objectMapper.readValue(result, CommonDto.class);
        if(!commonDto.getResult().getIsSuccess()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, commonDto.getResult().getResultMsg()), HttpStatus.OK);
        }
        List<Object> applyNumRecords = Lists.newArrayList();
        List<Map> guangXiRecords = Lists.newArrayList();
        if(commonDto.getShenqinliang() != null && commonDto.getShenqinliang().size() != 0){
            commonDto.getShenqinliang().forEach(object -> {
                Map map = (Map) object;
                if(map.get("area").equals("广西")){
                    guangXiRecords.add(map);
                }else if(!map.get("area").equals("")){
                    applyNumRecords.add(map);
                }
            });
        }
        final Integer[] guangXiApplyNum = {0};
        final String[] currentDate = {""};
        guangXiRecords.forEach(map -> {
            guangXiApplyNum[0] += Integer.parseInt(map.get("count").toString());
            currentDate[0] = map.get("XTCZRQ").toString();
        });
        Map guangXiRecord = Maps.newHashMap();
        guangXiRecord.put("area", "广西");
        guangXiRecord.put("count", guangXiApplyNum[0]);
        guangXiRecord.put("XTCZRQ", currentDate[0]);
        applyNumRecords.add(guangXiRecord);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, applyNumRecords), HttpStatus.OK);
    }
}
