package com.zjhy.framework.ioc.utils;

import java.lang.reflect.Field;

/**
 * 反射工具类
 *
 * @author zj
 * @date 2018-4-21
 */
public class ReflectionUtils {

    /**
     * 为对象注入值
     *
     * @param field
     * @param obj
     * @param value
     * @throws IllegalAccessException
     */
    public static void injectField(Field field, Object obj, Object value) throws IllegalAccessException {
        if (field != null) {
            field.setAccessible(true);
            field.set(obj, value);
        }
    }
}
