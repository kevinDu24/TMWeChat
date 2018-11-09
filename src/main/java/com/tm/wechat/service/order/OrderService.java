package com.tm.wechat.service.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.dao.OrderAssignHistoryRepository;
import com.tm.wechat.domain.OrderAssignHistory;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.order.*;
import com.tm.wechat.dto.resu.Resul;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.utils.commons.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengchao on 2018/4/9.
 */
@Service
public class OrderService {

    @Autowired
    CoreSystemInterface coreSystemInterface;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OrderAssignHistoryRepository orderAssignHistoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     * 查询订单列表
     * @param userName
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    public ResponseEntity<Message> getOrderList(String userName, String condition, String beginTime, String endTime){
        if(condition == null){
            condition = "";
        }
        if(beginTime == null){
            beginTime = "";
        }
        if(endTime == null){
            endTime = "";
        }
        // 接受主系统结果dto
        OrderAssignDto result = new OrderAssignDto();
        String coreResult = coreSystemInterface.getOrderList("QueryOrderList", userName, condition, beginTime, endTime);
        try {
            result = objectMapper.readValue(coreResult, OrderAssignDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        List<OrderAssignListResult> orderAssignListResults = new ArrayList();
        if("true".equals(result.getResult().getIsSuccess().toString())){
            orderAssignListResults = result.getOrderList();
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, result.getResult().getResultMsg()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, orderAssignListResults), HttpStatus.OK);
    }

    /**
     * 查询组织架构
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getOrganization(String userName){
        // 接受主系统的结果集
        OrganizationDto result = new OrganizationDto();
        String coreResult = coreSystemInterface.getOrganizationAndCount("LowerLevelOrganization", userName);
        try {
            result = objectMapper.readValue(coreResult, OrganizationDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        OrganizationResult dto = new OrganizationResult();
        if("true".equals(result.getResult().getIsSuccess().toString())){
            List<OrganizationResult> dtos = result.getUserLowers();
            if(dtos == null || dtos.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "当前用户无下级"), HttpStatus.OK);
            }
            dtos.get(0).setParentId(null); //设定根节点的上级id为空，获取树状结构用
            dto = OrganizationResult.findResult(dtos).get(0);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, result.getResult().getResultMsg()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
    }


    /**
     * 查询某个用户下级机构及订单数量
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getOrganizationAndCount(String userName, String beginTime, String endTime){
        if(beginTime == null){
            beginTime = "";
        }
        if(endTime == null){
            endTime = "";
        }
        // 版本号
        OrganizationAndCountResult result = new OrganizationAndCountResult();
        String coreResult = coreSystemInterface.getOrganizationAndCount("QueryLowerLevel", userName, beginTime, endTime);
        try {
            result = objectMapper.readValue(coreResult, OrganizationAndCountResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        OrganizationAndCountListResult dto = new OrganizationAndCountListResult();
        if("true".equals(result.getResult().getIsSuccess().toString())){
            List<OrganizationAndCountListResult> dtos = result.getUserApplylist();
            if(dtos == null || dtos.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "当前用户无下级，不可进行订单分配"), HttpStatus.OK);
            }
            dtos.get(0).setParentId(null); //设定根节点的上级id为空，获取树状结构用
            dto = OrganizationAndCountListResult.findResult(dtos).get(0);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, result.getResult().getResultMsg()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
    }


    /**
     * 订单绑定
     * @param sourceUser 原始人
     * @param targetUser 绑定目标
     * @param applyNum   申请编号
     * @return
     */
    public ResponseEntity<Message> orderBind(String sourceUser, String targetUser, String applyNum, String name){
        // 版本号
        Resul res = new Resul();
        String result = coreSystemInterface.orderBind("OrderBinding", sourceUser, targetUser, applyNum);
        logger.info("OrderBinding={}", result);
        try {
            res = objectMapper.readValue(result, Resul.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(res.getResult().getIsSuccess().toString())){
            //登录订单分配历史表
            OrderAssignHistory orderAssignHistory = new OrderAssignHistory();
            orderAssignHistory.setApplyNum(applyNum);
            orderAssignHistory.setSourceUser(sourceUser);
            orderAssignHistory.setTargetUser(targetUser);
            orderAssignHistory.setCreateUser(sourceUser);
            orderAssignHistory.setUpdateUser(sourceUser);
            orderAssignHistory.setName(name);
            orderAssignHistoryRepository.save(orderAssignHistory);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, res.getResult().getResultMsg()), HttpStatus.OK);
        }
    }


    /**
     * 订单绑定
     * @param userName 登录人
     * @return
     */
    public ResponseEntity<Message> getOrderHistory(String userName, String condition){
        if(condition == null){
            condition = "";
        }
        List<OrderAssignHistory> orderHistory = orderAssignHistoryRepository.findBySourceUserAndCondition(userName, CommonUtils.likePartten(condition));
        if(orderHistory == null || orderHistory.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<OrderHistoryDto> dtos = new ArrayList();
        OrderHistoryDto dto;
        for(OrderAssignHistory item : orderHistory){
            dto = new OrderHistoryDto();
            dto.setName(item.getName());
            dto.setSourceUser(item.getSourceUser());
            dto.setApplyNum(item.getApplyNum());
            dto.setTargetUser(item.getTargetUser());
            dto.setCreateTime(DateUtils.getStrDate(item.getCreateTime(),DateUtils.simpleDateFormatyyyyMMdd));
            dtos.add(dto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dtos), HttpStatus.OK);
    }

}
