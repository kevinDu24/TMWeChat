package com.tm.wechat.utils;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;

/**
 * Created by huzongcheng on 2017/3/30.
 */
public class PushMsgToSingleDeviceUtil {

    /**
     * 向单个设备推送消息。
     *
     * @param deviceType android:3;ios:4
     * @throws PushClientException
     * @throws PushServerException
     */
    public void pushMsgToSingleDevice(String channelId, String deviceType) throws PushClientException, PushServerException {
        /*1.
         创建PushKeyPair
         *用于app的合法身份认证
         *apikey和secretKey可在应用详情中获取
         */
        String apiKey = "";
        String secretKey = "";
        String mesaage = "";
        if ("3".equals(deviceType)) {
            apiKey = "BGVhCMhvBN6f6jw0MW7dCj84";
            secretKey = "w7sAsX9mFlfqTxM4zMvhGreLuyG6mGmK";
            mesaage = "{\"title\":\"安全提示\",\"description\":\"当前账号已在另一台设备登录！请确认。如非本人操作请及时修改密码！\"}";
        } else if ("4".equals(deviceType)) {
            apiKey = "RHEpyHikMsSHiYnb4STGt0rZ";
            secretKey = "oKRyd83yBFx5GazFklOjztcNUoGwRNX3";
            mesaage = "{\"aps\":{\"alert\":\"当前账号已在另一台设备登录！请确认。如非本人操作请及时修改密码！\",\"sound\":\"default\"}}";
        }
        PushKeyPair pair = new PushKeyPair(apiKey, secretKey);

        // 2. 创建BaiduPushClient，访问SDK接口
        BaiduPushClient pushClient = new BaiduPushClient(pair,
                BaiduPushConstants.CHANNEL_REST_URL);

        // 3. 注册YunLogHandler，获取本次请求的交互信息
        pushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {
            // 4. 设置请求参数，创建请求实例l
            PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest().
                    addChannelId(channelId).
                    addMsgExpires(new Integer(3600)).   //设置消息的有效时间,单位秒,默认3600*5.
                    //addMessageType(0).             //设置消息类型,0表示透传消息,1表示通知,默认为0.
                    addMessage(mesaage).
                    addDeviceType(Integer.parseInt(deviceType));      //设置设备类型，deviceType => 1 for web, 2 for pc,
            //3 for android, 4 for ios, 5 for wp.
            if ("3".equals(deviceType)){
                request.addMessageType(0);             //设置消息类型,0表示透传消息,1表示通知,默认为0.
            }else if("4".equals(deviceType)){
                request.addMessageType(1);             //设置消息类型,0表示透传消息,1表示通知,默认为0.
                request.addDeployStatus(2);     //仅IOS应用推送时使用，默认值为null，取值如下：1：开发状态 2：生产状态
            }
            // 5. 执行Http请求
            PushMsgToSingleDeviceResponse response = pushClient.
                    pushMsgToSingleDevice(request);
        } catch (PushClientException e) {
            //ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,
            //'true' 表示抛出, 'false' 表示捕获。
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            }
        }
    }
}
