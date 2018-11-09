package com.tm.wechat.consts;

/**
 * Created by pengchao on 2018/3/19.
 */
public enum ContractSignStatus {


    /**
     * {@code 0 未完成}.
     */
    NEW("0", "未完成"),

    /**
     * {@code 100 已完成}.
     */
    SUBMIT("1", "已完成");

    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }


    ContractSignStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
