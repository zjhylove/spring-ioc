package com.zjhy.framework.ioc.core;

import com.zjhy.framework.ioc.bean.BeanDefinition;
import com.zjhy.framework.ioc.utils.BeanUtils;
import com.zjhy.framework.ioc.utils.ClassUtils;
import com.zjhy.framework.ioc.utils.ReflectionUtils;
import com.zjhy.framework.ioc.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean 工厂实现
 *
 * @author zj
 * @date 2018-4-21
 */
public class BeanFactoryImpl implements BeanFactory {


    private static final ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, BeanDefinition> beanDefineMap = new ConcurrentHashMap<>();

    private static final Set<String> beanNameSet = Collections.synchronizedSet(new HashSet<>());


    @Override
    public Object getBean(String beanName) throws Exception {

        if (StringUtils.isEmpty(beanName)) {
            return null;
        }

        //step 1：查找对象是否已被实例化
        Object bean = beanMap.get(beanName);
        if (bean != null) {
            return bean;
        }

        //step 2：实例化对象

        bean = createBean(beanDefineMap.get(beanName));
        if (bean != null) {

            //注入对象需要的参数
            populateBean(bean);
            //缓存对象下次利用
            beanMap.put(beanName, bean);
        }

        return bean;
    }


    /**
     * 注册bean
     *
     * @param definitions
     */
    protected void registerBean(List<BeanDefinition> definitions) {

        if (definitions == null || definitions.isEmpty()) {
            return;
        }

        for (BeanDefinition definition : definitions) {
            beanDefineMap.put(definition.getName(), definition);
            beanNameSet.add(definition.getName());
            registerBean(definition.getRefs());
        }
    }

    /**
     * 根据实体定义创建对象
     *
     * @param beanDefinition
     * @return
     */
    private Object createBean(BeanDefinition beanDefinition) throws Exception {

        if (beanDefinition == null) {
            throw new IllegalArgumentException("args must not be null!");
        }

        String beanName = beanDefinition.getClzName();

        Class beanClz = ClassUtils.loadClz(beanName);

        if (beanClz == null) {
            throw new ClassNotFoundException(beanName);
        }

        List<BeanDefinition> refs = beanDefinition.getRefs();
        if (refs != null && !refs.isEmpty()) {
            List<Object> objs = new ArrayList<>(refs.size());
            Class[] parameterTypes = new Class[refs.size()];
            for (int i = 0; i < refs.size(); i++) {
                Object bean = createBean(refs.get(i));
                objs.add(bean);
                parameterTypes[i] = bean.getClass().getSuperclass();
            }
            return BeanUtils.instanceBean(beanClz, beanClz.getConstructor(parameterTypes), objs.toArray());
        }
        return BeanUtils.instanceBean(beanClz, null, null);
    }


    private void populateBean(Object bean) throws Exception {

        if (bean == null) {
            throw new IllegalArgumentException("args must not be null!!");
        }

        Field[] fields = bean.getClass().getSuperclass().getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                String beanName = field.getName();
                if (beanNameSet.contains(beanName)) {
                    Object fieldBean = getBean(beanName);
                    if (fieldBean != null) {
                        ReflectionUtils.injectField(field, bean, fieldBean);
                    }
                }
            }
        }
    }
}
