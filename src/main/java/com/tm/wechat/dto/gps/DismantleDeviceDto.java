package com.tm.wechat.dto.gps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by LEO on 16/11/8.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DismantleDeviceDto {
    private String dismantlePersonName;//拆除师傅姓名
    private String dismantlePersonPhone;//拆除师傅电话
    private String dismantleReason;//拆除原因
    private String providerName; //拆除属性名称
    private String submitPersonPhone; // 提交人手机号码
    private String customerName;//车主姓名
    private String vin;//车架号
    private List<ApplyTaskDto> applyTasks; //订单集合
}
