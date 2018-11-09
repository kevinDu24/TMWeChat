package com.tm.wechat.dto.core.result;

import com.tm.wechat.domain.SysUser;
import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2017/8/8.
 */
@Data
public class Zam003FirstResult {

    private Long XTCZID;
    private String xtczdm;
    private String XTCZMC;
    private String XTCZMM;
    private Long XTJGID;
    private String XTJGMC;
    private String XTJSID;
    private String XTJSMC;
    private Long XTBMID;
    private String XTBMMC;
    private String XTYHDH;
    private String XTYHSJ;
    private String XTYHYX;
    private String XTANIP;
    private String XTQYBZ;
    private String XTYHBZ;
    private Long XTYHJB;
    private Long XTCZRQ;
    private Long XTCZSJ;
    private String XTCZRY;
    private String XTJSDM;
    private Long XTCDID;
    private String XTDRSJ;
    private String XTDCSJ;
    private String BAFWDM;
    private String BAFWMC;
    private String BALCSX;
    private Long XTMMYX;
    private String TXURL;
    private String customer;  //客户标识 : taimeng or yachi
    private String SFDLTMB; //是否可以登录太盟宝
    private List<SysUser> zam003SecondRe;
}
