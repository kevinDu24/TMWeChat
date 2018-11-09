//package com.tm.wechat.dto.gxb;
//
//import com.tm.wechat.domain.gxb.*;
//import com.tm.wechat.dto.apply.ApplyDto;
//import com.tm.wechat.dto.approval.ApplyFromDto;
//import lombok.Data;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by Leadu on 2017/7/21.
// */
//@Data
//public class GxbReportDto {
//    private String approvalUuid;
//
//    private ApplyFromDto applyFromDto;
//
//    private ApplyDto applyDto;
//
//    private WechatInfo wechatInfo;
//
//    private Double priceRatio;// 车辆融资价/车辆当地销售价
//
//    private Date applyDate; //申请借款日期
//
//    private String phoneNumberCity; //主贷人 主手机属地
//
//    private String vicePhoneNumberCity; //主贷人 副手机属地
//
//    private String mateMobileCity; // 配偶手机属地
//
//    private String contact1MobileCity; //紧急联系人1属地
//
//    private String contact2MobileCity; //紧急联系人2属地
//
//    private int wechatContactNum; //微信联系人人数
//
//    private int wechatContactGroupNum; //微信群个数
//
//    private String idCardCity; //身份证地址属地
//
//    private String driveLicenceCity; // 驾驶证地址属地
//
//    private String isOnTargetIdAdr; // 身份证地址是否命中淘宝地址 {0未命中；1命中}
//
//    private String isOnTargetDriveAdr; // 驾驶证地址是否命中淘宝地址 {0未命中；1命中}
//
//    private String isOnTargetWorkAdr; // 单位地址是否命中淘宝地址 {0未命中；1命中}
//
//    private int wechatShareContactNum; //微信共享联系人人数
//
//    private int wechatShareContactGroupNum; //微信共享群个数
//
//    private TaobaoInfo taobaoInfo;
//
//    private List<TaobaoConsigneeAddressesDto> taobaoConsigneeAddressesDtoList;
//
//    private  AlipayInfo alipayInfo;
//
//    private int alipayIncomeTradeNum; // 支付宝收入交易总笔数
//
//    private String alipayIncomeTradeAmountNum; // 支付宝收入交易总金额
//
//    private int alipayPayTradeNum; // 支付宝支出交易总笔数
//
//    private String alipayPayTradeAmountNum; // 支付宝支出交易总金额
//
//    private  List<AlipayBindedBankCards> alipayBindedBankCardsList;
//
//    private  List<AlipayPaymentAccounts> alipayPaymentAccountsList;
//}
