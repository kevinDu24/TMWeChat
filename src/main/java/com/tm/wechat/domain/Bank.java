package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 微众银行卡匹配表
 * Created by LEO on 16/9/27.
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bank {

    @Id
    private String bin; // 卡BIN
    private String name; //银行名称
    private String bankNum; //卡联行号

}
