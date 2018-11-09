package com.tm.wechat.controller;

import com.tm.wechat.dto.approval.AprrovalReportDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.approval.ApprovalDataService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zcHu on 17/5/10.
 */
@RestController
@RequestMapping("approvalData")
public class ApprovalDataController {
    @Autowired
    private ApprovalDataService approvalDataService;


    /**
     * 分页返回申请报表列表
     * @param aprrovalReportDto
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/approvalSearch", method = RequestMethod.GET)
    public ResponseEntity<Message> approvalSearch(AprrovalReportDto aprrovalReportDto, int page , int size){
        try{
            return approvalDataService.approvalSearch(aprrovalReportDto,page,size);
        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR , CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 按月查询不同审批状态的申请数量
     *
     * @param searchType 0:查询预审批相关信息、1：查询在线申请相关信息
     * @return
     */
    @RequestMapping(value = "/approvalCount", method = RequestMethod.GET)
    public ResponseEntity<Message> approvalSearch(String searchType){
        try{
            return approvalDataService.getApprovalMonthData(searchType);
        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR , CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


}

