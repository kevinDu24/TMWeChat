package com.tm.wechat.consts;

/**
 * 微众交易类型
 * Created by zcHu on 2018/1/8.
 */
public enum WzSubmitType {


    /**
     * {@code 10031 一审提交}.
     */
    NEW("10031", "一审提交"),

    /**
     * {@code 10202 电子签约}.
     */
    SUBMIT("10202", "电子签约"),


    /**
     * {@code 10014 还款卡变更}.
     */
    CARDCHANGE("10014", "还款卡变更"),

    /**
     * {@code 10090 电子签约查询}.
     */
    SIGNQUERY("10090", "电子签约查询"),

    /**
     * {@code 12002 审批结果查询}.
     */
    STATEQUERY("12002", "审批结果查询"),

    /**
     * {@code 10080 微众电子签约信息查询}.
     */
    SIGNINFOQUERY("10080", "微众电子签约信息查询");


    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    WzSubmitType(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
