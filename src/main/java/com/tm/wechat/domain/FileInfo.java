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
 * 请款文件信息
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileInfo {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String applyNum; //申请编号

//    private String bankCardImg; //银行卡照片
//
//    private String invoice; // 发票照片
//
//    private String invoiceQRCodeImg; // 发票二维码
//
//    private String cardListImg; //工行卡单
//
//    private String cardListSignImg; //工行卡单面签
//
//    private String bankDetailedImg; //工行细则
//
//    private String bankDetailedSignImg; //工行细则面签
//
//    private String paymentAgreementImg; //工行代扣协议

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
