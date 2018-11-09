package com.tm.wechat.dto.usedcar;

import com.tm.wechat.domain.UsedCarAnalysis;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by pengchao on 2018/7/18.
 */
@Data
public class UsedCarAnalysisDto {

    @NotBlank(message = "车架号不能为空")
    private String vin;//车架号

    @NotBlank(message = "车型id不能为空")
    private String modelId; //车型id

    @NotBlank(message = "车型名称不能为空")
    private String modelName;//车型名称

    @NotBlank(message = "城市id不能为空")
    private String zone; //城市id

    @NotBlank(message = "车辆上牌日期不能为空")
    private String regDate;//车辆上牌日期 2018-08

    @NotBlank(message = "颜色不能为空")
    private String color;//颜色

    @NotNull(message = "行驶公里数不能为空")
    private Double mile;//车辆行驶公里数

    private String interior;//车内装饰（优良中差）

    private String surface; //漆面状况（优良中差）

    private String workState; //工况状况（优良中差）

    private Integer transferTimes; //过户次数 可选

    private String makeDate;//车辆出厂日期 2018-08 可选

    @NotBlank(message = "业务人员姓名不能为空")
    private String operator; //操作业务人员姓名

    @NotBlank(message = "业务人员手机号不能为空")
    private String operatorPhoneNum; //操作业务人员手机号

    @NotBlank(message = "短信验证码不能为空")
    private String messageCode; //短信验证码


    private String uniqueMark; //评估单号


    private UsedCarFileDto usedCarFileDto;


    public UsedCarAnalysisDto() {
    }


    public UsedCarAnalysisDto(UsedCarAnalysis dto) {
        this.vin = dto.getVin();
        this.modelId = dto.getModelId();
        this.modelName = dto.getModelName();
        this.zone = dto.getZone();
        this.regDate = dto.getRegDate();
        this.color = dto.getColor();
        this.mile = dto.getMile();
        this.interior = dto.getInterior();
        this.surface = dto.getSurface();
        this.workState = dto.getWorkState();
        this.transferTimes = dto.getTransferTimes();
        this.makeDate = dto.getMakeDate();
        this.operator = dto.getOperator();
        this.operatorPhoneNum = dto.getOperatorPhoneNum();
        this.uniqueMark = dto.getUniqueMark();
    }
}
