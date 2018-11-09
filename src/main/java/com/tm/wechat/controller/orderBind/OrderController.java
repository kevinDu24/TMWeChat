package com.tm.wechat.controller.orderBind;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.order.OrderService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单绑定
 * Created by pengchao on 2018/4/9.
 */
@RestController
@RequestMapping("/orderBind")
@PreAuthorize("@permission.isDealerUser(authentication.principal.username)")
public class OrderController {

    @Autowired
    OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    /**
     * 查询某用户的订单列表
     * @param userName
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    @RequestMapping(value = "/getOrderList", method = RequestMethod.GET)
    public ResponseEntity<Message> getOrderList(String userName, @RequestParam(value = "condition", required = false, defaultValue="") String condition, String beginTime, String endTime){
        try {
            return orderService.getOrderList(userName, condition, beginTime, endTime);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取用户订单列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询用户下级机构
     * @param userName
     * @return
     */
    @RequestMapping(value = "/getOrganization", method = RequestMethod.GET)
    public ResponseEntity<Message> getOrganization(String userName){
        try {
            return orderService.getOrganization(userName);
        } catch (Exception ex) {
            ex.printStackTrace();

            logger.error("获取用户下级机构异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 查询用户下级机构的订单数量
     * @param userName
     * @return
     */
    @RequestMapping(value = "/getOrderCount", method = RequestMethod.GET)
    public ResponseEntity<Message> getOrderCount(String userName, String beginTime, String endTime){
        try {
            return orderService.getOrganizationAndCount(userName, beginTime ,endTime);
        } catch (Exception ex) {
            ex.printStackTrace();

            logger.error("获取组织订单数量异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 订单绑定
     * @param userName
     * @param targetUser 要绑定到的对象
     * @return
     */
    @RequestMapping(value = "/orderBind", method = RequestMethod.GET)
    public ResponseEntity<Message> orderBind(String userName,  String targetUser, String applyNum, String name){
        try {
            return orderService.orderBind(userName, targetUser, applyNum, name);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("订单绑定异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询用户分单历史
     * @param userName
     * @return
     */
    @RequestMapping(value = "/getOrderHistory", method = RequestMethod.GET)
    public ResponseEntity<Message> getOrderHistory(String userName, String condition){
        try {
            return orderService.getOrderHistory(userName, condition);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询分单历史记录异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

}
