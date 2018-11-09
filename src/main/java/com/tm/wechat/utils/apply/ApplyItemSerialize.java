package com.tm.wechat.utils.apply;

import com.tm.wechat.config.VersionProperties;
import com.tm.wechat.domain.Apply;
import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zcHu on 2017/05/09.
 */
public class ApplyItemSerialize {

    @Autowired
    private VersionProperties versionProperties;

    /**
     * 分割符
     */
    private static final String SEP = "#";


    public static List<Apply> serialize(Object obj, String uniqueMark, String version, String userName) throws Exception {
        List<Apply> resultList = new ArrayList<>();
        if (obj == null) {
            return resultList;
        }

        serialize(null, obj, resultList);
        for(Apply item : resultList){
            item.setUniqueMark(uniqueMark);
            item.setVersion(version);
            item.setCreateUser(userName);
        }
        return resultList;
    }

    private static void serialize(String parentKey,Object obj, List<Apply> resultList) throws Exception {
        Class clazz = obj==null?null:obj.getClass();
        serialize(parentKey,clazz,obj,resultList);

    }

    private static void serialize(String parentKey,Class objType, Object obj, List<Apply> resultList) throws Exception {
        if(objType == null){
            if(obj!=null){
                objType = obj.getClass();
            }
        }
        if(objType==null){
            return;
        }
        Field[] declaredFields = objType.getDeclaredFields();
        ItemColumn itemColumn;
        Object fieldValue;
        for (Field field : declaredFields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            itemColumn = field.getAnnotation(ItemColumn.class);
            if (itemColumn == null) {
                continue;
            }
            field.setAccessible(true);

            fieldValue = obj==null?null:field.get(obj);

            serialize(parentKey, itemColumn, fieldValue, resultList);
        }
    }


    private static void serialize4List(String parentPathKey, ItemColumn itemColumn, List<Object> valueList, List<Apply> resultList) throws Exception {
//        int size = valueList == null?0:valueList.size();
        if(valueList == null){
            return;
        }
        int size = valueList.size();
        Apply item = new Apply();
        String itemKey = "#COUNT";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(parentPathKey)) {
            itemKey = parentPathKey.trim() + itemKey;
        }
        item.setItemKey(itemKey);
        item.setItemType(ItemDataTypeEnum.IntegerType.getType());
        item.setItemInt32Value(size);
        resultList.add(item);

        if (itemColumn.isListInnerClass()) {
            serialize4ListClass(parentPathKey,itemColumn, valueList, resultList);
            return;
        }
        serialize4ListNormal(parentPathKey, itemColumn.listInnerNormalType().getType(), valueList, resultList);
    }


    private static void serialize4ListNormal(String parentPathKey, int type, List<Object> valueList, List<Apply> resultList) throws Exception {
        int size = valueList==null?0:valueList.size();
        String tmpParentKey;
        StringBuffer stringBuffer;
        for (int i = 0; i < size; i++) {
            stringBuffer = new StringBuffer();
            tmpParentKey = stringBuffer.append(parentPathKey).append(SEP).append(i).toString();
            serialize4Normal(tmpParentKey, type, valueList.get(i), resultList);
        }
    }

    private static void serialize4ListClass(String parentPathKey, ItemColumn itemColumn,List<Object> valueList, List<Apply> resultList) throws Exception {
        int size = valueList==null?0:valueList.size();

        Class<?> classType = itemColumn.listInnerClassType();
        String tmpParentKey;
        StringBuffer stringBuffer;
        for (int i = 0; i < size; i++) {
            stringBuffer = new StringBuffer();
            tmpParentKey = stringBuffer.append(parentPathKey).append(SEP).append(i).toString();
            serialize(tmpParentKey,classType,valueList.get(i), resultList);
        }
    }

    private static void serialize(String parentPathKey, ItemColumn itemColumn, Object fieldValue, List<Apply> resultList) throws Exception {

        Apply item = new Apply();
        String itemKey = itemColumn.keyPath();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(parentPathKey)) {
            itemKey = parentPathKey.trim() + itemKey;
        }
        item.setItemKey(itemKey);
        item.setItemType(itemColumn.itemDateType().getType());
        switch (item.getItemType()) {
            case 5:
                serialize(item.getItemKey(),itemColumn.ClassType(),fieldValue, resultList);
                return;
            case 10:
                serialize4List(item.getItemKey(), itemColumn, (List<Object>) fieldValue, resultList);
                return;
            default:
                serialize4Normal(itemKey, item.getItemType(), fieldValue, resultList);
                break;
        }
    }

    private static void serialize4Normal(String pathKey, int type, Object fieldValue, List<Apply> resultList) throws Exception {
        if (fieldValue == null) {
            return;
        }
        Apply item = new Apply();
        item.setItemKey(pathKey);
        item.setItemType(type);
        switch (item.getItemType()) {
            case 1:
                item.setItemStringValue(fieldValue==null?null:String.valueOf(fieldValue));
                break;
            case 2:
                item.setItemInt32Value((Integer) fieldValue);
                break;
            case 3:
                item.setItemInt64Value((Long) fieldValue);
                break;
            case 4:
                item.setItemDataTimeValue((Date) fieldValue);
                break;
            default:
                item.setItemStringValue(fieldValue==null?null:String.valueOf(fieldValue));
                break;
        }
        resultList.add(item);
    }


}
