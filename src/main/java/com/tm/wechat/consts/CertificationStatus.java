package com.tm.wechat.consts;

/**
 * 优质认证状态
 * Created by yuanzhenxia on 2017/7/14.
 */
public enum CertificationStatus {

    /**
     * {@code 000 默认}.
     */
    MOREN("000", ""),
    /**
     * {@code 100 微信}.
     */
    WEIXIN("100", "weixin"),

    /**
     * {@code 010 支付宝}.
     */
    ZHIFUBAO("010", "zhifubao"),
    /**
     * {@code 001 淘宝}.
     */
    TAOBAO("001", "taobao");


    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    CertificationStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
