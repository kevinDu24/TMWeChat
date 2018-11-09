package com.tm.wechat.dto.gpsconvention;

import com.tm.wechat.dto.result.Res;
import lombok.Data;

import java.util.List;

/**
 * 分单列表dto
 * Created by LEO on 16/9/29.
 */
@Data
public class GpsAreaDto {

    private Res result; //状态相应结果集

    private List<GpsAreaResult> gpssfLys; //地区集合


}
