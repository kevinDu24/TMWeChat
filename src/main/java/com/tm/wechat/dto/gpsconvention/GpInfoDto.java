package com.tm.wechat.dto.gpsconvention;

import lombok.Data;

import java.util.List;

/**
 * 查询GPS安装品牌、方式to
 * Created by LEO on 16/9/29.
 */
@Data
public class GpInfoDto {

    private List<String> installBrand; //安装品牌集合

    private List<String> installWay; //安装方式集合


}
