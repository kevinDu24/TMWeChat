package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by pengchao on 2018/3/22.
 * 请款保单信息表
 */

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsurancePolicy {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

//    private String sn; //sn号
//
//    private String commercialInsurance ;//商业险
//
//    private String compulsoryInsurance ;//交强险
//
//    private String registration; // 登记证
//
//    private String drivingLicense; // 行驶证

    private String applyNum; //申请编号

    @Column(length = 100000)
    private String fileList;

    private String name;

    private String required; //是否必需

    private String state; // 0,"", null:未上传,1:已上传

    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;

}
