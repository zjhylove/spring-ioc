package com.zjhy.framework.ioc.core;

/**
 * bean 工厂
 * @author zj
 * @date 2018-4-21
 */
public interface BeanFactory {

    /**
     * 通过名称获取对象
     * @param beanName
     * @return
     */
    Object getBean(String beanName) throws Exception;

}
