package com.tm.wechat.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengchao on 2017/6/1.
 */
public class ApprovalTypeConvert {
    public String typeConvert(String status){
        Map<String, String> map = new HashMap<>();
        map.put("000", "1000");//预审通过
        map.put("0001", "100");//申请中
        map.put("0002", "1100");//预审拒绝
        map.put("0003", "300");
        map.put("100", "2000");
        map.put("101", "2000");
        map.put("102", "3000");
        map.put("103", "3500");
        map.put("104", "4000");
        //微众状态1,2预审批失败
        map.put("1", "1100");
        map.put("2", "1100");
        if(map.get(status) == null){
            return status;
        }
        return map.get(status);
    }
}
