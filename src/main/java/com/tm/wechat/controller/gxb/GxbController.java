//package com.tm.wechat.controller.gxb;
//
//import com.tm.wechat.dto.gxb.WechatShareDto;
//import com.tm.wechat.dto.message.Message;
//import com.tm.wechat.dto.message.MessageType;
//import com.tm.wechat.service.approval.ApprovalService;
//import com.tm.wechat.service.gxb.GxbService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by xuhao on 2017/7/18.
// */
//@RestController
//@RequestMapping("/gxb")
//public class GxbController {
//
//    @Autowired
//    private GxbService gxbService;
//
//    @Autowired
//    private ApprovalService approvalService;
//
//    /**
//     *  解析JSON数据
//     * @param sequenceNo sequenceNo
//     */
//    @RequestMapping(value = "/processGxbData", method = RequestMethod.POST)
//    public void processGxbData(String sequenceNo) {
//        gxbService.processGxb02Data(sequenceNo);
//    }
//
//    /**
//     * 取得认证报告的信息
//     * @param applyNum 申请编号
//     * @param token 页面验证token
//     */
//    @RequestMapping(value = "/getReportData", method = RequestMethod.GET)
//    public ResponseEntity<Message> getReportData(String applyNum, String token) {
//        return gxbService.getReportData(applyNum, token);
//    }
//
//    /**
//     * 取得微信联系人的信息
//     * @param approvalUuid 申请编号Uuid
//     */
//    @RequestMapping(value = "/getWechatContactList", method = RequestMethod.GET)
//    public ResponseEntity<Message> getWechatContactList(String approvalUuid) {
//        return gxbService.getWechatContactList(approvalUuid);
//    }
//
//    /**
//     * 取得微信群组的信息
//     * @param approvalUuid 申请编号Uuid
//     */
//    @RequestMapping(value = "/getWechatContactGroupList", method = RequestMethod.GET)
//    public ResponseEntity<Message> getWechatContactGrouList(String approvalUuid) {
//        return gxbService.getWechatContactGroupList(approvalUuid);
//    }
//
//    /**
//     * 取得微信共享联系人的信息
//     * @param approvalUuid 申请编号Uuid
//     */
//    @RequestMapping(value = "/getWechatShareContactList", method = RequestMethod.GET)
//    public ResponseEntity<Message> getWechatShareContactList(String approvalUuid) {
//        Map<String, List<WechatShareDto>> map = gxbService.getWechatShareContactMap(approvalUuid);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, map), HttpStatus.OK);
//    }
//
//    /**
//     * 取得微信共享群组的信息
//     * @param approvalUuid 申请编号Uuid
//     */
//    @RequestMapping(value = "/getWechatShareContactGroupList", method = RequestMethod.GET)
//    public ResponseEntity<Message> getWechatShareContactGroupList(String approvalUuid) {
//        Map<String, List<WechatShareDto>> map = gxbService.getWechatShareContactGroupMap(approvalUuid);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, map), HttpStatus.OK);
//    }
//
//    /**
//     * 取得淘宝交易的信息
//     * @param approvalUuid 申请编号Uuid
//     */
//    @RequestMapping(value = "/getTaobaoTradeList", method = RequestMethod.GET)
//    public ResponseEntity<Message> getTaobaoTradeList(String approvalUuid) {
//        return gxbService.getTaobaoTradeList(approvalUuid);
//    }
//
//    /**
//     * 取得支付宝交易的信息
//     * @param approvalUuid 申请编号Uuid
//     */
//    @RequestMapping(value = "/getAlipayTradeList", method = RequestMethod.GET)
//    public ResponseEntity<Message> getAlipayTradeList(String approvalUuid, String isIncomeOrPayFlag) {
//        return gxbService.getAlipayTradeList(approvalUuid, isIncomeOrPayFlag);
//    }
//
//    /**
//     * 获取优质认证报告书token
//     */
//    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
//    public ResponseEntity<Message> getToken() {
//        return gxbService.getToken();
//    }
//}
