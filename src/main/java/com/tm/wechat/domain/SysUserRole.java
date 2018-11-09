package com.tm.wechat.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by LEO on 16/8/26.
 */
@Data
@Entity
@Table(name = "ZAM017")
public class SysUserRole {

    @Id
    private Long XTBCID;// 部门ID
    private String xtbmdm;// 级别：（QYJL：分公司；XSJL：团队账号，XSZY：销售专员）
    private String xtbmmc;// 用户名称
    private Long SJBMDM;//上级部门代码
    private String xtczdm;// 系统操作代码
    private Long xtjgid; //系统机构ID
    private String XTJGMC;//
    private String XTQYBZ;//
    private String XTCZRQ; //
    private String XTCZSJ; //
    private String XTCZRY; //
    private String xtyqhm;//邀请码
}
