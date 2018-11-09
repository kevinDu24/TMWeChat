package com.tm.wechat.dto.sysUser;

import lombok.Data;

/**
 * Created by pengchao on 2018/5/2.
 */
@Data
public class UserProductInfoDto {
    
    private String productId; //产品id

    private String productName; //产品name
    
    private String bacllx; //车辆类型(新车,二手车)
    
    private String basqlx; //申请类型(个人，企业)

    private String state; //0被禁用,1可用


}
