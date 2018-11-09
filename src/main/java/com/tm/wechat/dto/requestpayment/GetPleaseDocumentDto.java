package com.tm.wechat.dto.requestpayment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2018/5/16.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPleaseDocumentDto {
    
    private String name; //文件名称
    
    private String required; //是否必需

    private String uploadState; //上传状态

    private List<String> urlList; //附件url

}
