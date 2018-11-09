package com.tm.wechat.dto.apply;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/7/11.
 */
@Data
public class WorkInfoDto {

    @ItemColumn(keyPath = "#workUnit", itemDateType = ItemDataTypeEnum.StringType)
    private String workUnit;//工作单位名称

    @ItemColumn(keyPath = "#workUnitPost", itemDateType = ItemDataTypeEnum.StringType)
    private String workUnitPost;//职务

    @ItemColumn(keyPath = "#workUnitNature", itemDateType = ItemDataTypeEnum.StringType)
    private String workUnitNature;//企业性质

    @ItemColumn(keyPath = "#workingLife", itemDateType = ItemDataTypeEnum.StringType)
    private String workingLife;//在职年限

    @ItemColumn(keyPath = "#workUnitTitle", itemDateType = ItemDataTypeEnum.StringType)
    private String workUnitTitle;//职称

    @ItemColumn(keyPath = "#workUnitIndustry", itemDateType = ItemDataTypeEnum.StringType)
    private String workUnitIndustry;//所属行业

    @ItemColumn(keyPath = "#workUnitProv", itemDateType = ItemDataTypeEnum.StringType)
    private String workUnitProv;//单位所在省份

    @ItemColumn(keyPath = "#workUnitCity", itemDateType = ItemDataTypeEnum.StringType)
    private String workUnitCity;//单位所在城市

    @ItemColumn(keyPath = "#workUnitPhone", itemDateType = ItemDataTypeEnum.StringType)
    private String workUnitPhone;//单位电话

    @ItemColumn(keyPath = "#annualPayAfterTax", itemDateType = ItemDataTypeEnum.StringType)
    private String annualPayAfterTax;//税后年薪

    @ItemColumn(keyPath = "#workUnitAddressProv", itemDateType = ItemDataTypeEnum.StringType)
    private String workUnitAddressProv;//工作单位所在省份

    @ItemColumn(keyPath = "#workUnitAddressCity", itemDateType = ItemDataTypeEnum.StringType)
    private String workUnitAddressProvCity;//工作单位所在城市

    @ItemColumn(keyPath = "#workUnitAddress", itemDateType = ItemDataTypeEnum.StringType)
    private String workUnitAddress;//工作单位详细地址

}
