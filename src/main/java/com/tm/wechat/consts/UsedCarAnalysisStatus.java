package com.tm.wechat.consts;

/**
 * Created by pengchao on 2018/8/2.
 */
public enum UsedCarAnalysisStatus {


    /**
     * {@code 0 HPL}.
     */
    UNSUCCESSFUL("0", "评估未出"),

    /**
     * {@code 1 微众}.
     */
    SUCCESS("1", "报告已出");



    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    UsedCarAnalysisStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getCode(String code) {
        if(code == null || "".equals(code)){
            return UNSUCCESSFUL.value();
        }
        for (UsedCarAnalysisStatus usedCarAnalysisStatus : UsedCarAnalysisStatus.values()) {
            if (usedCarAnalysisStatus.code().equals(code)) {
                return usedCarAnalysisStatus.value();
            }
        }
        return "";
    }
    public static String getCodeByValue(String value) {
        if(value == null || "".equals(value)){
            return UNSUCCESSFUL.code();
        }
        for (UsedCarAnalysisStatus usedCarAnalysisStatus : UsedCarAnalysisStatus.values()) {
            if (usedCarAnalysisStatus.value().equals(value)) {
                return usedCarAnalysisStatus.code();
            }
        }
        return "";
    }
}
