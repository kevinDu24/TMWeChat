package com.tm.wechat.consts;

/**
 * Created by pengchao on 2017/7/17.
 */
public enum  UsedCarEvaluationStatus {


    /**
     * {@code 0 无需评估}.
     */
    NEW("0", "无需评估"),

    /**
     * {@code 1 未评估}.
     */
    NOT_SUBMIT("1", "未评估"),
    /**
     * {@code   1100 未通过}.
     */
    REFUSE("1100", "拒绝"),

    /**
     * {@code 100  评估中}.
     */
    SUBMIT("100", "评估中"),

    /**
     * {@code 1000 通过}.
     */
    PASS("1000", "通过");



    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    UsedCarEvaluationStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
