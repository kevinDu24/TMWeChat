package com.tm.wechat.consts;

/**
 * 预审批状态类型
 * Created by zcHu on 2017/5/11.
 */
public enum ApplyType {


    /**
     * {@code 0 新建申请}.
     */
    NEW("0", "新建申请"),

    /**
     * {@code 1 微众一审失败}.
     */
    REFUSE_WX("1", "拒绝"),
    /**
     * {@code 2 微众成功但天启预审批失败}.
     */
    REFUSE_APP("2", "拒绝"),

    /**
     * {@code 2 微众一审、预审批都通过}.
     */
    FIRST_PASS("2", "预审批通过"),

    /**
     * {@code 4 微众二审通过}.
     */
    SECOND_PASS("4", "通过"),
    /**
     * {@code 5 微众二审失败}.
     */
    SECOND_REFUSE("5", "二审拒绝"),
    /**
     * {@code 6 订单终止}.
     */
    CANCEL("6", "订单终止");






    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    ApplyType(String code, String value) {
        this.code = code;
        this.value = value;
    }


    public static String getCode(String code) {
        for (ApplyType applyType : ApplyType.values()) {
            if (applyType.code().equals(code)) {
                return applyType.value();
            }
        }
        return null;
    }
}
