package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.io.Serializable;
import java.util.Date;

/**
 * 预审批申请信息表  applyInfo 以后就不用了
 * (20181015 修改，为了以后的拓展性，此表修改以前的字段，调整为下面结构)
 * -- By ChengQiChuan 2018/10/15 17:08
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyInfoNew implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String approvalUuid; //申请人的唯一标识

    private String applyNum; //申请单号(from主系统)

    @CreatedDate
    private Date createTime;    //创建时间

    @CreatedBy
    private String createUser;  //创建用户

    @LastModifiedDate
    private Date updateTime;    //修改时间

    @LastModifiedBy
    private String updateUser;  //修改用户

    private String version; // 版本号

    private String origin; //来源【0：app本身、1：微信、2：在线助力融、3.工行产品】

    private String hplStatus; //hpl 天启审批状态 详情见：ApprovalType

    private String productStatus; //产品审批状态【100：审批中、1000：通过、1100：拒绝、300:退回待修改】 (产品为 工行 ， 新网 的时候该地方有值，为产品合作商审批的值)

    private String wxState; //微信保单信息完整度【0：未完善、1：已完善】 （特殊情况：当 origin 为 1 微信端过来的单子 的时候这个地方才有值）

    private String hplReason; //hpl 天启退回原因

    private String productReason; //产品商 退回原因

    private String approvalFileState;//预审批文件提交到主系统状态【0：未提交、1：已提交】

    private Date approvalSubmitTime;//预审批提交时间

    private Double approvalLon;//预审批提交经度

    private Double approvalLat;//预审批提交纬度
}
