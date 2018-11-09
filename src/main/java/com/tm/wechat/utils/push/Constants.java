package com.tm.wechat.utils.push;

/**
 * @author qiaomengnan
 * @ClassName: Constants
 * @Description: 字符串常量配置
 * @date 2018/1/7
 */
public class Constants {

    /**
     * 空格分隔符
     */
    public static final String ASCIISPLIT30 = String.valueOf((char)Integer.parseInt("30", 10));

    /**
     * 英文字符逗号 "，"
     */
    public static final String COMMA = ",";

    public static final String  COMPONENT_SCAN = "cn.com.leadu.cmsxc";

    public static final String  ENABLE_FEIGN_CLIENTS = "cn.com.leadu.cmsxc";

    public static final String MAPPER_SCAN =  "cn.com.leadu.cmsxc.data.*.dao";
    // 注册获取短信验证码的方法路径，保存到DB用
    public static final String SENDCODE_SCAN =  "cn.com.leadu.cmsxc.appuser.controller.SystemUserController.sendCode";
    // 找回密码获取短信验证码的方法路径，保存到DB用
    public static final String SENDCODEFINDPASSWORD_SCAN =  "cn.com.leadu.cmsxc.appuser.controller.SystemUserController.sendCodeFindPassword";
    // 获取短信验证码的方法路径，保存到DB用
    public static final String SENDMESSAGE_SCAN =  "cn.com.leadu.cmsxc.appuser.service.impl.PlateUploadServiceImpl.uploadPlateInfor";
    // 授权成功发送短信的方法路径，保存到DB用
    public static final String AUTHORIZATION_SCAN =  "cn.com.leadu.cmsxc.system.service.impl.AuthorizationServiceImpl.authorization";
    // 获取短信验证码的方法路径，保存到DB用 申请授权处理发送短信
    public static final String SENDMESSAGE_APPLY_SCAN =  "cn.com.leadu.cmsxc.appuser.service.impl.AuthorizationServiceImpl.applyAuthorization";
    // 获取短信的方法路径，保存到DB用 收车任务取消处理发送短信
    public static final String SENDMESSAGE_TASK_CANSEL_SCAN =  "cn.com.leadu.cmsxc.external.service.impl.TaskPushServiceImpl.pushRecoveryCanal";
    // 非独家任务线索命中时给委托公司审批专员发送短信
    public static final String CLUE_TARGET_SCAN =  "cn.com.leadu.cmsxc.appuser.service.impl.PlateUploadServiceImpl.pushMessage";
    // 独家任务线索命中时给委托公司审批专员发送短信
    public static final String CLUE_TARGET_SOLE_SCAN =  "cn.com.leadu.cmsxc.appuser.service.impl.PlateUploadServiceImpl.pushMessageSole";
    // 获取短信的方法路径，保存到DB用 推送收车任务发送短信
    public static final String SENDMESSAGE_TASK_PUSH_SCAN =  "cn.com.leadu.cmsxc.external.service.impl.TaskPushServiceImpl.pushVehicleTask";


}
