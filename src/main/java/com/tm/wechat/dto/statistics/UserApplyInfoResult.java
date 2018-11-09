package com.tm.wechat.dto.statistics;

import lombok.Data;

/**
 * @program: TMWeChat
 * @description: 用户预审批信息数量结果
 * @author: ChengQC
 * @create: 2018-10-31 13:09
 **/
@Data
public class UserApplyInfoResult implements Comparable<UserApplyInfoResult> {

    private String fpName;        //Fp名字

    private String realName;    //真实姓名

    private int num;         //数量

    public UserApplyInfoResult() {
    }

    public UserApplyInfoResult(Object[] objs) {
        this.fpName  = objs[0] == null ? "" : objs[0].toString();
        this.num = objs[1] == null ? 0 : Integer.parseInt(objs[1].toString());
    }


    @Override
    public int compareTo(UserApplyInfoResult o) {
        if(this.num >= o.num){
            return -1;
        }
        return 1;
    }
}