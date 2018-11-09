package com.tm.wechat.dto.usedcar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.domain.UsedCarAnalysisFile;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by pengchao on 2018/7/23.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarFileDto {

    @NotBlank(message = "行驶证照片不能为空")
    private String drivingLicense; // 行驶证

    @NotBlank(message = "登记证1,2页照片不能为空")
    private String registrationPage12; //登记证1,2页

    @NotBlank(message = "登记证3,4页照片不能为空")
    private String registrationPage34; // 登记证3,4页

    @NotBlank(message = "左前45度照片不能为空")
    private String leftFront45; // 左前45度

    @NotBlank(message = "左A柱与左B柱照片不能为空")
    private String leftAAndLeftB; //左A柱与左B柱

    @NotBlank(message = "左C柱照片不能为空")
    private String leftC; //左C柱

    @NotBlank(message = "右后45度照片不能为空")
    private String rightRear45; //右后45度

    @NotBlank(message = "右A柱与右B柱照片不能为空")
    private String rightAAndRightB; //右A柱与右B柱

    @NotBlank(message = "右C柱照片不能为空")
    private String rightC; //右C柱

    @NotBlank(message = "仪表盘照片不能为空")
    private String dashboard; //仪表盘

    @NotBlank(message = "安全带照片不能为空")
    private String seatBelt; //安全带

    @NotBlank(message = "中控台照片不能为空")
    private String centerConsole; //中控台

    @NotBlank(message = "后备箱照片不能为空")
    private String trunk; //后备箱

    @NotBlank(message = "后备箱底板照片不能为空")
    private String trunkFloor; //后备箱底板
    
    @NotBlank(message = "铭牌照片不能为空")
    private String nameplate; //铭牌

    @NotBlank(message = "发动机舱照片不能为空")
    private String engineCompartment; //发动机舱

    @NotBlank(message = "车架号照片不能为空")
    private String vin; //车架号

    private String remake; //照片备注

    public UsedCarFileDto() {
    }

    public UsedCarFileDto(UsedCarAnalysisFile usedCarFileDto) {
        this.drivingLicense = usedCarFileDto.getDrivingLicense();
        this.registrationPage12 = usedCarFileDto.getRegistrationPage12();
        this.registrationPage34 = usedCarFileDto.getRegistrationPage34();
        this.leftFront45 = usedCarFileDto.getLeftFront45();
        this.leftAAndLeftB = usedCarFileDto.getLeftAAndLeftB();
        this.leftC = usedCarFileDto.getLeftC();
        this.rightRear45 = usedCarFileDto.getRightRear45();
        this.rightAAndRightB = usedCarFileDto.getRightAAndRightB();
        this.rightC = usedCarFileDto.getRightC();
        this.dashboard = usedCarFileDto.getDashboard();
        this.seatBelt = usedCarFileDto.getSeatBelt();
        this.centerConsole = usedCarFileDto.getCenterConsole();
        this.trunk = usedCarFileDto.getTrunk();
        this.trunkFloor = usedCarFileDto.getTrunkFloor();
        this.nameplate = usedCarFileDto.getNameplate();
        this.engineCompartment = usedCarFileDto.getEngineCompartment();
        this.vin = usedCarFileDto.getVin();
        this.remake = usedCarFileDto.getRemake();
    }


}
