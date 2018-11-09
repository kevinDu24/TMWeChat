package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/10/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsuranceTrialResult {
    private String commercialInsurance ;//商业险
    private String compulsoryInsurance ;//交强险
    private String vehicleTax;//车船税
}
