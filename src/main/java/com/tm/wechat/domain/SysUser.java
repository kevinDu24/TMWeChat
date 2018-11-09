package com.tm.wechat.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by LEO on 16/8/26.
 */
@Data
@Entity
@Table(name = "ZAM003")
public class SysUser implements Serializable {

    @Id
    private Long XTCZID;

    private String xtczdm;// 用户代码

    private String XTCZMC;// 用户名称

    private String XTCZMM;// 用户密码

    private Long XTJGID;// 系统机构代码（操作代码）

    private String XTJGMC;

    private String XTJSID;// 角色ID组

    private String XTJSMC;// 角色名称

    private Long XTBMID; // 经销商部门ID

    private String XTBMMC; // 经销商部门（QYJL：分公司；XSJL：团队账号, XSZY: 销售专员）

    private String XTYHDH; // 用户电话

    private String XTYHSJ;// 用户手机

    private String XTYHYX;// 用户邮箱

    private String XTANIP; //是否启用安全IP登录

    private String XTQYBZ;// 启用标致

    private String XTYHBZ;// 用户系统标识

    private Long XTYHJB;// 用户管理级别

    private Long XTCZRQ;// 操作日期

    private Long XTCZSJ;// 操作时间

    private String XTCZRY;// 操作人员

    private String XTJSDM;

    private Long XTCDID;// 用户一级菜单

    private String XTDRSJ;

    private String XTDCSJ;

    private String BAFWDM;

    private String BAFWMC;

    private String BALCSX;// 流程属性

    private Long XTMMYX;

    private String TXURL;
    private String customer;  //客户标识 : taimeng or yachi

    private String SFDLTMB; //是否可以登录太盟宝
}
