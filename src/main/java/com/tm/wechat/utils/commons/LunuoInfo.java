package com.tm.wechat.utils.commons;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiaohao on 2017/4/16.
 */
public enum LunuoInfo {

    loginUrl,
    gpsDataUrl,
    UserID,
    Password;

    public static final Map<String,String> LunuoDesc =new HashMap<>();

    public static final Map<String,String> LunuoVal = new HashMap<>();

    static {
        LunuoDesc.put("loginUrl","鲁诺平台登录地址");
        LunuoDesc.put("gpsDataUrl","鲁诺平台读取gps_data信息地址");
        LunuoDesc.put("UserID","鲁诺平台登录账号");
        LunuoDesc.put("Password","鲁诺平台登录密码");
        LunuoVal.put("loginUrl","http://27.221.58.41:8000/workspace/default.aspx");
        LunuoVal.put("gpsDataUrl","http://27.221.58.41:8000/workspace/Ajax/ajax.ashx");
        LunuoVal.put("UserID","先锋太盟融资租赁有限公司");
        LunuoVal.put("Password","8888");
    }

}
