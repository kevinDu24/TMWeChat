package com.tm.wechat.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by LEO on 16/12/3.
 */
@Entity
@Data
public class TmSysRole {
    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    private String customer;  //客户标识 : taimeng or yachi

    @ManyToMany
    @JoinTable(name = "tm_sys_role_tm_sys_resource",joinColumns= { @JoinColumn(name="role_id")},inverseJoinColumns= {@JoinColumn(name="resource_id")})
    private List<TmSysResource> tmSysResources;
}
