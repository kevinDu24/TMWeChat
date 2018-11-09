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
 * Created by pengchao on 2018/8/2.
 * 车300二手车评估维护（暂未使用）
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarAnalysisFileMaintain {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String fileType;//附件类型

    private String fileName; //附件名称

    private String isMust; //是否必须



    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;
}
