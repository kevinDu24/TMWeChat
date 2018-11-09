package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by LEO on 16/12/3.
 */
@Entity
@Data
public class TmSysResource {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "tmSysResources" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TmSysRole> tmSysRoles;
}
