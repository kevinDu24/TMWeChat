//package com.tm.wechat.dto.gxb;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Data;
//
//import java.util.List;
//
///**
// * 淘宝子订单实体类
// *
// * Created by xuhao on 2017/7/18.
// */
//@Data
//public class TaobaoOrderDto {
//
//    @JsonProperty("tradeNumber")
//    private String tradeNumber;//支付交易号
//
//    @JsonProperty("orderNumber")
//    private String orderNumber;//订单号
//
//    @JsonProperty("tradeStatusName")
//    private String tradeStatusName;//交易状态
//
//    @JsonProperty("tradeTypeName")
//    private String tradeTypeName;//交易类型
//
//    @JsonProperty("createTime")
//    private String createTime;//创建时间
//
//    @JsonProperty("endTime")
//    private String endTime;//订单完成时间
//
//    @JsonProperty("payTime")
//    private String payTime;//支付时间
//
//    @JsonProperty("totalQuantity")
//    private Integer totalQuantity;//商品总数量
//
//    @JsonProperty("postFees")
//    private Double postFees;//邮费
//
//    @JsonProperty("actualFee")
//    private Double actualFee;//实付金额
//
//    @JsonProperty("virtualSign")
//    private Boolean virtualSign;//是否虚拟商品
//
//    @JsonProperty("address")
//    private EcommerceConsigneeAddressesDto address;//交易地址
//
//    @JsonProperty("subOrders")
//    private List<TaobaoSubOrderDto> subOrders;//子订单集合
//}
