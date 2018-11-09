package com.tm.wechat.dto.webank;

import lombok.Data;

/**
 * Created by pengchao on 2018/5/31.
 */
@Data
public class WzSignInfoDto {

    private String name; //借款人姓名

    private String idCardNum; //借款人身份证号

    private String carName; //车辆名称

    private String grantBank; //贷款发放银行 微众银行

    private String applyNum; //申请编号

    private String phoneNum; //手机号

    private String totalInvestment; //融资总额

    private String financeTerm; //融资期限

    private String monthPay; //月供

    private String financeAmount; //车辆融资额

    private String purchaseTax; //购置税

    private String retrofittingFee; //加装费

    private String extendedWarranty; //延保

    private String gps; //GPS硬件

    private String compulsoryInsurance; //交强险保费

    private String commercialInsurance; //商业险保费

    private String vehicleTax; //车船税

    private String unexpected; //意外保障

    private String xfws; //先锋卫士

    private String otherFee;//其他费用

    private String creditFinanceAmount; //信用融资额

    private String rates; //贷款年利率

}
