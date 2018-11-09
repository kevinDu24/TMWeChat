package com.tm.wechat.utils.commons;

import com.tm.wechat.domain.Approval;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qrhu on 2016/10/24.
 */
public class ItemDeserialize {

    /**
     * 分割符
     */
    private static final String SEP = "#";

    private static Map<String, Approval> listToMap(List<Approval> itemList) {
        Map<String, Approval> itemMap = new HashMap<>();
        for (Approval item : itemList) {
            if (StringUtils.isNotBlank(item.getItemKey())) {
                itemMap.put(item.getItemKey().trim(), item);
            }
        }
        return itemMap;
    }


    public static <T> T deserialize(Class<T> beanClass, List<Approval> itemList) throws Exception {
        if (CollectionUtils.isEmpty(itemList)) {
            return null;
        }
        if (beanClass == null) {
            return null;
        }

        Map<String, Approval> itemMap = listToMap(itemList);

        return deserialize(null, beanClass, itemMap);

    }

    private static <T> T deserialize(String parentKey, Class<T> beanClass, Map<String, Approval> itemMap) throws Exception {

        T instance = beanClass.newInstance();

        deserialize(parentKey, instance, itemMap);

        return instance;
    }

    private static <T> List<T> deserialize4List(String parentKey, Class<T> beanClass, ItemColumn itemColumn, Map<String, Approval> itemMap) throws Exception {

        if (!beanClass.getName().equalsIgnoreCase("java.unit.List")) {
//            return null;
        }

        StringBuffer stringBuffer = new StringBuffer();
        String key = stringBuffer.append(parentKey).append("#COUNT").toString();
        Approval item = itemMap.get(key);
        if (item == null) {
//            throw new RuntimeException("");
            return null;
        }
        Integer listSize = item.getItemInt32Value();
        List resultList = new ArrayList();

        if (itemColumn.isListInnerClass()) {
            deserialize4ListClass(parentKey, itemColumn.listInnerClassType(), itemMap, listSize, resultList);
        } else {
            deserialize4ListNormal(parentKey, itemColumn.listInnerNormalType().getType(), itemMap, listSize, resultList);
        }
        return resultList;
    }

    private static <T> void deserialize4ListClass(String parentKey, Class<T> beanClass, Map<String, Approval> itemMap, int listSize, List resultList) throws Exception {
        String pathKey;
        T instance;
        StringBuffer stringBuffer;
        for (int i = 0; i < listSize; i++) {
            stringBuffer = new StringBuffer();
            pathKey = stringBuffer.append(parentKey).append(SEP).append(i).toString();
            instance = beanClass.newInstance();
            deserialize(pathKey, instance, itemMap);
            resultList.add(instance);
        }
    }

    private static <T> void deserialize4ListNormal(String parentKey, int type, Map<String, Approval> itemMap, int listSize, List resultList) throws Exception {
        String pathKey;
        Object o;
        StringBuffer stringBuffer;
        for (int i = 0; i < listSize; i++) {
            stringBuffer = new StringBuffer();
            pathKey = stringBuffer.append(parentKey).append(SEP).append(i).toString();
            o = deSerialize4Normal(pathKey, type, itemMap);
            resultList.add(o);
        }
    }

    public static <T> void deserialize(String parentPathKey, T instance, Map<String, Approval> itemMap) throws Exception {
        Field[] fields = instance.getClass().getDeclaredFields();
        ItemColumn itemColumn;
        String pathKey;
        Object value;
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            itemColumn = field.getAnnotation(ItemColumn.class);
            if (itemColumn == null) {
                continue;
            }
            field.setAccessible(true);
            pathKey = itemColumn.keyPath();
            if (StringUtils.isNotBlank(parentPathKey)) {
                pathKey = parentPathKey.trim() + pathKey;
            }
            value = deSerialize(pathKey, field, itemColumn, itemMap);
            if(value==null){
                continue;
            }
            field.set(instance, value);
        }
    }


    private static Object deSerialize(String pathKey, Field field, ItemColumn itemColumn, Map<String, Approval> itemMap) throws Exception {
        switch (itemColumn.itemDateType().getType()) {
            case 5:
                return deserialize(pathKey, field.getType(), itemMap);
            case 10:
                return deserialize4List(pathKey, field.getType(), itemColumn, itemMap);
            default:
                return deSerialize4Normal(pathKey, itemColumn.itemDateType().getType(), itemMap);
        }
    }


    private static Object deSerialize4Normal(String pathKey, int type, Map<String, Approval> itemMap) throws Exception {
        Object result;
        Approval item = itemMap.get(pathKey.trim());
        if (item == null) {
            return null;
        }
        switch (type) {
            case 1:
                result = item.getItemStringValue();
                break;
            case 2:
                result = item.getItemInt32Value();
                break;
            case 3:
                result = item.getItemInt64Value();
                break;
            case 4:
                result = item.getItemDataTimeValue();
                break;
            default:
                result = item.getItemStringValue();
                break;
        }
        return result;
    }


}
