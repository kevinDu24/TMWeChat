package com.tm.wechat.consts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengchao on 2018/5/15.
 */
public enum OriginType {

    /**
     * {@code 0 HPL}.
     */
    HPL("0", "HPL","1","0"),

    /**
     * {@code 1 微众}.
     */
    WE_BANK("1", "微信","2","0"),
    /**
     * {@code 2 微盟贷（在线助力融）}.  很久以前叫在线助力融，现在叫微盟贷，注意如果有问题，注意看库中有没有存文字的。
     */
    ONLINE_HELP("2", "微盟贷","1","0"),
    /**
     * {@code 3 工行产品}.
     */
    ICBC("3", "工盟贷","1","1"),
    /**
     * {@code 4 新网产品}.
     */
    XW_BANK("4", "新盟贷","1","1");



    private final String code;
    private final String value;
    private final String sourceType; //产品来源  1 App; 2 微信
    private final String haveReturn; //是否有退回情况 0 否; 1 是

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }
    public String sourceType() {
        return this.sourceType;
    }
    public String haveReturn() {
        return this.haveReturn;
    }

    OriginType(String code, String value,String sourceType,String haveReturn) {
        this.code = code;
        this.value = value;
        this.sourceType = sourceType;
        this.haveReturn = haveReturn;
    }

    public static String getCode(String code) {
        if(code == null || "".equals(code)){
            return HPL.value();
        }
        for (OriginType originType : OriginType.values()) {
            if (originType.code().equals(code)) {
                return originType.value();
            }
        }
        return null;
    }
    public static String getCodeByValue(String value) {
        if(value == null || "".equals(value)){
            return HPL.code();
        }
        for (OriginType originType : OriginType.values()) {
            if (originType.value().equals(value)) {
                return originType.code();
            }
        }
        return null;
    }

    public static String getValueByCode(String code) {
        if(code == null || "".equals(code)){
            return HPL.value();
        }
        for (OriginType originType : OriginType.values()) {
            if (originType.code().equals(code)) {
                return originType.value();
            }
        }
        return null;
    }

    /**
     * 根据type获取到 来源类型  1 app 2 微信 0或空 所有
     * @param type
     * @return
     */
    public static List<String> getCodeListBySourceType(String type){
        List<String> codeList = new ArrayList<>();
        for (OriginType originType : OriginType.values()) {
            //如果是查所有,或匹配上了产品类型
            if(type == null || type.isEmpty() ||  "0".equals(type) || type.equals(originType.sourceType())){
                codeList.add(originType.code());
            }
        }
        return codeList;
    }

    /**
     * 获取到有退回情况的产品
     * @return
     */
    public static List<String> getCodeListByHaveReturn(){
        List<String> codeList = new ArrayList<>();
        for (OriginType originType : OriginType.values()) {
            if("1".equals(originType.haveReturn())){
                codeList.add(originType.code());
            }
        }
        return codeList;
    }
}
