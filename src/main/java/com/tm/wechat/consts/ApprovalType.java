package com.tm.wechat.consts;

/**
 * 预审批状态类型
 * Created by zcHu on 2017/5/11.
 */
public enum ApprovalType {


    /**
     * {@code 0 新建申请}.
     */
    NEW("0", "新建申请"),

    /**
     * {@code 100 申请中}.
     */
    SUBMIT("100", "申请中"),
    /**
     * {@code 300 退回待修改，有法院非经济案件}.
     */
    BACK("300", "退回待修改"),

    /**
     * {@code 1000 通过}.
     */
    PASS("1000", "通过"),

    /**
     * {@code 1100 拒绝}.
     */
    REFUSE_NOREASON("1100", "拒绝"),

    /*  预审批完善申请*/
    /**
     * {@code 2000 申请提交待审批}.
     */
    WAIT_APPROVAL("2000", "待审批"),
    /**
     * {@code 3000 申请提交审批通过}.
     */
    APPROVAL_PASS("3000", "通过"),
    /**
     * {@code 3500 申请提交审批拒绝}.
     */
    APPROVAL_REFUSE("3500", "拒绝"),
    /**
     * {@code 4000 申请提交被退回}.
     */
    APPROVAL_BACK("4000", "被退回");


    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    ApprovalType(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
