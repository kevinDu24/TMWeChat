package com.tm.wechat.consts;

/**
 * Created by pengchao on 2018/8/31.
 */
public enum SubmitStates {

    NO_EXIST(0, "未获取到订单"),

    PASS(1, "通过"),

    REFUSE(2, "拒绝"),

    WAIT(3, "等待审核"),

    BACK(4, "退回补充材料"),

    INCORRECT(5, "四要素不一致"),

    FAIL(6, "失败");




    private final int code;
    private final String value;

    public int code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    SubmitStates(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
