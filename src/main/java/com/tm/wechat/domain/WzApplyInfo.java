package com.tm.wechat.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by pengchao on 2018/1/5.
 * 在线助力融微众申请表
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "wzApplyInfo")
public class WzApplyInfo {
        @Id
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid",strategy = "uuid")
        private String id;

        private String uniqueMark; //app唯一标识

        private String wxId;//提交微众一审的唯一标识

        private String openId; //微信openID

        private String applyNum; //主系统申请编号

        private String name; //姓名

        private String phoneNum; //手机号

        private String bank; //开户银行

        private String bankNum; //银联号

        private String bankCardNum; //银行卡号

        private String idType; //证件提交类型 01 身份证

        private String idCard; //证件号

        private String ip; //ip

        private String locationType; //地址位置信息 {locationType: LBS提交类型,locationData:LBS数据,netWortType:网络提交类型}
        private String locationData;
        private String netWortType;

        @LastModifiedDate
        private Date updateTime; //更新时间

        @CreatedDate
        private Date createTime; //提交时间
        private String reason; //失败原因
        private String status; //状态
        private String signStatus;//签约状态
        private String signDate; //签约返回结果
        private String fpName;//FP账号

}
