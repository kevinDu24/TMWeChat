package com.tm.wechat.dto;

import com.tm.wechat.domain.ApplyInfoNew;
import com.tm.wechat.dto.approval.ApplyRecordDto;
import com.tm.wechat.dto.approval.SubmitStateDetailDto;
import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2018/8/31.
 * 预审批状态页面返回信息
 */
@Data
public class SubmitStateDto {
    private List<SubmitStateDetailDto> applicantList;
    private List<SubmitStateDetailDto> mateList;
    private List<ApplyRecordDto> applyRecordDtoList;
    private ApplyInfoNew applyInfo;

}
