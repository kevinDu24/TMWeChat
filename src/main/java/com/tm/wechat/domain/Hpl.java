package com.tm.wechat.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by wangbiao on 16/12/16.
 * 目前没用
 */
@Data
@Entity
public class Hpl {

    @Id
    private Long id;
    private String data;
}
