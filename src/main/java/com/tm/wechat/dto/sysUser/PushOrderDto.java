package com.tm.wechat.dto.sysUser;

import lombok.Data;

/**
 * 通过手机号分享至客户微信号实体类
 *
 * Created by huzongcheng on 18/1/5.
 */
@Data
public class PushOrderDto {
    private String phoneNum; //客户电话
    private String userName; //经销商账号
}
