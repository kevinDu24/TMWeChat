package com.tm.wechat.controller.gpsConvention;

import com.tm.wechat.dto.gpsconvention.GpsSubmitInfoDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.gpsconvention.GpsConventionService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by pengchao on 2018/4/9.
 */
@RestController
@RequestMapping("/gpsConvention")
@PreAuthorize("@permission.isDealerUser(authentication.principal.username)")
public class GpsConventionController {

    @Autowired
    GpsConventionService gpsConventionService;

    private static final Logger logger = LoggerFactory.getLogger(GpsConventionController.class);

    /**
     * 查询某用户的GPS邀约列表
     * @param user
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    @RequestMapping(value = "/getGpsList", method = RequestMethod.GET)
    public ResponseEntity<Message> getGpsList(Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition, String beginTime, String endTime){
        try {
            return gpsConventionService.getGpsList(user.getName(), condition, beginTime, endTime);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取用户GPS邀约列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 获取省、市、县列表
     * @param type 获取类型 [“0”:所有省份] [“1”:所有当前省的所有城市] [“2”:所有当前市的所有县]
     * @param id 查询参数 [“0”:null] [“1”:省名称] [“2”:市id]
     * @return
     */
    @RequestMapping(value = "/getAreaList", method = RequestMethod.GET)
    public ResponseEntity<Message> getAreaList(String type, String id){
        try {
            return gpsConventionService.getAreaList(type, id);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取省、市、县列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 获取GPS安装品牌、方式
     * @param applyNum
     * @param installProvince
     * @param installProvince
     * @param user
     * @return
     */
    @RequestMapping(value = "/getGpsInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getGpsInfo(Principal user, String applyNum, String installProvince, String installCity){
        try {
            return gpsConventionService.getGpsInfo(applyNum, installProvince, installCity, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取GPS安装品牌、方式异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * GPS邀约信息提交
     * @param user
     * @param submitInfo 提交信息
     * @return
     */
    @RequestMapping(value = "/submitGpsInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> submitGpsInfo(Principal user, @RequestBody GpsSubmitInfoDto submitInfo){
        try {
            return gpsConventionService.submitGpsInfo(user.getName(), submitInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("GPS邀约信息提交异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询用户GPS邀约历史
     * @param user
     * @return
     */
    @RequestMapping(value = "/getGpsHistory", method = RequestMethod.GET)
    public ResponseEntity<Message> getGpsHistory(Principal user, String condition){
        try {
            return gpsConventionService.getGpsHistory(user.getName(), condition);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询GPS邀约历史异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

}
