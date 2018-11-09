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
import java.io.Serializable;
import java.util.Date;

/**
 * 融资预审批纵向存储表
 * Created by zcHu on 2017/5/9.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Approval implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String itemKey; //每个字段的key

    private String uniqueMark; // 客户唯一标识

    /**
     * 1-字符数据
     * 2-INT32数据
     * 3-INT64数据
     * 4-DATETIME数据
     * 10-List
     */
    private Integer itemType;

    private String itemStringValue;

    private Integer itemInt32Value;

    private Long itemInt64Value;

    private Date itemDataTimeValue;

    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;

    private String version; // 版本号
}
