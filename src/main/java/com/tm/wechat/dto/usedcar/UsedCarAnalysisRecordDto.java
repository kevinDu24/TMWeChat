package com.tm.wechat.dto.usedcar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.domain.UsedCarAnalysisRecord;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * Created by pengchao on 2018/8/2.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarAnalysisRecordDto {

    private String create_user;//评估用户名
    private String id;//订单号
    private String status;//评估状态（0,"",null：失败，1：成功）
    private String update_time;//评估时间
    private String model_name;//车型名称
    private String report_url;//报告url（状态为0 时显示报告url）
    private String error_msg;//失败原因（状态为1时显示原因）


    public UsedCarAnalysisRecordDto(Object[] objs){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.create_user = objs[0] == null ? "": objs[0].toString();
        this.id = objs[1] == null ? "": objs[1].toString();
        this.status = objs[2] == null ? "": objs[2].toString();
        this.update_time = objs[3] == null ? "": sdf.format(objs[3]);
        this.model_name = objs[4] == null ? "": objs[4].toString();
        this.report_url = objs[5] == null ? "" : objs[5].toString();
        this.error_msg = objs[6] == null ? "" : objs[6].toString();
    }

    public UsedCarAnalysisRecordDto(UsedCarAnalysisRecord usedCarAnalysisRecord) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.id = usedCarAnalysisRecord.getAnalysisId();
        this.status = usedCarAnalysisRecord.getStatus();
        this.update_time = sdf.format(usedCarAnalysisRecord.getCreateTime());
        this.report_url = usedCarAnalysisRecord.getReport_url();
        this.error_msg = usedCarAnalysisRecord.getError_msg();
    }
}
