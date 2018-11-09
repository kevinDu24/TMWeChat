package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 微众银行名称 产品类型对应字典表
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankNameOrigin {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String Id;

    private String name; //银行名称

    private String originType; //产品类型

}
