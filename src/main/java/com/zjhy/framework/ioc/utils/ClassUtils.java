package com.zjhy.framework.ioc.utils;

/**
 * 类操作工具
 *
 * @author zj
 * @date 2018-4-21
 */
public class ClassUtils {


    /**
     * 获取默认类转载器
     *
     * @return
     */
    public static ClassLoader getDefaultClzLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class loadClz(String clzName) {

        try {
            return getDefaultClzLoader().loadClass(clzName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
