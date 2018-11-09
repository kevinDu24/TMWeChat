package com.tm.wechat.consts;

/**
 *  @author ChengQiChuan
 *  @date 2018/10/29 18:40
 *  Description: 预审批申请状态表类型
 */

public enum ApplyInfoStatusType {

    /**
     * hpl 天启
     */
    HPL("0","#hpl"),

    /**
     * WZ_BANK 微众预审批
     */
    WZ_BANK("2","#wz_bank"),


    /**
     * ICBC 工行预审批
     */
    ICBC("3","#icbc"),

    /**
     * XW_BANK 新网风控
     */
    XW_BANK("4","#xw_bank"),

    /**
     * XW_BANK_PHOTO_MSG 新网大头照
     */
    XW_BANK_PHOTO_MSG("4","#xw_bank#photo_msg");

    private final String code;      //产品类型 对应  OriginType
    private final String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    ApplyInfoStatusType(String code, String value) {
        this.code = code;
        this.value = value;
    }
}