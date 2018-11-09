
package com.tm.wechat.utils.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Created by zcHu on 2017/05/09.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemColumn {

    /**
     * keyPath 路径
     *    (其中路径中 '#' 为分隔符 )
     * @return
     */
    String keyPath() default "";

    /**
     * 数据类型
     * @return
     */
    ItemDataTypeEnum itemDateType() default ItemDataTypeEnum.StringType;

    /**
     *
     * List<基础数据类型>   false
     * List<对象数据类型>   true
     *
     * @return
     */
    boolean isListInnerClass() default true;

    /**
     * List<基础数据类型>
     *     目前仅支持   String
     *                  Integer
     *                  Long
     *                  Date
     * @return
     */
    ItemDataTypeEnum listInnerNormalType() default  ItemDataTypeEnum.StringType;

    /**
     *   List<对象数据类型>  classType
     * @return
     */
    Class<?> listInnerClassType() default Object.class;

    /**
     *   ClassType
     * @return
     */
    Class<?> ClassType() default Object.class;

}
