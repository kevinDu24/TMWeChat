package com.tm.wechat.utils.push.ios;


import com.tm.wechat.utils.push.IOSNotification;

public class IOSBroadcast extends IOSNotification {
	public IOSBroadcast(String appkey,String appMasterSecret) throws Exception {
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "broadcast");	
	}
	public IOSBroadcast(String appkey) throws Exception {
		setPredefinedKeyValue("appkey", appkey);
	}
}
