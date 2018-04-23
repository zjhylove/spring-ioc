package com.zjhy.framework.ioc.utils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * 实体类工具
 *
 * @author zj
 * @date 2018-4-21
 */
public class BeanUtils {


    /**
     * 实例化对象
     *
     * @param clz
     * @param ctr
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T instanceBean(Class<T> clz, Constructor ctr, Object... args) {

        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(clz);

        enhancer.setCallback(NoOp.INSTANCE);

        if (ctr == null) {
            return (T) enhancer.create();
        } else {
            return (T) enhancer.create(ctr.getParameterTypes(), args);
        }
    }
}
