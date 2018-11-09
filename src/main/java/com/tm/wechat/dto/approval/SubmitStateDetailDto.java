package com.tm.wechat.dto.approval;

import lombok.Data;

/**
 * Created by pengchao on 2018/8/31.
 */
@Data
public class SubmitStateDetailDto {

    private String name;//姓名

    private String productSource; //产品来源

    private String state; //状态
    
    private String operation; //操作

    private int code; //0:错误类(红色) 1:通过（绿色） 2：普通（黑色）

    private String reason; //原因
     

}
