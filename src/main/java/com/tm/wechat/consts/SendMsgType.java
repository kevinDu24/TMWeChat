package com.tm.wechat.consts;

/**
 * Created by pengchao on 2018/6/28.
 */
public enum SendMsgType {
    /**
     * {@code 1 在线助力融提交微众预审页面}.
     */
    CONFIRM_MSG("1", "微众信息确认"),
    /**
     * {@code 2 指定申请编号四要素验证}.
     */
    FOUR_ELEMENTS("2", "四要素验证"),

    /**
     * {@code 3 客户还款卡地址录入}.
     */
    ADDRESS_INPUT("3", "地址录入");

    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    SendMsgType(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
