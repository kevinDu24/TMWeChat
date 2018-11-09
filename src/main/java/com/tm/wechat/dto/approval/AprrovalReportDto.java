package com.tm.wechat.dto.approval;

import com.tm.wechat.annotation.ExcelTitle;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.utils.commons.DateUtils;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * Created by qiaohao on 2017/5/6.
 */
@Data
public class AprrovalReportDto {

    private String name; // 申请人姓名

    private String applyNum; // 申请编号

    private String status; // 状态

    private Date applyDate; // 日期

    private String fpName; // 经销商代码

    private String beginTime; // 开始时间

    private String endTime; // 结束时间

    private String searchType; //searchType 0:预审批、1：在线申请

    public AprrovalReportDto(){


    }

    public AprrovalReportDto(Map<String,Object> map) {
        this.name = CommonUtils.getStr(map.get("name"));
        this.applyNum = CommonUtils.getStr(map.get("applynum"));
        this.status = CommonUtils.getStr(map.get("status"));
        this.applyDate = CommonUtils.getDate(map.get("applydate"));
        this.fpName = CommonUtils.getStr(map.get("fpname"));
    }

    @ExcelTitle(value = "姓名",sort = 1)
    public String getName() {
        return name;
    }

    @ExcelTitle(value = "申请编号",sort = 2)
    public String getApplyNum() {
        return applyNum;
    }

    @ExcelTitle(value = "状态",sort = 3)
    public String getStatusName() {
        if ("0".equals(status)) {
            return "待提交";
        } else if ("100".equals(status) || "2000".equals(status)) {
            return "审批中";
        } else if ("1000".equals(status) || "3000".equals(status)) {
            return "审批通过";
        } else if ("1100".equals(status) || "3500".equals(status)) {
            return "审批拒绝";
        } else if ("4000".equals(status)) {
            return "退回待修改";
        }
        return "";
    }

    @ExcelTitle(value = "申请时间",sort = 4)
    public String getConfirmDateExport() {
        return DateUtils.getStrDate(applyDate,DateUtils.simpleDateFormat);
    }

    @ExcelTitle(value = "创建者",sort = 5)
    public String getFpName() {
        return fpName;
    }
}
