package com.tm.wechat.dto.area;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by LEO on 16/10/27.
 */
@Data
public class Province implements Serializable{

    private static final long serialVersionUID = -6871286449948943566L;
    private Long ProID;
    private String name;
    private Long ProSort;
    private String ProRemark;
}
