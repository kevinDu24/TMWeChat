package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.dto.usedcar.UsedCarAnalysisDto;
import com.tm.wechat.utils.commons.CommonUtils;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by pengchao on 2018/7/18.
 * 车300二手车评估表（暂未使用）
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarAnalysis {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String uniqueMark;//评估单号

    private String vin;//车架号

    private String token; //查询编号

    private String modelId; //车型id

    private String modelName; //车型名称

    private String zone; //城市id

    private String regDate;//车辆上牌日期 2018-08

    private String color;//颜色

    private Double mile;//车辆行驶公里数

    private String interior;//车内装饰（优良中差）

    private String surface; //漆面状况（优良中差）

    private String workState; //工况状况（优良中差）

    private Integer transferTimes; //过户次数 可选

    private String makeDate;//车辆出厂日期 2018-08 可选

    private String operator; //操作业务人员姓名

    private String operatorPhoneNum; //操作业务人员手机号

    private String status; //评估状态

    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;

    public UsedCarAnalysis(UsedCarAnalysisDto dto) {
        this.vin = dto.getVin();
        this.token = CommonUtils.CARTHREEHUNDRED_TOKEN;
        this.modelId = dto.getModelId();
        this.modelName = dto.getModelName();
        this.zone = dto.getZone();
        this.regDate = dto.getRegDate();
        this.color = dto.getColor();
        this.mile = dto.getMile();
        this.interior = dto.getInterior();
        this.surface = dto.getSurface();
        this.workState = dto.getWorkState();
        this.transferTimes = dto.getTransferTimes();
        this.makeDate = dto.getMakeDate();
        this.operator = dto.getOperator();
        this.operatorPhoneNum = dto.getOperatorPhoneNum();
        this.uniqueMark = dto.getUniqueMark();
    }

    public UsedCarAnalysis() {
    }
}
