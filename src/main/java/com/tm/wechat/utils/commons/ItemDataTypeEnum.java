package com.tm.wechat.utils.commons;

/**
 * Created by zcHu on 2017/05/09.
 */
public enum ItemDataTypeEnum {


    StringType(1),

    IntegerType(2),

    LongType(3),

    DateType(4),

    ClassType(5),

    ListType(10);

    private int type;

    ItemDataTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
