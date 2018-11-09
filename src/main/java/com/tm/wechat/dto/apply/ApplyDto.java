package com.tm.wechat.dto.apply;

import com.tm.wechat.dto.apply.file.ApplyFileDto;
import com.tm.wechat.dto.apply.file.GuaranteeFileDto;
import com.tm.wechat.dto.apply.file.JointFileDto;
import com.tm.wechat.dto.apply.file.MateFileDto;
import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;


/**
 * Created by pengchao on 2017/7/11.
 */
@Data
public class ApplyDto {

    @ItemColumn(keyPath = "#productPlanDto", itemDateType = ItemDataTypeEnum.ClassType,ClassType = ProductPlanDto.class)
    private ProductPlanDto productPlanDto;//产品方案

    @ItemColumn(keyPath = "#carInfoDto", itemDateType = ItemDataTypeEnum.ClassType,ClassType = CarInfoDto.class)
    private CarInfoDto carInfoDto;//车辆信息

    @ItemColumn(keyPath = "#carPledgeDto", itemDateType = ItemDataTypeEnum.ClassType,ClassType = CarPledgeDto.class)
    private CarPledgeDto carPledgeDto;//车辆抵押信息

    @ItemColumn(keyPath = "#financeInfoDto", itemDateType = ItemDataTypeEnum.ClassType,ClassType = FinanceInfoDto.class)
    private FinanceInfoDto financeInfoDto;//融资信息

    @ItemColumn(keyPath = "#detailedDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = DetailedDto.class )
    private DetailedDto detailedDto;//客户详细信息

    @ItemColumn(keyPath = "#workInfoDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = WorkInfoDto.class )
    private WorkInfoDto workInfoDto;//客户职业信息

    @ItemColumn(keyPath = "#jointInfoDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = JointInfoDto.class )
    private JointInfoDto jointInfoDto;//共申人信息

    @ItemColumn(keyPath = "#usedCarEvaluationDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = UsedCarEvaluationDto.class )
    private UsedCarEvaluationDto usedCarEvaluationDto;//二手车估价信息

    @ItemColumn(keyPath = "#usedCarEvaluationResultDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = UsedCarEvaluationResultDto.class )
    private UsedCarEvaluationResultDto usedCarEvaluationResultDto;//二手车估价信息

    @ItemColumn(keyPath = "#addressInfoDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = AddressInfoDto.class )
    private AddressInfoDto addressInfoDto;//客户地址信息

    @ItemColumn(keyPath = "#applyFileDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = ApplyFileDto.class )
    private ApplyFileDto applyFileDto;//客户附件信息

    @ItemColumn(keyPath = "#jointFileDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = JointFileDto.class )
    private JointFileDto jointFileDto;//共申人附件信息

    @ItemColumn(keyPath = "#mateFileDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = MateFileDto.class )
    private MateFileDto mateFileDto;//配偶附件信息

    @ItemColumn(keyPath = "#guaranteeFileDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = GuaranteeFileDto.class )
    private GuaranteeFileDto guaranteeFileDto;//担保人附件信息

}
