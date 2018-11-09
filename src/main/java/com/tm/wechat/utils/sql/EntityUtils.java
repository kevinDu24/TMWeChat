package com.tm.wechat.utils.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: TMWeChat
 * @description: Spring JPA 查询结果转 实体类
 * 注意实体类中需要增加  入参为 Object[] 的构造函数 ，例子参见 ApprovalCountDto
 * @author: ChengQC
 * @create: 2018-10-11 18:39
 **/
public class EntityUtils {
    private static Logger logger = LoggerFactory.getLogger(EntityUtils.class);


    //转换实体类list
    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz) throws Exception {
        List<T> returnList = new ArrayList<T>();
        Object[] co = list.get(0);
        Class[] c2 = new Class[co.length];
        //确定构造方法
        for (int i = 0; i < co.length; i++) {
            c2[i] = co[i].getClass();
        }
        for (Object[] o : list) {
            Constructor<T> constructor = clazz.getConstructor(c2);
            returnList.add(constructor.newInstance(o));
        }
        return returnList;
    }


    //转换实体类
    public static <T> T castEntity(Object[] co, Class<T> clazz) throws Exception {
        Class[] c2 = new Class[co.length];
        //确定构造方法
        for (int i = 0; i < co.length; i++) {
            c2[i] = co[i].getClass();
        }
        // 通过反射生成对象 -- By ChengQiChuan 2018/10/12 12:11
        Constructor<T> constructor = clazz.getConstructor(c2);
        return constructor.newInstance(co);
    }




}
