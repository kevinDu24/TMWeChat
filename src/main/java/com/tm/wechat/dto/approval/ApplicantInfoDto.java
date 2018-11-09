package com.tm.wechat.dto.approval;

import com.tm.wechat.dto.apply.JointInfoDto;
import lombok.Data;

/**
 * Created by pengchao on 2018/3/30.
 */
@Data
public class ApplicantInfoDto {

    private IdCardInfoDto idCardInfoDto;

    private MateInfoDto mateInfoDto;

    private JointInfoDto jointInfoDto;

    public ApplicantInfoDto() {
    }

    public ApplicantInfoDto(IdCardInfoDto idCardInfoDto, MateInfoDto mateInfoDto, JointInfoDto jointInfoDto) {
        this.idCardInfoDto = idCardInfoDto;
        this.mateInfoDto = mateInfoDto;
        this.jointInfoDto = jointInfoDto;
    }
}
