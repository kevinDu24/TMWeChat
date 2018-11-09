package com.tm.wechat.utils.push;


import com.tm.wechat.utils.push.android.AndroidBroadcast;
import com.tm.wechat.utils.push.android.AndroidListcast;
import com.tm.wechat.utils.push.android.AndroidUnicast;
import com.tm.wechat.utils.push.android.UPushResultDto;
import com.tm.wechat.utils.push.ios.IOSBroadcast;
import com.tm.wechat.utils.push.ios.IOSListcast;
import com.tm.wechat.utils.push.ios.IOSUnicast;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by qiaohao on 2017/2/21.
 */
public class UmengPushUtils {


    private String appkey = null;
    private String appMasterSecret = null;
    private PushClient client = new PushClient();

    private final String iosAlert = "{\"title\":\"标题\",\"body\":\"内容\"}";

    public UmengPushUtils(String key, String secret) {
        try {
            appkey = key;
            appMasterSecret = secret;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 单播消息推送——安卓
     *
     * @param deviceToken  推送设备
     * @param messageTitle 消息标题
     * @param content 消息内容
     * @throws Exception
     */
    public UPushResultDto sendAndroidUnicast(String deviceToken, String messageTitle, String content) throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(appkey,appMasterSecret);
        unicast.setDeviceToken(deviceToken); //设定token
        unicast.setTicker("新消息"); //通知栏提示文字，暂未用到
        unicast.setTitle(messageTitle); //通知栏标题
        unicast.setText(content); //通知消息内容
        unicast.goAppAfterOpen(); //读取消息默认打开app
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION); //消息类型:通知
        unicast.setTestMode(); //推送环境:测试环境 todo
//        unicast.setPlayVibrate(false);
//        unicast.setPlayLights(false);
//        unicast.setPlaySound(true);
//        unicast.setDescription("新消息描述");
//        Date date = new Date();
//        String timestamp = DateUtils.getStrDate(new Date(date.getTime() + 60 * 60 * 1000),DateUtils.simpleDateFormat);;
//        unicast.setExpireTime(timestamp);
//        unicast.setProductionMode();//推送环境:生产环境
        return client.send(unicast);
    }


    /**
     * 广播消息推送——安卓H
     *
     * @param messageTitle 消息标题
     * @param content 消息内容
     * @throws Exception
     */
    public UPushResultDto sendAndroidBroadcast(String messageTitle, String content) throws Exception {
        AndroidBroadcast unicast = new AndroidBroadcast(appkey,appMasterSecret);
        unicast.setTicker( "新消息"); //通知栏提示文字，暂未用到
        unicast.setTitle(messageTitle); //通知栏标题
        unicast.setText(content); //通知消息内容
        unicast.goAppAfterOpen(); //读取消息默认打开app
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION); //消息类型:通知
        unicast.setTestMode(); //推送环境:测试环境
//        unicast.setProductionMode();//推送环境:生产环境
        return client.send(unicast);
    }

    /**
     *  列播消息推送——安卓
     *
     * @param deviceTokens  deviceToken集合
     * @param messageTitle 消息标题
     * @param content 消息内容
     * @throws Exception
     */
    public UPushResultDto sendAndroidListcast(List<String> deviceTokens, String messageTitle, String content) throws Exception {
        AndroidListcast listcast = new AndroidListcast(appkey,appMasterSecret);
        StringBuilder deviceTokenStr = new StringBuilder();
        //遍历deviceToken集合，拼接成字符串，以","分割
        for(String deviceToken : deviceTokens){
            if(deviceTokenStr.length() != 0){
                deviceTokenStr.append(Constants.COMMA);
            }
            deviceTokenStr.append(deviceToken);
        }
        listcast.setDeviceToken(deviceTokenStr.toString());
        listcast.setTicker( "新消息"); //通知栏提示文字，暂未用到
        listcast.setTitle(messageTitle); //通知栏标题
        listcast.setText(content); //通知消息内容
        listcast.goAppAfterOpen(); //读取消息默认打开app
        listcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION); //消息类型:通知
        listcast.setTestMode(); //推送环境:测试环境
//        listcast.setProductionMode();//推送环境:生产环境
        return client.send(listcast);
    }

    /**
     * 单播消息推送——IOS
     *
     * @param deviceToken  推送设备
     * @param messageTitle 消息标题
     * @param content 消息内容
     * @throws Exception
     */
    public UPushResultDto sendIOSUnicast(String deviceToken, String messageTitle, String content) throws Exception {
        IOSUnicast unicast = new IOSUnicast(appkey,appMasterSecret);
        unicast.setDeviceToken(deviceToken); //设定token
        //设定标题和内容
        String message = iosAlert.replace("标题",messageTitle).replace("内容",content);
        JSONObject object = new JSONObject(message);
        unicast.setAlert(object);
        unicast.setBadge( 0); //默认值
        unicast.setSound( "default"); //默认声音
        unicast.setTestMode(); //推送环境:测试环境
//        unicast.setProductionMode();//推送环境:生产环境
        return client.send(unicast);
    }


    /**
     * 广播消息推送——IOS
     *
     * @param messageTitle 消息标题
     * @param content 消息内容
     * @throws Exception
     */
    public UPushResultDto sendIOSBroadcast(String messageTitle, String content) throws Exception {
        IOSBroadcast iosBroadcast = new IOSBroadcast(appkey,appMasterSecret);
        //设定标题和内容
        String message = iosAlert.replace("标题",messageTitle).replace("内容",content);
        JSONObject object = new JSONObject(message);
        iosBroadcast.setAlert(object);
        iosBroadcast.setBadge( 0); //默认值
        iosBroadcast.setSound( "default"); //默认声音
        iosBroadcast.setTestMode(); //推送环境:测试环境
//        iosBroadcast.setProductionMode();//推送环境:生产环境
        return client.send(iosBroadcast);
    }


    /**
     * 广播消息推送——IOS&Android
     *
     * @param messageTitle 消息标题
     * @param content 消息内容
     * @throws Exception
     */
    public UPushResultDto sendAllBroadcast(String messageTitle, String content) throws Exception {
        IOSBroadcast iosBroadcast = new IOSBroadcast(appkey,appMasterSecret);
        //设定标题和内容
        String message = iosAlert.replace("标题",messageTitle).replace("内容",content);
        JSONObject object = new JSONObject(message);
        iosBroadcast.setAlert(object);
        iosBroadcast.setBadge( 0); //默认值
        iosBroadcast.setSound( "default"); //默认声音
        iosBroadcast.setTestMode(); //推送环境:测试环境
//      iosBroadcast.setProductionMode();//推送环境:生产环境
        UPushResultDto  uPushResultDto = client.send(iosBroadcast);
        if("SUCCESS".equals(uPushResultDto.getRet())){
            AndroidBroadcast unicast = new AndroidBroadcast(appkey,appMasterSecret);
            unicast.setTicker( "新消息"); //通知栏提示文字，暂未用到
            unicast.setTitle(messageTitle); //通知栏标题
            unicast.setText(content); //通知消息内容
            unicast.goAppAfterOpen(); //读取消息默认打开app
            unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION); //消息类型:通知
            unicast.setTestMode(); //推送环境:测试环境
//          unicast.setProductionMode();//推送环境:生产环境
            return client.send(unicast);
        }
        return uPushResultDto;
    }


    public UPushResultDto querySendStatus(String task_id) throws Exception {
        IOSBroadcast unicast = new IOSBroadcast(appkey);
        unicast.setTask_id(task_id);
        unicast.setAppMasterSecret(appMasterSecret);
        return client.querySendStatus(unicast);
    }

    /**
     * 列播消息推送——IOS
     *
     * @param deviceTokens  deviceToken集合
     * @param messageTitle 消息标题
     * @param content 消息内容
     * @throws Exception
     */
    public UPushResultDto sendIOSListcast(List<String> deviceTokens, String messageTitle, String content) throws Exception {
        IOSListcast listcast = new IOSListcast(appkey,appMasterSecret);
        StringBuilder deviceTokenStr = new StringBuilder();
        //遍历deviceToken集合，拼接成字符串，以","分割
        for(String deviceToken : deviceTokens){
            if(deviceTokenStr.length() != 0){
                deviceTokenStr.append(Constants.COMMA);
            }
            deviceTokenStr.append(deviceToken);
        }
        listcast.setDeviceToken(deviceTokenStr.toString());
        //设定标题和内容
        String message = iosAlert.replace("标题",messageTitle).replace("内容",content);
        JSONObject object = new JSONObject(message);
        listcast.setAlert(object);
        listcast.setBadge( 0); //默认值
        listcast.setSound( "default"); //默认声音
        listcast.setTestMode(); //推送环境:测试环境
//        listcast.setProductionMode();//推送环境:生产环境
        return client.send(listcast);
    }

    /**
     * 推送共通方法
     * @param messageType "1":单播，"0":列播
     * @param clientType "4":ios,"3":安卓
     * @param deviceToken messageType为"1"时，不为空
     * @param deviceTokens messageType为"0"时，不为空
     * @param messageTitle 推送标题
     * @param content 推送内容
     */
    public static UPushResultDto push(String messageType, String clientType, String deviceToken,
                            List<String> deviceTokens, String messageTitle, String content)  throws Exception {
        UmengPushUtils umengPushUtils;
        UPushResultDto uPushResultDto = new UPushResultDto();
        //广播先推送IOS，成功后再推Android
        if(PushCastEnum.BROADCAST.getCode().equals(messageType)){ //广播
            umengPushUtils = new UmengPushUtils("5b29b0bdf29d984653000024", "df0yv8prrmfrygsafu3b7ug9pyzzzbxs");
            uPushResultDto = umengPushUtils.sendIOSBroadcast(messageTitle,content);
            String iosTaskId = uPushResultDto.getData().getTask_id();
            uPushResultDto.getData().setIosTaskId(iosTaskId);
            if("SUCCESS".equals(uPushResultDto.getRet())){
                umengPushUtils = new UmengPushUtils("5b28cd008f4a9d528e000073", "aidl6olpi7kr7etszrdyqwye32tsafps");
                uPushResultDto = umengPushUtils.sendAndroidBroadcast(messageTitle,content);
                uPushResultDto.getData().setAndroidTaskId(uPushResultDto.getData().getTask_id());
                uPushResultDto.getData().setIosTaskId(iosTaskId);
                return  uPushResultDto;
            }
            return uPushResultDto;
          }
        //单播
        //推送给ios设备
        if(ClientTypeEnums.IOS.getCode().equals(clientType)){
            umengPushUtils = new UmengPushUtils("5b29b0bdf29d984653000024", "df0yv8prrmfrygsafu3b7ug9pyzzzbxs");
            //单播
            if(PushCastEnum.UNICAST.getCode().equals(messageType)){
                return umengPushUtils.sendIOSUnicast(deviceToken,messageTitle,content);
            }else if(PushCastEnum.LISTCAST.getCode().equals(messageType)){ //列播
                return umengPushUtils.sendIOSListcast(deviceTokens,messageTitle,content);
            }
//            else if(PushCastEnum.BROADCAST.getCode().equals(messageType)){ //广播
//                return umengPushUtils.sendIOSBroadcast(messageTitle,content);
//            }
        } else { //推送给安卓设备
            umengPushUtils = new UmengPushUtils("5b28cd008f4a9d528e000073", "aidl6olpi7kr7etszrdyqwye32tsafps");
            //单播
            if(PushCastEnum.UNICAST.getCode().equals(messageType)){
                return umengPushUtils.sendAndroidUnicast(deviceToken,messageTitle,content);
            }else if(PushCastEnum.LISTCAST.getCode().equals(messageType)){ //列播
                return umengPushUtils.sendAndroidListcast(deviceTokens,messageTitle,content);
            }
//            else if(PushCastEnum.BROADCAST.getCode().equals(messageType)){ //广播播
//                return umengPushUtils.sendAndroidBroadcast(messageTitle,content);
//            }
        }
        return uPushResultDto;
    }


    public static UPushResultDto queryStatus(String iosTaskId, String androidTaskId)  throws Exception {
        UmengPushUtils umengPushUtils;
        if(iosTaskId != null){
            umengPushUtils = new UmengPushUtils("5b29b0bdf29d984653000024", "df0yv8prrmfrygsafu3b7ug9pyzzzbxs");
            return umengPushUtils.querySendStatus(iosTaskId);
        }else {
            umengPushUtils = new UmengPushUtils("5b28cd008f4a9d528e000073", "aidl6olpi7kr7etszrdyqwye32tsafps");
            return umengPushUtils.querySendStatus(androidTaskId);
        }
    }
}
