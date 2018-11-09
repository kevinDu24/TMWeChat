//package com.tm.wechat.dao.gxb;
//
//import com.tm.wechat.domain.gxb.AlipayTradeInfo;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//
///**
// * Created by Leadu on 2017/7/20.
// */
//public interface AlipayTradeInfoRepository extends JpaRepository<AlipayTradeInfo, String> {
//
//    @Query(nativeQuery = true, value = "SELECT * FROM alipay_trade_info alipayTradeInfo WHERE alipayTradeInfo.uuid =?1 AND alipayTradeInfo.amount >= 0")
//    List<AlipayTradeInfo> getIncomeData(String uuid);
//
//    @Query(nativeQuery = true, value = "SELECT * FROM alipay_trade_info alipayTradeInfo WHERE alipayTradeInfo.uuid =?1 AND alipayTradeInfo.amount < 0")
//    List<AlipayTradeInfo> getPayData(String uuid);
//}
