package com.tm.wechat.utils.push.ios;


import com.tm.wechat.utils.push.IOSNotification;

public class IOSListcast extends IOSNotification {
	public IOSListcast(String appkey, String appMasterSecret) throws Exception{
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "listcast");
	}
	
	public void setDeviceToken(String token) throws Exception {
    	setPredefinedKeyValue("device_tokens", token);
    }
}
