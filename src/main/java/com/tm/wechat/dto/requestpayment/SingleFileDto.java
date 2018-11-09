package com.tm.wechat.dto.requestpayment;

import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2018/5/16.
 */
@Data
public class SingleFileDto {

    private String name; //文件名称

    private List<String> urlList; //附件url

    private String required; //是否必需
}
