package com.tm.wechat.dto.usedcar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.domain.UsedCarAnalysisFileMaintain;
import lombok.Data;

/**
 * Created by pengchao on 2018/8/3.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarAnalysisMaintainDto {

    private String fileType;//附件类型

    private String fileName; //附件名称

    private String isMust; //是否必须

    public UsedCarAnalysisMaintainDto() {
    }

    public UsedCarAnalysisMaintainDto(UsedCarAnalysisFileMaintain usedCarAnalysisFileMaintain) {
        this.fileType = usedCarAnalysisFileMaintain.getFileType();
        this.fileName = usedCarAnalysisFileMaintain.getFileName();
        this.isMust = usedCarAnalysisFileMaintain.getIsMust();
    }
}
