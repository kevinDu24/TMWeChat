package com.tm.wechat.dto.sale;

import lombok.Data;

/**
 * Created by LEO on 16/10/31.
 */
@Data
public class InsuranceCalculateRecDto {
    private String baclpl; //车辆排量
    private String badycs; //抵押城市
    private String bazwsl; //座位数量
    private String baszzr; //三者责任险
    private String baclzd; //车辆指导价
    private String ryzrxsj; //车上人员责任险（司机）
    private String ryzrxsjxe; //车上人员责任险（司机）限额
    private String ryzrxck; //车上人员责任险（乘客）
    private String ryzrxckxe; //车上人员责任险（乘客）限额
    private String babjmp; //车上人员责任险不计免赔
    private String bacshh; //车身划痕险
    private String badqxe; //盗抢险
    private String cshhxe; //车身划痕险限额
    private String cshhbjmp; //车身划痕险不计免赔
    private String blddbs; //玻璃单独破粹险
    private String babxcd; //产地
    private String bazdzx; //指定专修险
    private String bayyfs; //营运方式
    private String bacldw; //车辆吨位
    private String bacxlx; //车型类型
    private String sfraxb; //是否融安信宝
}
