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
import java.util.Date;

/**
 * Created by pengchao on 2018/8/28.
 * 视频面签记录
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoSignRecord {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private Date endTime;//结束时间

    private String applyNum; //申请编号

    private String state; //面签状态，0正在面签，1面签结束

    private String signAccount;//签约账号

    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;
}
