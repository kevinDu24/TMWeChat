package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by pengchao on 2018/7/18.
 * 车300二手车评估记录（暂未使用）
 */

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarAnalysisRecord {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String analysisId;//评估id

    private String status;//状态

    private String c2b_price; //c2b价格

    private String b2b_price; //b2b价格

    private String b2c_price; //b2c价格（我们需要的）

    private String report_url;//详细估值报告链接

    private String error_msg;//错误信息

    @CreatedDate
    private Date createTime;


}
