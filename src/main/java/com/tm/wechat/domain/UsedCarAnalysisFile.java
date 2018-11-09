package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.dto.usedcar.UsedCarFileDto;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by pengchao on 2018/7/23.
 * 车300二手车评估附件表（暂未使用）
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarAnalysisFile {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String analysisId;//评估id

    private String drivingLicense; // 行驶证

    private String registrationPage12; //登记证1,2页

    private String registrationPage34; // 登记证3,4页

    private String leftFront45; // 左前45度

    private String leftAAndLeftB; //左A柱与左B柱

    private String leftC; //左C柱

    private String rightRear45; //右后45度

    private String rightAAndRightB; //右A柱与右B柱

    private String rightC; //右C柱

    private String dashboard; //仪表盘

    private String seatBelt; //安全带

    private String centerConsole; //中控台

    private String trunk; //后备箱

    private String trunkFloor; //后备箱底板

    private String nameplate; //铭牌

    private String engineCompartment; //发动机舱

    private String vin; //车架号

    private String remake; //照片备注

    @CreatedDate
    private Date createTime;


    public UsedCarAnalysisFile() {
    }

    public UsedCarAnalysisFile(UsedCarFileDto usedCarFileDto) {
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
