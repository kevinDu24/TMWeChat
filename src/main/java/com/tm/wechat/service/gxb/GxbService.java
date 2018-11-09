//package com.tm.wechat.service.gxb;
//
//import com.alibaba.fastjson.JSONObject;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tm.wechat.config.VersionProperties;
//import com.tm.wechat.dao.ApplyInfoRepository;
//import com.tm.wechat.dao.gxb.*;
//import com.tm.wechat.domain.ApplyInfo;
//import com.tm.wechat.domain.gxb.*;
//import com.tm.wechat.dto.apply.ApplyDto;
//import com.tm.wechat.dto.approval.ApplyFromDto;
//import com.tm.wechat.dto.gxb.*;
//import com.tm.wechat.dto.message.Message;
//import com.tm.wechat.dto.message.MessageType;
//import com.tm.wechat.service.applyOnline.ApplyService;
//import com.tm.wechat.service.approval.ApprovalService;
//import com.tm.wechat.config.BaiduMapConfig;
//import com.tm.wechat.utils.Utils;
//import com.tm.wechat.utils.commons.PhoneAuthUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.UnsupportedEncodingException;
//import java.math.BigDecimal;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.*;
//
//
///**
// * Created by Leadu on 2017/7/18.
// */
//@Service
//public class GxbService {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private WechatContactGroupInfoRepository wechatContactGroupInfoRepository;
//
//    @Autowired
//    private WechatContactInfoRepository wechatContactInfoRepository;
//
//    @Autowired
//    private WechatInfoRepository wechatInfoRepository;
//
//    @Autowired
//    private TaobaoInfoRepository taobaoInfoRepository;
//
//    @Autowired
//    private TaobaoTradesRepository taobaoTradesRepository;
//
//    @Autowired
//    private TaobaoConsigneeAddressesRepository taobaoConsigneeAddressesRepository;
//
//    @Autowired
//    private AlipayBindedBankCardsRepository alipayBindedBankCardsRepository;
//
//    @Autowired
//    private AlipayPaymentAccountsRepository alipayPaymentAccountsRepository;
//
//    @Autowired
//    private AlipayInfoRepository alipayInfoRepository;
//
//    @Autowired
//    private AlipayTradeInfoRepository alipayTradeInfoRepository;
//
//    @Autowired
//    private ApplyInfoRepository applyInfoRepository;
//
//    @Autowired
//    private PhoneAuthUtil phoneAuthUtil;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private HttpServletResponse httpServletResponse;
//
//    @Autowired
//    private HttpServletRequest httpServletRequest;
//
//    @Autowired
//    private VersionProperties versionProperties;
//
//    @Autowired
//    private BaiduMapInterface baiduMapInterface;
//
//
//    @Autowired
//    private ApprovalService approvalService;
//
//    @Autowired
//    private ApplyService applyService;
//
//    /**
//     * 解析JSON数据
//     * @param sequenceNo id
//     */
//    public void processGxb02Data(String sequenceNo) {
//        Map<String, Object> map = jdbcTemplate.queryForMap(getSql(),new Object[]{sequenceNo});
//        String authJson = map.get("auth_json").toString();
//        String type = map.get("type").toString();
//        String unique_mark = map.get("unique_mark").toString();
//        if ("weixin".equals(type)) {
//            processWeixinData(authJson, unique_mark);
//        } else if("taobao".equals(type)) {
//            processTaobaoData(authJson, unique_mark);
//        } else if("zhifubao".equals(type)) {
//            processZhifubaoData(authJson, unique_mark);
//        }
//    }
//
//    /**
//     *  解析微信数据
//     * @param authJson authJson
//     */
//    private void processWeixinData(String authJson, String uuid) {
//        try {
//            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//            Gxb02WeixinDto gxb02WeixinDto = objectMapper.readValue(authJson, Gxb02WeixinDto.class);
//
//            // 登录微信信息表(wechatInfo)
//            WechatInfo wechatInfo = new WechatInfo();
//            // 登录微信联系人表(wechatContactInfo)
//            List<WechatContactInfo> wechatContactInfoList = new ArrayList<>();
//            // 微信联系群组表(wechatContactGroupInfo)
//            List<WechatContactGroupInfo> wechatContactGroupInfoList = new ArrayList<>();
//
//            if (gxb02WeixinDto.getWeChatKey() != null && gxb02WeixinDto.getWeChatKey().getBaseInfo() != null) {
//                BaseInfoDto baseInfo = gxb02WeixinDto.getWeChatKey().getBaseInfo();
//                // 登录微信信息表(wechatInfo)
//                wechatInfo.setUuid(uuid);
//                wechatInfo.setUin(baseInfo.getUin());
//                wechatInfo.setNickName(baseInfo.getNickName());
//                wechatInfo.setHeadImgPath(baseInfo.getHeadImgPath());
//                wechatInfo.setSex(baseInfo.getSex());
//                wechatInfo.setSignature(baseInfo.getSignature());
//            }
//            jdbcTemplate.batchUpdate(getDeleteWechatInfoSql(),new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setString(1, uuid);
//                }
//                @Override
//                public int getBatchSize() {
//                    return 1;
//                }
//            });
//            wechatInfoRepository.save(wechatInfo);
//
//            if (gxb02WeixinDto.getWeChatKey() != null &&
//                    (gxb02WeixinDto.getWeChatKey().getContactList() != null && gxb02WeixinDto.getWeChatKey().getContactList().size() > 0)) {
//                List<ContactListDto> contactList = gxb02WeixinDto.getWeChatKey().getContactList();
//                for (ContactListDto contactListDto : contactList) {
//                    // 是否群组，1是0不是
//                    if (contactListDto.getIsGroup() == 0) {
//                        WechatContactInfo wechatContactInfo = new WechatContactInfo();
//                        wechatContactInfo.setUuid(uuid);
//                        wechatContactInfo.setWechatId(wechatInfo.getId());
//                        wechatContactInfo.setContactNickName(contactListDto.getNickName());
//                        wechatContactInfo.setContactHeadImgPath(contactListDto.getHeadImgPath());
//                        wechatContactInfo.setContactRemarkName(contactListDto.getRemarkName());
//                        wechatContactInfo.setContactSex(contactListDto.getSex());
//                        wechatContactInfo.setSignature(contactListDto.getSignature());
//                        wechatContactInfo.setContactVerifyFlag(contactListDto.getVerifyFlag());
//                        wechatContactInfo.setContactStarFriend(contactListDto.getStarFriend());
//                        wechatContactInfo.setContactProvince(contactListDto.getProvince());
//                        wechatContactInfo.setContactCity(contactListDto.getCity());
//                        wechatContactInfo.setContactIntimacy(contactListDto.getIntimacy());
//                        wechatContactInfoList.add(wechatContactInfo);
//                    } else {
//                        WechatContactGroupInfo wechatContactGroupInfo = new WechatContactGroupInfo();
//                        wechatContactGroupInfo.setUuid(uuid);
//                        wechatContactGroupInfo.setWechatId(wechatInfo.getId());
//                        wechatContactGroupInfo.setGroupNickName(contactListDto.getNickName());
//                        wechatContactGroupInfo.setGroupHeadImgPath(contactListDto.getHeadImgPath());
//                        wechatContactGroupInfo.setGroupSex(contactListDto.getSex());
//                        wechatContactGroupInfo.setGroupStarFriend(contactListDto.getStarFriend());
//                        wechatContactGroupInfo.setIsGroup(contactListDto.getIsGroup());
//                        wechatContactGroupInfo.setIsOwner(contactListDto.getIsOwner());
//                        wechatContactGroupInfo.setMemberCount(contactListDto.getMemberCount());
//                        wechatContactGroupInfo.setIsCollection(contactListDto.getIsCollection());
//                        wechatContactGroupInfo.setContactIntimacy(contactListDto.getIntimacy());
//                        wechatContactGroupInfoList.add(wechatContactGroupInfo);
//                    }
//                }
//            }
//
//            jdbcTemplate.batchUpdate(getDeleteWechatContactInfoSql(),new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setString(1, uuid);
//                }
//                @Override
//                public int getBatchSize() {
//                    return wechatContactInfoList.size();
//                }
//            });
//            wechatContactInfoRepository.save(wechatContactInfoList);
//            jdbcTemplate.batchUpdate(getDeleteWechatContactGroupInfoSql(),new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setString(1, uuid);
//                }
//                @Override
//                public int getBatchSize() {
//                    return wechatContactGroupInfoList.size();
//                }
//            });
//            wechatContactGroupInfoRepository.save(wechatContactGroupInfoList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 删除微信信息表sql
//     * @return
//     */
//    private String getDeleteWechatInfoSql() {
//        StringBuffer deleteSql = new StringBuffer();
//        deleteSql.append("delete from wechat_info where uuid = ?");
//        return deleteSql.toString();
//    }
//
//    /**
//     * 删除微信联系人表sql
//     * @return
//     */
//    private String getDeleteWechatContactInfoSql() {
//        StringBuffer deleteSql = new StringBuffer();
//        deleteSql.append("delete from wechat_contact_info where uuid = ?");
//        return deleteSql.toString();
//    }
//
//    /**
//     * 删除微信联系群组表(sql
//     * @return
//     */
//    private String getDeleteWechatContactGroupInfoSql() {
//        StringBuffer deleteSql = new StringBuffer();
//        deleteSql.append("delete from wechat_contact_group_info where uuid = ?");
//        return deleteSql.toString();
//    }
//
//    /**
//     * 解析淘宝数据
//     * @param authJson
//     * @param uuid
//     */
//    private void processTaobaoData(String authJson, String uuid) {
//        try {
//            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//            Gxb02TaobaoDto gxb02TaobaoDto = objectMapper.readValue(authJson, Gxb02TaobaoDto.class);
//
//            //登录淘宝（支付宝）基本信息表
//            TaobaoInfo taobaoInfo = new TaobaoInfo();
//            //登录淘宝交易记录表
//            List<TaobaoTradeInfo> taobaoTradeInfoList = new ArrayList<>();
//            //登录淘宝收货地址表
//            List<TaobaoConsigneeAddresses> taobaoConsigneeAddressesList = new ArrayList<>();
//
//            if (gxb02TaobaoDto.getEcommerceBaseInfo() != null) {
//                EcommerceBaseInfoDto ecommerceBaseInfo = gxb02TaobaoDto.getEcommerceBaseInfo();
//                taobaoInfo.setUuid(uuid);
//                taobaoInfo.setName(ecommerceBaseInfo.getName());
//                taobaoInfo.setMobile(ecommerceBaseInfo.getMobile());
//                taobaoInfo.setIdentityCard(ecommerceBaseInfo.getIdentityCard());
//                taobaoInfo.setTaobaoAccount(ecommerceBaseInfo.getTaobaoAccount());
//                taobaoInfo.setAlipayAccount(ecommerceBaseInfo.getAlipayAccount());
//                taobaoInfo.setCreditLevelAsBuyer(ecommerceBaseInfo.getCreditLevelAsBuyer());
//                taobaoInfo.setIsVerified(ecommerceBaseInfo.isVerified());
//            }
//            List<TaobaoOrderDto> taobaoOrders = gxb02TaobaoDto.getTaobaoOrders();
//            taobaoInfo.setWholeCount(taobaoOrders == null ? 0: taobaoOrders.size());
//            Double wholeFee = 0d;
//            if(taobaoOrders != null){
//                for(TaobaoOrderDto dto : taobaoOrders){
//                    wholeFee = wholeFee + dto.getActualFee();
//                }
//            }
//            taobaoInfo.setWholeFee(wholeFee);
//            jdbcTemplate.batchUpdate(getDeleteTaobaoInfoSql(),new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setString(1, uuid);
//                }
//                @Override
//                public int getBatchSize() {
//                    return 1;
//                }
//            });
//            taobaoInfoRepository.save(taobaoInfo);
//
//            List<EcommerceConsigneeAddressesDto> ecommerceConsigneeAddresses = gxb02TaobaoDto.getEcommerceConsigneeAddresses();
//            for (EcommerceConsigneeAddressesDto ecommerceConsigneeAddressesDto : ecommerceConsigneeAddresses) {
//                TaobaoConsigneeAddresses taobaoConsigneeAddresses = new TaobaoConsigneeAddresses();
//                taobaoConsigneeAddresses.setUuid(uuid);
//                taobaoConsigneeAddresses.setTaobaoId(taobaoInfo.getId());
//                taobaoConsigneeAddresses.setAddress(ecommerceConsigneeAddressesDto.getAddress());
//                taobaoConsigneeAddresses.setReceiveName(ecommerceConsigneeAddressesDto.getReceiveName());
//                taobaoConsigneeAddresses.setTelNumber(ecommerceConsigneeAddressesDto.getTelNumber());
//                taobaoConsigneeAddresses.setRegion(ecommerceConsigneeAddressesDto.getRegion());
//                taobaoConsigneeAddressesList.add(taobaoConsigneeAddresses);
//            }
//
//            if (taobaoOrders != null && taobaoOrders.size() >0) {
//                for (TaobaoOrderDto dto : taobaoOrders) {
//                    List<TaobaoSubOrderDto> subOrders = dto.getSubOrders();
//                    if(subOrders != null && subOrders.size() >0){
//                        for(TaobaoSubOrderDto subDto : subOrders){
//                            TaobaoTradeInfo taobaoTradeInfo = new TaobaoTradeInfo();
//                            taobaoTradeInfo.setUuid(uuid);
//                            taobaoTradeInfo.setTaobaoId(taobaoInfo.getId());
//                            taobaoTradeInfo.setOrderNumber(dto.getOrderNumber());
//                            taobaoTradeInfo.setTradeStatusName(dto.getTradeStatusName());
//                            taobaoTradeInfo.setOrderCreateTime(dto.getCreateTime());
//                            taobaoTradeInfo.setEndTime(dto.getEndTime());
//                            taobaoTradeInfo.setPayTime(dto.getPayTime());
//                            taobaoTradeInfo.setActualFee(dto.getActualFee());
//                            taobaoTradeInfo.setItemTitle(subDto.getItemTitle());
//                            taobaoTradeInfo.setQuantity(subDto.getQuantity());
//                            taobaoTradeInfo.setOriginal(subDto.getOriginal());
//                            taobaoTradeInfo.setActual(subDto.getActual());
//                            taobaoTradeInfoList.add(taobaoTradeInfo);
//                        }
//                    }
//                }
//            }
//            jdbcTemplate.batchUpdate(getDeleteTaobaoTradesSql(),new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setString(1, uuid);
//                }
//                @Override
//                public int getBatchSize() {
//                    return taobaoTradeInfoList.size();
//                }
//            });
//            taobaoTradesRepository.save(taobaoTradeInfoList);
//
//            jdbcTemplate.batchUpdate(getDeleteTaobaoAddressSql(),new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setString(1, uuid);
//                }
//                @Override
//                public int getBatchSize() {
//                    return taobaoConsigneeAddressesList.size();
//                }
//            });
//            taobaoConsigneeAddressesRepository.save(taobaoConsigneeAddressesList);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 删除淘宝基本信息表sql
//     * @return
//     */
//    private String getDeleteTaobaoInfoSql() {
//        StringBuffer deleteSql = new StringBuffer();
//        deleteSql.append("delete from taobao_info where uuid = ?");
//        return deleteSql.toString();
//    }
//
//    /**
//     * 删除淘宝交易信息sql
//     * @return
//     */
//    private String getDeleteTaobaoTradesSql() {
//        StringBuffer deleteSql = new StringBuffer();
//        deleteSql.append("delete from taobao_trade_info where uuid = ?");
//        return deleteSql.toString();
//    }
//
//    /**
//     * 删除淘宝收货地址sql
//     * @return
//     */
//    private String getDeleteTaobaoAddressSql() {
//        StringBuffer deleteSql = new StringBuffer();
//        deleteSql.append("delete from taobao_consignee_addresses where uuid = ?");
//        return deleteSql.toString();
//    }
//
//    private String getSql() {
//        StringBuffer sql = new StringBuffer();
//        sql.append(" select ");
//        sql.append(" A.auth_json, B.type, B.unique_mark ");
//        sql.append(" from (select * from gxb02 T where T.sequence_no = ? order by T.create_time desc limit 1) as A ");
//        sql.append(" inner join gxb01 B on B.sequence_no =  A.sequence_no ");
//        return sql.toString();
//    }
//
//    /**
//     * 解析支付宝数据
//     * @param authJson
//     * @param uuid
//     */
//    private void processZhifubaoData(String authJson, String uuid) {
//        try {
//            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//            Gxb02TaobaoDto gxb02TaobaoDto = objectMapper.readValue(authJson, Gxb02TaobaoDto.class);
//
//            //登录支付宝基本信息表
//            AlipayInfo alipayInfo = new AlipayInfo();
//            //登录支付宝交易信息表
//            List<AlipayTradeInfo> alipayTradeInfoList = new ArrayList<>();
//            //登录支付宝银行卡信息表
//            List<AlipayBindedBankCards> alipayBindedBankCardsList = new ArrayList<>();
//            //登录支付宝缴费信息表
//            List<AlipayPaymentAccounts> alipayPaymentAccountsList = new ArrayList<>();
//
//            if (gxb02TaobaoDto.getEcommerceBaseInfo() != null) {
//                EcommerceBaseInfoDto ecommerceBaseInfo = gxb02TaobaoDto.getEcommerceBaseInfo();
//                alipayInfo.setUuid(uuid);
//                alipayInfo.setName(ecommerceBaseInfo.getName());
//                alipayInfo.setMobile(ecommerceBaseInfo.getMobile());
//                alipayInfo.setIdentityCard(ecommerceBaseInfo.getIdentityCard());
//                alipayInfo.setAlipayAccount(ecommerceBaseInfo.getAlipayAccount());
//                alipayInfo.setYuebaoBalance(ecommerceBaseInfo.getYuebaoBalance());
//                alipayInfo.setIsVerified(ecommerceBaseInfo.isVerified());
//                alipayInfo.setAlipayRegistrationDatetime(ecommerceBaseInfo.getAlipayRegistrationDatetime());
//            }
//
//            List<EcommerceTradesDto> ecommerceTrades = gxb02TaobaoDto.getEcommerceTrades();
//            if (ecommerceTrades != null && ecommerceTrades.size() >0) {
//                for (EcommerceTradesDto dto : ecommerceTrades) {
//                    AlipayTradeInfo alipayTradeInfo = new AlipayTradeInfo();
//                    alipayTradeInfo.setUuid(uuid);
//                    alipayTradeInfo.setAlipayInfoId(alipayInfo.getId());
//                    alipayTradeInfo.setTradeNo(dto.getTradeNo());
//                    alipayTradeInfo.setTitle(dto.getTitle());
//                    alipayTradeInfo.setAmount(dto.getAmount());
//                    alipayTradeInfo.setTradeTime(dto.getTradeTime());
//                    alipayTradeInfo.setTradeStatusName(dto.getTradeStatusName());
//                    alipayTradeInfo.setTxTypeName(dto.getTxTypeName());
//                    alipayTradeInfo.setOtherSide(dto.getOtherSide());
//                    alipayTradeInfoList.add(alipayTradeInfo);
//                }
//            }
//
//            List<EcommerceBindedBankCardsDto> ecommerceBindedBankCards = gxb02TaobaoDto.getEcommerceBindedBankCards();
//            if (ecommerceBindedBankCards != null && ecommerceBindedBankCards.size() >0) {
//                for (EcommerceBindedBankCardsDto dto : ecommerceBindedBankCards) {
//                    AlipayBindedBankCards alipayBindedBankCards = new AlipayBindedBankCards();
//                    alipayBindedBankCards.setUuid(uuid);
//                    alipayBindedBankCards.setAlipayInfoId(alipayInfo.getId());
//                    alipayBindedBankCards.setBankName(dto.getBankName());
//                    alipayBindedBankCards.setCardNo(dto.getCardNo());
//                    alipayBindedBankCards.setCardType(dto.getCardType());
//                    alipayBindedBankCards.setCardOwnerName(dto.getCardOwnerName());
//                    alipayBindedBankCards.setIsExpress(dto.isExpress());
//                    alipayBindedBankCardsList.add(alipayBindedBankCards);
//                }
//            }
//
//            List<EcommercePaymentAccountsDto> ecommercePaymentAccounts = gxb02TaobaoDto.getEcommercePaymentAccounts();
//            if (ecommercePaymentAccounts != null && ecommercePaymentAccounts.size() >0) {
//                for (EcommercePaymentAccountsDto dto : ecommercePaymentAccounts) {
//                    AlipayPaymentAccounts alipayPaymentAccounts = new AlipayPaymentAccounts();
//                    alipayPaymentAccounts.setUuid(uuid);
//                    alipayPaymentAccounts.setAlipayInfoId(alipayInfo.getId());
//                    alipayPaymentAccounts.setCategory(dto.getCategory());
//                    alipayPaymentAccounts.setCity(dto.getCity());
//                    alipayPaymentAccounts.setOrganization(dto.getOrganization());
//                    alipayPaymentAccounts.setAccountName(dto.getAccountName());
//                    alipayPaymentAccounts.setAccountCode(dto.getAccountCode());
//                    alipayPaymentAccountsList.add(alipayPaymentAccounts);
//                }
//            }
//            jdbcTemplate.batchUpdate(getDeleteAlipayInfoSql(),new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setString(1, uuid);
//                }
//                @Override
//                public int getBatchSize() {
//                    return 1;
//                }
//            });
//            alipayInfoRepository.save(alipayInfo);
//
//            jdbcTemplate.batchUpdate(getDeleteAlipayTradeInfoSql(),new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setString(1, uuid);
//                }
//                @Override
//                public int getBatchSize() {
//                    return alipayTradeInfoList.size();
//                }
//            });
//            alipayTradeInfoRepository.save(alipayTradeInfoList);
//
//            jdbcTemplate.batchUpdate(getDeleteAlipayBankCardSql(),new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setString(1, uuid);
//                }
//                @Override
//                public int getBatchSize() {
//                    return alipayBindedBankCardsList.size();
//                }
//            });
//            alipayBindedBankCardsRepository.save(alipayBindedBankCardsList);
//
//            jdbcTemplate.batchUpdate(getDeleteAlipayPymentSql(),new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setString(1, uuid);
//                }
//                @Override
//                public int getBatchSize() {
//                    return alipayPaymentAccountsList.size();
//                }
//            });
//            alipayPaymentAccountsRepository.save(alipayPaymentAccountsList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * 删除支付宝交易信息sql
//     * @return
//     */
//    private String getDeleteAlipayInfoSql() {
//        StringBuffer deleteSql = new StringBuffer();
//        deleteSql.append("delete from alipay_info where uuid = ?");
//        return deleteSql.toString();
//    }
//
//
//    /**
//     * 删除支付宝交易信息sql
//     * @return
//     */
//    private String getDeleteAlipayTradeInfoSql() {
//        StringBuffer deleteSql = new StringBuffer();
//        deleteSql.append("delete from alipay_trade_info where uuid = ?");
//        return deleteSql.toString();
//    }
//
//    /**
//     * 删除支付宝银行卡信息sql
//     * @return
//     */
//    private String getDeleteAlipayBankCardSql() {
//        StringBuffer deleteSql = new StringBuffer();
//        deleteSql.append("delete from alipay_binded_bank_cards where uuid = ?");
//        return deleteSql.toString();
//    }
//
//    /**
//     * 删除支付宝缴费信息sql
//     * @return
//     */
//    private String getDeleteAlipayPymentSql() {
//        StringBuffer deleteSql = new StringBuffer();
//        deleteSql.append("delete from alipay_payment_accounts where uuid = ?");
//        return deleteSql.toString();
//    }
//
//    /**
//     * 取得审批信审报告数据
//     * @param applyNum 申请人编号
//     * @param token 页面验证token
//     * @return
//     */
//    public ResponseEntity<Message> getReportData(String applyNum, String token) {
//        if(token == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR , "对不起，您无访问权限！"), HttpStatus.OK);
//        }
//        boolean auth = verifyToken(token);
//        if(!auth){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR , "对不起，您无访问权限！"), HttpStatus.OK);
//        }
//
//        GxbReportDto gxbReportDto = new GxbReportDto();
//
//        // 申请人信息
//        ApplyFromDto applyFromDto = approvalService.getApprovalInfo(applyNum);
//        String phoneNumberCity = "";
//        String vicePhoneNumberCity = "";
//        String mateMobileCity = "";
//        String contact1MobileCity = "";
//        String contact2MobileCity = "";
//        try {
//            if (applyFromDto.getOtherInfoDto().getPhoneNumber() != null) {
//                phoneNumberCity = phoneAuthUtil.getPnoneInfo(applyFromDto.getOtherInfoDto().getPhoneNumber());
//            }
//            if (applyFromDto.getOtherInfoDto().getVicePhoneNumber() != null) {
//                vicePhoneNumberCity = phoneAuthUtil.getPnoneInfo(applyFromDto.getOtherInfoDto().getVicePhoneNumber());
//            }
//            if (applyFromDto.getMateInfoDto() != null && applyFromDto.getMateInfoDto().getMateMobile() != null) {
//                mateMobileCity = phoneAuthUtil.getPnoneInfo(applyFromDto.getMateInfoDto().getMateMobile());
//            }
//            if (applyFromDto.getContactInfoDto().getContact1Mobile() != null) {
//                contact1MobileCity = phoneAuthUtil.getPnoneInfo(applyFromDto.getContactInfoDto().getContact1Mobile());
//            }
//            if (applyFromDto.getContactInfoDto().getContact2Mobile() != null) {
//                contact2MobileCity = phoneAuthUtil.getPnoneInfo(applyFromDto.getContactInfoDto().getContact2Mobile());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
//        }
//        String idCardAddress = applyFromDto.getIdCardInfoDto().getAddress();
//        String idCardCity = idCardAddress.contains("市") ?
//                idCardAddress.substring(0, idCardAddress.indexOf("市") + 1) : idCardAddress.substring(0, idCardAddress.indexOf("县") + 1);
//        String driveLicencedAddress = applyFromDto.getDriveLicenceInfoDto().getAddress();
//        String driveLicenceCity = driveLicencedAddress.contains("市") ?
//                driveLicencedAddress.substring(0, driveLicencedAddress.indexOf("市") + 1) : driveLicencedAddress.substring(0, driveLicencedAddress.indexOf("县") + 1);
//
//        ApplyDto applyDto = applyService.getApprovalInfo(applyNum);
//        BigDecimal priceRatio = new BigDecimal("0.00");
//        if(applyDto != null && applyDto.getCarInfoDto() != null){
//            String secondPrice = applyDto.getCarInfoDto().getSecondOfficialPrice();
//            String salePrices = applyDto.getCarInfoDto().getSalePrice();
//            // 车型为新车
//            if((secondPrice == null || secondPrice.isEmpty()) && (salePrices != null && !salePrices.isEmpty())){
//                BigDecimal officialPrice = new BigDecimal(applyDto.getCarInfoDto().getOfficialPrice());
//                BigDecimal salePrice = new BigDecimal(applyDto.getCarInfoDto().getSalePrice());
//                priceRatio = salePrice.divide(officialPrice,2,BigDecimal.ROUND_HALF_UP);
//            } else if((secondPrice != null && !secondPrice.isEmpty())){
//                BigDecimal officialPrice = new BigDecimal(applyDto.getCarInfoDto().getSecondOfficialPrice());
//                BigDecimal salePrice = new BigDecimal(applyDto.getCarInfoDto().getSalePrice());
//                priceRatio = salePrice.divide(officialPrice,2).setScale(2, BigDecimal.ROUND_HALF_UP);
//            }
//        }
//        ApplyInfo applyInfo = applyInfoRepository.findByApplyNum(applyNum);
//        String approvalUuid = applyInfo.getApprovalUuid();
//        // 微信数据
//        WechatInfo wechatInfo = wechatInfoRepository.findByUuid(approvalUuid);
//        List<WechatContactInfo> wechatContactInfoList = wechatContactInfoRepository.findByUuidOrderByContactIntimacyDesc(approvalUuid);
//        List<WechatContactGroupInfo> wechatContactGroupInfoList = wechatContactGroupInfoRepository.findByUuidOrderByContactIntimacyDesc(approvalUuid);
//        // 微信共享联系人
//        List<String> wechatShareContactList = getWechatShareContactList(approvalUuid);
//        // 微信共享群组
//        List<String> wechatShareContactGroupList = getWechatShareContactGroupList(approvalUuid);
//
//        // 淘宝数据
//        TaobaoInfo taobaoInfo = taobaoInfoRepository.findByUuid(approvalUuid);
//        List<TaobaoConsigneeAddresses> taobaoConsigneeAddressesList = taobaoConsigneeAddressesRepository.findByUuid(approvalUuid);
//        List<LonLatDto> lonLatList = new ArrayList(); //淘宝地址经纬度集合
//        if(taobaoConsigneeAddressesList != null){
//            for(TaobaoConsigneeAddresses item : taobaoConsigneeAddressesList){
//                String address = item.getAddress().trim();
//                LonLatDto dto = getLonLat(address);
//                lonLatList.add(dto);
//            }
//        }
//        gxbReportDto.setIsOnTargetIdAdr("0");
//        gxbReportDto.setIsOnTargetDriveAdr("0");
//        gxbReportDto.setIsOnTargetWorkAdr("0");
//        // 检测是否命中要报地址
//        checkMainAddress(idCardAddress, driveLicencedAddress, applyDto, lonLatList, gxbReportDto);
//        // 检查淘宝地址是否命中
//        checkTaobaoAddress(idCardAddress, driveLicencedAddress, applyDto,  taobaoConsigneeAddressesList, gxbReportDto);
//
//        // 支付宝
//        AlipayInfo alipayInfo = alipayInfoRepository.findByUuid(approvalUuid);
//        List<AlipayTradeInfo> alipayIncomeTradeList = alipayTradeInfoRepository.getIncomeData(approvalUuid);
//        List<AlipayTradeInfo> alipayPayTradeList = alipayTradeInfoRepository.getPayData(approvalUuid);
//        int alipayIncomeTradeNum = alipayIncomeTradeList.size();
//        int alipayPayTradeNum = alipayPayTradeList.size();
//        Double alipayIncomeTradeAmountNum = 0d;
//        Double alipayPayTradeAmountNum = 0d;
//        for (AlipayTradeInfo alipayTradeInfo : alipayIncomeTradeList) {
//            alipayIncomeTradeAmountNum = Double.sum(alipayIncomeTradeAmountNum, alipayTradeInfo.getAmount());
//        }
//        for (AlipayTradeInfo alipayTradeInfo : alipayPayTradeList) {
//            alipayPayTradeAmountNum = Double.sum(alipayPayTradeAmountNum, alipayTradeInfo.getAmount());
//        }
//        List<AlipayBindedBankCards> alipayBindedBankCardsList = alipayBindedBankCardsRepository.findByUuid(approvalUuid);
//        List<AlipayPaymentAccounts> alipayPaymentAccountsList = alipayPaymentAccountsRepository.findByUuid(approvalUuid);
//
//        gxbReportDto.setApprovalUuid(approvalUuid);
//        gxbReportDto.setApplyFromDto(applyFromDto);
//        gxbReportDto.setApplyDto(applyDto);
//        gxbReportDto.setApplyDate(applyInfo.getSubmitTime());
//        gxbReportDto.setWechatInfo(wechatInfo);
//        gxbReportDto.setPriceRatio(priceRatio.doubleValue());
//        gxbReportDto.setPhoneNumberCity(phoneNumberCity);
//        gxbReportDto.setVicePhoneNumberCity(vicePhoneNumberCity);
//        gxbReportDto.setMateMobileCity(mateMobileCity);
//        gxbReportDto.setContact1MobileCity(contact1MobileCity);
//        gxbReportDto.setContact2MobileCity(contact2MobileCity);
//        gxbReportDto.setWechatContactNum(wechatContactInfoList.size());
//        gxbReportDto.setWechatContactGroupNum(wechatContactGroupInfoList.size());
//        gxbReportDto.setIdCardCity(idCardCity);
//        gxbReportDto.setDriveLicenceCity(driveLicenceCity);
//        gxbReportDto.setWechatShareContactNum(wechatShareContactList.size());
//        gxbReportDto.setWechatShareContactGroupNum(wechatShareContactGroupList.size());
//        gxbReportDto.setTaobaoInfo(taobaoInfo);
//        gxbReportDto.setAlipayInfo(alipayInfo);
//        gxbReportDto.setAlipayIncomeTradeNum(alipayIncomeTradeNum);
//        gxbReportDto.setAlipayIncomeTradeAmountNum(String.format("%.2f",alipayIncomeTradeAmountNum));
//        gxbReportDto.setAlipayPayTradeNum(alipayPayTradeNum);
//        gxbReportDto.setAlipayPayTradeAmountNum(String.format("%.2f",alipayPayTradeAmountNum));
//        gxbReportDto.setAlipayBindedBankCardsList(alipayBindedBankCardsList);
//        gxbReportDto.setAlipayPaymentAccountsList(alipayPaymentAccountsList);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, gxbReportDto), HttpStatus.OK);
//    }
//
//    private void checkMainAddress(String idCardAddress, String driveLicencedAddress, ApplyDto applyDto, List<LonLatDto> lonLatList, GxbReportDto gxbReportDto) {
//        LonLatDto idDto = getLonLat(idCardAddress.trim());
//        LonLatDto driveDto = getLonLat(driveLicencedAddress.trim());
//        LonLatDto workDto = getLonLat(applyDto.getWorkInfoDto().getWorkUnitAddress().trim());
//        for(LonLatDto dto: lonLatList){
//            if(idDto != null){
//                if(!"1".equals(gxbReportDto.getIsOnTargetIdAdr())){
//                    double distence = Utils.getDistance(idDto.getLon(), idDto.getLat(), dto.getLon(), dto.getLat());
//                    if(distence - 500d <= 0){
//                        gxbReportDto.setIsOnTargetIdAdr("1");
//                    }
//                }
//                if(!"1".equals(gxbReportDto.getIsOnTargetDriveAdr())){
//                    double distence = Utils.getDistance(driveDto.getLon(), driveDto.getLat(), dto.getLon(), dto.getLat());
//                    if(distence - 500d <= 0){
//                        gxbReportDto.setIsOnTargetDriveAdr("1");
//                    }
//                }
//                if(!"1".equals(gxbReportDto.getIsOnTargetWorkAdr())){
//                    double distence = Utils.getDistance(workDto.getLon(), workDto.getLat(), dto.getLon(), dto.getLat());
//                    if(distence - 500d <= 0){
//                        gxbReportDto.setIsOnTargetWorkAdr("1");
//                    }
//                }
//            }
//        }
//    }
//
//    private void checkTaobaoAddress(String idCardAddress, String driveLicencedAddress, ApplyDto applyDto,
//                                    List<TaobaoConsigneeAddresses> taobaoConsigneeAddressesList, GxbReportDto gxbReportDto) {
//        LonLatDto idDto = getLonLat(idCardAddress.trim());
//        LonLatDto driveDto = getLonLat(driveLicencedAddress.trim());
//        LonLatDto workDto = getLonLat(applyDto.getWorkInfoDto().getWorkUnitAddress().trim());
//        List<TaobaoConsigneeAddressesDto> taobaoConsigneeAddressesDtoList = new ArrayList<>();
//        for (TaobaoConsigneeAddresses taobaoConsigneeAddresses : taobaoConsigneeAddressesList) {
//            TaobaoConsigneeAddressesDto taobaoConsigneeAddressesDto = new TaobaoConsigneeAddressesDto();
//            taobaoConsigneeAddressesDto.setAddress(taobaoConsigneeAddresses.getAddress());
//            taobaoConsigneeAddressesDto.setReceiveName(taobaoConsigneeAddresses.getReceiveName());
//            taobaoConsigneeAddressesDto.setRegion(taobaoConsigneeAddresses.getRegion());
//            taobaoConsigneeAddressesDto.setTelNumber(taobaoConsigneeAddresses.getTelNumber());
//            taobaoConsigneeAddressesDto.setFlag("0");
//            LonLatDto dto = getLonLat(taobaoConsigneeAddresses.getAddress().trim());
//            if(!"1".equals(gxbReportDto.getIsOnTargetWorkAdr())){
//                double distence = Utils.getDistance(idDto.getLon(), idDto.getLat(), dto.getLon(), dto.getLat());
//                if(distence - 500d <= 0){
//                    taobaoConsigneeAddressesDto.setFlag("1");
//                }
//            }
//            if(!"1".equals(gxbReportDto.getIsOnTargetWorkAdr())){
//                double distence = Utils.getDistance(driveDto.getLon(), driveDto.getLat(), dto.getLon(), dto.getLat());
//                if(distence - 500d <= 0){
//                    taobaoConsigneeAddressesDto.setFlag("1");
//                }
//            }
//            if(!"1".equals(gxbReportDto.getIsOnTargetWorkAdr())){
//                double distence = Utils.getDistance(workDto.getLon(), workDto.getLat(), dto.getLon(), dto.getLat());
//                if(distence - 500d <= 0){
//                    taobaoConsigneeAddressesDto.setFlag("1");
//                }
//            }
//            taobaoConsigneeAddressesDtoList.add(taobaoConsigneeAddressesDto);
//        }
//        gxbReportDto.setTaobaoConsigneeAddressesDtoList(taobaoConsigneeAddressesDtoList);
//    }
//
//    /**
//     * 调用百度api通过中文地址获取经纬度
//     * @param address
//     * @return
//     */
//    private LonLatDto getLonLat(String address) {
//        String resultJson = baiduMapInterface.getLonLatByAddress(cutString(address), "json", "bd09ll" , BaiduMapConfig.ak);
//        JSONObject jsonObject = JSONObject.parseObject(resultJson);
//        if (jsonObject.get("status").toString().equals("1")) {
//            return null;
//        }
//        LonLatDto lonLatDto = new LonLatDto();
//        JSONObject result = JSONObject.parseObject(jsonObject.get("result").toString());
//        JSONObject location = JSONObject.parseObject(result.get("location").toString());
//        Double add_lon = Double.parseDouble(location.get("lng").toString());
//        Double add_lat = Double.parseDouble(location.get("lat").toString());
//        lonLatDto.setLon(add_lon);
//        lonLatDto.setLat(add_lat);
//        return lonLatDto;
//    }
//
//    /**
//     * 取得微信共享联系人数目
//     * @param approvalUuid 申请人uuid
//     * @return
//     */
//    public List<String> getWechatShareContactList(String approvalUuid) {
//        List<String> wechatShareContactList =
//                jdbcTemplate.query(getWechatShareContactListSql(), new Object[]{approvalUuid, approvalUuid}, new RowMapper<String>() {
//
//                    @Override
//                    public String mapRow(ResultSet resultSet, int i) throws SQLException {
//                        return resultSet.getString("nickName");
//                    }
//                });
//        return wechatShareContactList;
//    }
//
//    private String getWechatShareContactListSql() {
//        StringBuffer sql = new StringBuffer();
//        sql.append(" select DISTINCT D.nick_name nickName ");
//        sql.append(" from (select A.contact_nick_name, A.contact_head_img_path, A.contact_sex, A.signature from wechat_contact_info AS A ");
//        sql.append(" WHERE A.uuid = ? AND A.contact_verify_flag = 0)AS C ");
//        sql.append(" INNER JOIN wechat_contact_info AS B ");
//        sql.append(" ON B.uuid != ? ");
//        sql.append(" AND B.contact_verify_flag = 0 ");
//        sql.append(" and C.contact_nick_name = B.contact_nick_name ");
//        sql.append(" and C.contact_sex = B.contact_sex ");
//        sql.append(" and C.signature = B.signature ");
//        sql.append(" INNER JOIN wechat_info AS D ");
//        sql.append(" ON B.uuid = D.uuid ");
//        return sql.toString();
//    }
//
//    /**
//     * 取得微信共享联系人的信息
//     * @param approvalUuid 申请人uuid
//     * @return
//     */
//    public Map<String, List<WechatShareDto>> getWechatShareContactMap(String approvalUuid) {
//        List<WechatShareDto> wechatShareContactList = new ArrayList<>();
//        jdbcTemplate.query(getWechatShareContactMapSql(), new Object[]{approvalUuid, approvalUuid}, new RowMapper<WechatShareDto>() {
//
//            @Override
//            public WechatShareDto mapRow(ResultSet resultSet, int i) throws SQLException {
//                WechatShareDto wechatShareDto = new WechatShareDto();
//                wechatShareDto.setUuid(resultSet.getString("uuid"));
//                wechatShareDto.setNickName(resultSet.getString("nickName"));
//                wechatShareDto.setSex(resultSet.getInt("sex"));
//                wechatShareDto.setSignature(resultSet.getString("signature"));
//                wechatShareDto.setProvince(resultSet.getString("province"));
//                wechatShareDto.setCity(resultSet.getString("city"));
//                wechatShareContactList.add(wechatShareDto);
//                return wechatShareDto;
//            }
//        });
//        Map<String, List<WechatShareDto>> map = new HashMap<>();
//        for (WechatShareDto wechatShareDto : wechatShareContactList) {
//            List<WechatShareDto> list = map.get(wechatShareDto.getUuid());
//            if (list == null || list.size() == 0) {
//                ApplyFromDto applyFromDto = approvalService.getApproval(wechatShareDto.getUuid());
//                String version = versionProperties.getVersion();
//                ApplyInfo ApplyInfo = applyInfoRepository.findByApprovalUuidAndVersion(wechatShareDto.getUuid(),version);
//                wechatShareDto.setApplyNum(ApplyInfo.getApplyNum());
//                wechatShareDto.setName(applyFromDto.getIdCardInfoDto().getName());
//                wechatShareDto.setTel(applyFromDto.getOtherInfoDto().getPhoneNumber());
//                list = new ArrayList<>();
//                list.add(wechatShareDto);
//                map.put(wechatShareDto.getUuid(), list);
//            } else {
//                list.add(wechatShareDto);
//                map.put(wechatShareDto.getUuid(), list);
//            }
//        }
//        return map;
//    }
//
//    private String getWechatShareContactMapSql() {
//        StringBuffer sql = new StringBuffer();
//        sql.append(" select D.uuid uuid, B.contact_nick_name nickName, B.contact_sex sex, B.signature signature, B.contact_province province, B.contact_city city ");
//        sql.append(" from (select A.contact_nick_name, A.contact_head_img_path, A.contact_sex, A.signature, A.contact_province, A.contact_city from wechat_contact_info AS A ");
//        sql.append(" WHERE A.uuid = ? AND A.contact_verify_flag = 0)AS C ");
//        sql.append(" INNER JOIN wechat_contact_info AS B ");
//        sql.append(" ON B.uuid != ? ");
//        sql.append(" AND B.contact_verify_flag = 0 ");
//        sql.append(" and C.contact_nick_name = B.contact_nick_name ");
//        sql.append(" and C.contact_sex = B.contact_sex ");
//        sql.append(" and C.signature = B.signature ");
//        sql.append(" and C.contact_province = B.contact_province ");
//        sql.append(" and C.contact_city = B.contact_city ");
//        sql.append(" INNER JOIN wechat_info AS D ");
//        sql.append(" ON B.uuid = D.uuid ");
//        sql.append(" ORDER BY D.uuid ");
//        return sql.toString();
//    }
//
//    /**
//     * 取得微信共享群组个数
//     * @param approvalUuid 申请人uuid
//     * @return
//     */
//    public List<String> getWechatShareContactGroupList(String approvalUuid) {
//        List<String> wechatContactGroupList =
//                jdbcTemplate.query(getWechatShareContactGroupListSql(), new Object[]{approvalUuid, approvalUuid}, new RowMapper<String>() {
//
//                    @Override
//                    public String mapRow(ResultSet resultSet, int i) throws SQLException {
//                        return resultSet.getString("uuid");
//                    }
//                });
//        return wechatContactGroupList;
//    }
//
//    private String getWechatShareContactGroupListSql() {
//        StringBuffer sql = new StringBuffer();
//        sql.append(" select DISTINCT D.uuid uuid ");
//        sql.append(" from (select A.group_nick_name, A.member_count from wechat_contact_group_info AS A ");
//        sql.append(" WHERE A.uuid = ? )AS C ");
//        sql.append(" INNER JOIN wechat_contact_group_info AS B ");
//        sql.append(" ON B.uuid != ? ");
//        sql.append(" and C.group_nick_name = B.group_nick_name ");
//        sql.append(" and C.member_count = B.member_count ");
//        sql.append(" INNER JOIN wechat_info AS D ");
//        sql.append(" ON B.uuid = D.uuid ");
//        return sql.toString();
//    }
//    /**
//     * 取得微信共享群组的信息
//     * @param approvalUuid 申请人uuid
//     * @return
//     */
//    public Map<String, List<WechatShareDto>> getWechatShareContactGroupMap(String approvalUuid) {
//            List<WechatShareDto> wechatShareContactList = new ArrayList<>();
//            jdbcTemplate.query(getWechatShareContactGroupMapSql(), new Object[]{approvalUuid, approvalUuid}, new RowMapper<WechatShareDto>() {
//
//                @Override
//                public WechatShareDto mapRow(ResultSet resultSet, int i) throws SQLException {
//                    WechatShareDto wechatShareDto = new WechatShareDto();
//                    wechatShareDto.setUuid(resultSet.getString("uuid"));
//                    wechatShareDto.setNickName(resultSet.getString("nickName"));
//                    wechatShareContactList.add(wechatShareDto);
//                    return wechatShareDto;
//                }
//            });
//            Map<String, List<WechatShareDto>> map = new HashMap<>();
//            for (WechatShareDto wechatShareDto : wechatShareContactList) {
//                List<WechatShareDto> list = map.get(wechatShareDto.getUuid());
//                if (list == null || list.size() == 0) {
//                    ApplyFromDto applyFromDto = approvalService.getApproval(wechatShareDto.getUuid());
//                    String version = versionProperties.getVersion();
//                    ApplyInfo ApplyInfo = applyInfoRepository.findByApprovalUuidAndVersion(wechatShareDto.getUuid(),version);
//                    wechatShareDto.setApplyNum(ApplyInfo.getApplyNum());
//                    wechatShareDto.setName(applyFromDto.getIdCardInfoDto().getName());
//                    wechatShareDto.setTel(applyFromDto.getOtherInfoDto().getPhoneNumber());
//                    list = new ArrayList<>();
//                    list.add(wechatShareDto);
//                    map.put(wechatShareDto.getUuid(), list);
//                } else {
//                    list.add(wechatShareDto);
//                    map.put(wechatShareDto.getUuid(), list);
//                }
//            }
//            return map;
//    }
//
//    private String getWechatShareContactGroupMapSql() {
//        StringBuffer sql = new StringBuffer();
//        sql.append(" select D.uuid uuid, B.group_nick_name nickName ");
//        sql.append(" from (select A.group_nick_name, A.member_count from wechat_contact_group_info AS A ");
//        sql.append(" WHERE A.uuid = ? )AS C ");
//        sql.append(" INNER JOIN wechat_contact_group_info AS B ");
//        sql.append(" ON B.uuid != ? ");
//        sql.append(" and C.group_nick_name = B.group_nick_name ");
//        sql.append(" and C.member_count = B.member_count ");
//        sql.append(" INNER JOIN wechat_info AS D ");
//        sql.append(" ON B.uuid = D.uuid ");
//        sql.append(" ORDER BY D.uuid ");
//        return sql.toString();
//    }
//
//    /**
//     * 取得微信联系人的具体信息
//     * @param approvalUuid 申请编号uuid
//     * @return
//     */
//    public ResponseEntity<Message> getWechatContactList(String approvalUuid) {
//        List<WechatContactInfo> wechatContactInfoList = wechatContactInfoRepository.findByUuidOrderByContactIntimacyDesc(approvalUuid);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, wechatContactInfoList), HttpStatus.OK);
//    }
//
//    /**
//     * 取得微信群组的具体信息
//     * @return
//     */
//    public ResponseEntity<Message> getWechatContactGroupList(String approvalUuid) {
//        List<WechatContactGroupInfo> wechatContactGroupInfoList = wechatContactGroupInfoRepository.findByUuidOrderByContactIntimacyDesc(approvalUuid);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, wechatContactGroupInfoList), HttpStatus.OK);
//    }
//
//    /**
//     * 取得淘宝交易的具体信息
//     * @return
//     */
//    public ResponseEntity<Message> getTaobaoTradeList(String approvalUuid) {
//        List<TaobaoTradeInfo> taobaoTradeInfoList = taobaoTradesRepository.findByUuidOrderByOrderCreateTimeDesc(approvalUuid);
//        Map<String,List<TaobaoTradeInfo>> map = new HashMap<>();
//        for (TaobaoTradeInfo taobaoTradeInfo : taobaoTradeInfoList) {
//            List<TaobaoTradeInfo> valueList = map.get(taobaoTradeInfo.getOrderNumber());
//            if (valueList != null && valueList.size() != 0) {
//                if(taobaoTradeInfo.getOrderCreateTime() != null && !taobaoTradeInfo.getOrderCreateTime().isEmpty()){
//                    taobaoTradeInfo.setOrderCreateTime(taobaoTradeInfo.getOrderCreateTime().substring(0,10));
//                }
//                if(taobaoTradeInfo.getPayTime() != null && !taobaoTradeInfo.getPayTime().isEmpty()){
//                    taobaoTradeInfo.setPayTime(taobaoTradeInfo.getPayTime().substring(0,10));
//                }
//                if(taobaoTradeInfo.getEndTime() != null && !taobaoTradeInfo.getEndTime().isEmpty()){
//                    taobaoTradeInfo.setEndTime(taobaoTradeInfo.getEndTime().substring(0,10));
//                }
//                valueList.add(taobaoTradeInfo);
//                map.put(taobaoTradeInfo.getOrderNumber(), valueList);
//            } else {
//                valueList = new ArrayList<>();
//                if(taobaoTradeInfo.getOrderCreateTime() != null && !taobaoTradeInfo.getOrderCreateTime().isEmpty()){
//                    taobaoTradeInfo.setOrderCreateTime(taobaoTradeInfo.getOrderCreateTime().substring(0,10));
//                }
//                if(taobaoTradeInfo.getPayTime() != null && !taobaoTradeInfo.getPayTime().isEmpty()){
//                    taobaoTradeInfo.setPayTime(taobaoTradeInfo.getPayTime().substring(0,10));
//                }
//                if(taobaoTradeInfo.getEndTime() != null && !taobaoTradeInfo.getEndTime().isEmpty()){
//                    taobaoTradeInfo.setEndTime(taobaoTradeInfo.getEndTime().substring(0,10));
//                }
//                valueList.add(taobaoTradeInfo);
//                map.put(taobaoTradeInfo.getOrderNumber(), valueList);
//            }
//        }
//        ValueComparator comparator = new ValueComparator();
//        List<Map.Entry<String,List<TaobaoTradeInfo>>> list=new ArrayList<>();
//        list.addAll(map.entrySet());
//        Collections.sort(list,comparator);
//        Map<String,List<TaobaoTradeInfo>> resultMap = new LinkedHashMap<>();
//        for (Map.Entry<String,List<TaobaoTradeInfo>> item : list){
//            resultMap.put(item.getKey(),item.getValue());
//        }
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultMap), HttpStatus.OK);
//    }
//
//    /**
//     * 取得支付宝交易的具体信息
//     * @return
//     */
//    public ResponseEntity<Message> getAlipayTradeList(String approvalUuid, String isIncomeOrPayFlag) {
//        List<AlipayTradeInfo> alipayTradeInfoList = new ArrayList<>();
//        if ("0".equals(isIncomeOrPayFlag)) {
//            alipayTradeInfoList = alipayTradeInfoRepository.getIncomeData(approvalUuid);
//        } else {
//            alipayTradeInfoList = alipayTradeInfoRepository.getPayData(approvalUuid);
//        }
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, alipayTradeInfoList), HttpStatus.OK);
//    }
//
//    /**
//     * 获取优质认证报告书token
//     */
//    public ResponseEntity<Message> getToken() {
//        // 清除cookie
//        Cookie[] cookies = httpServletRequest.getCookies();
//        if (null != cookies) {
//            for(Cookie cookie : cookies){
//                if("reportToken".equals(cookie.getName())){
//                    cookie.setValue(null);
//                    cookie.setMaxAge(0);// 立即销毁cookie
//                    httpServletResponse.addCookie(cookie);
//                    break;
//                }
//            }
//        }
//        // 新增cookie
//        String token = UUID.randomUUID().toString().replace("-", "");
//        Cookie tokenCookie = new Cookie("reportToken",token);
//        tokenCookie.setMaxAge(1 * 60);// 有效期为30分钟
//        httpServletResponse.addCookie(tokenCookie);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, token), HttpStatus.OK);
//    }
//
//    public boolean verifyToken(String tokenParam){
//        boolean flag = false;
//        Cookie[] cookies = httpServletRequest.getCookies();
//        if (null != cookies) {
//            for(Cookie cookie : cookies){
//                if("reportToken".equals(cookie.getName())){
//                    if(tokenParam.equals(cookie.getValue())){
//                        flag = true;
//                        cookie.setValue(null);
//                        cookie.setMaxAge(0);// 立即销毁cookie
//                        httpServletResponse.addCookie(cookie);
//                    }
//                    break;
//                }
//            }
//        }
//        return flag;
//    }
//
//    private static class ValueComparator implements Comparator<Map.Entry<String,List<TaobaoTradeInfo>>>
//    {
//        public int compare(Map.Entry<String,List<TaobaoTradeInfo>> m,Map.Entry<String,List<TaobaoTradeInfo>> n)
//        {
//            return n.getValue().get(0).getOrderCreateTime().compareTo(m.getValue().get(0).getOrderCreateTime());
//        }
//    }
//
//    /**
//     * 地址过长的场合，截取前100个字节
//     *
//     * @param str
//     * @return
//     */
//    private static String cutString(String str) {
//        try {
//            while (str.getBytes("utf-8").length > 100) {
//                str = str.substring(0, str.length() - 1);
//            }
//        } catch (UnsupportedEncodingException ex) {
//            ex.printStackTrace();
//        }
//        return str;
//    }
//
//
//
//}
