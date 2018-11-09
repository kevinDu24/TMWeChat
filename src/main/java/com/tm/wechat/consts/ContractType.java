package com.tm.wechat.consts;

/**
 * Created by pengchao on 2018/1/8.
 */
public enum ContractType {
    /**
     * {@code 0 待签约}.
     */
    WAIT_SIGN("0", "待签约"),
    /**
     * {@code 1 已签约}.
     */
    SIGNED("1", "已签约"),

    /**
     * {@code 9 未到签约阶段签约}.
     */
    NO_SIGN("9", "无需签约");

    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    ContractType(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
