package com.tm.wechat.service.overdueRate;

import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.JsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbiao on 2016/11/9.
 */
@Service
public class OverdueRateService {

    @Autowired
    private JsonDecoder jsonDecoder;

    @Autowired
    private  OverdueRateInterface overdueRateInterface;

    @Autowired
    private OverdueRateYCInterface overdueRateYCInterface;

    @Autowired
    private WechatProperties wechatProperties;

    /**
     *按日期查询逾期率统计
     * @param type
     * @param startDate
     * @param endDate
     * @param levelId
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getOverDueRateByDate(int type, String startDate, String endDate, int levelId, String userName, String headerParam){
        String customer = JsonPath.from(headerParam).get("systemflag");
        String result;
        if("yachi".equals(customer)){
            result = overdueRateYCInterface.getOverDueRateByDate("tjYuQiLvByTime", type, startDate, endDate, levelId, userName, wechatProperties.getSign(), wechatProperties.getTimestamp());
        }else {
            result = overdueRateInterface.getOverDueRateByDate("tjYuQiLvByTime", type, startDate, endDate, levelId, userName, wechatProperties.getSign(), wechatProperties.getTimestamp());
        }
        return jsonDecoder.singlePropertyDecoder(result, "tjYuQiLvByTimeList");
    }

    /**
     * 按区域查询逾期率统计
     * @param date
     * @param levelId
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getOverDueRateByArea(String date, int levelId, String userName, String headerParam){
        String customer = JsonPath.from(headerParam).get("systemflag");
        String result;
        if("yachi".equals(customer)){
            result = overdueRateYCInterface.getOverDueRateByArea("tjYuQiLvByArea", date, levelId, userName, wechatProperties.getSign(), wechatProperties.getTimestamp());
        }else {
            result = overdueRateInterface.getOverDueRateByArea("tjYuQiLvByArea", date, levelId, userName, wechatProperties.getSign(), wechatProperties.getTimestamp());
        }
        if(!JsonPath.from(result).get("result.isSuccess").equals("true")){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, JsonPath.from(result).get("result.resultMsg")), HttpStatus.OK);
        }
        List<Map> list  = (List<Map>) JsonPath.from(result).get("tjYuQiLvByAreaList");
        Collections.sort(list, (arg0, arg1) -> Float.parseFloat(arg0.get("overdueRate").toString()) < Float.parseFloat(arg1.get("overdueRate").toString()) ? 1 : -1);
        list.forEach(temp -> {
            temp.put("intoPId", JsonPath.from(result).get("getNextAreaList.sjjgid"));
            temp.put("intoName", JsonPath.from(result).get("getNextAreaList.areaName"));
        });
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, list), HttpStatus.OK);
    }




}
