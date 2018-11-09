package com.tm.wechat.utils.push;

/**
 * 友盟消息推送方式枚举类
 *
 * @author qiaomengnan
 * @ClassName: TestStationEnum
 * @Description: 枚举 常量
 * @date 2018/1/7
 */
public enum PushCastEnum {

    LISTCAST("0", "列播"),
    UNICAST("1","单播"),
    BROADCAST("2","广播");

    private String code;
    private String name;

    PushCastEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static PushCastEnum getEnum(String code){
        if(code==null || "".equals(code)){
            return null;
        }
        for (PushCastEnum e : PushCastEnum.values()) {
            if (code.equals(e.getCode())) {
                return e;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

}