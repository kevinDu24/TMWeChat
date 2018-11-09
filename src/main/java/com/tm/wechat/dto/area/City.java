package com.tm.wechat.dto.area;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by LEO on 16/10/27.
 */
@Data
public class City implements Serializable{

    private static final long serialVersionUID = -3091126703268302847L;
    private Long CityID;
    private String name;
    private Long ProID;
    private Long CitySort;
}
