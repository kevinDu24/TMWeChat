package com.tm.wechat.dto.apply;

import lombok.Data;

/**
 * Created by pengchao on 2018/2/24.
 */
@Data
public class CarAndPledgeInfoDto {

    private CarInfoDto carInfoDto;//车辆信息

    private CarPledgeDto carPledgeDto; // 车辆抵押信息

    public CarAndPledgeInfoDto() {
    }

    public CarAndPledgeInfoDto(CarInfoDto carInfoDto, CarPledgeDto carPledgeDto) {
        this.carInfoDto = carInfoDto;
        this.carPledgeDto = carPledgeDto;
    }
}
