package com.zjhy.framework.ioc.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 核心数据结构
 *
 * @author zj
 * @date 2018/4/21
 */
public class BeanDefinition {


    private String name;

    private String clzName;

    List<BeanDefinition> refs = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClzName() {
        return clzName;
    }

    public void setClzName(String clzName) {
        this.clzName = clzName;
    }

    public List<BeanDefinition> getRefs() {
        return refs;
    }

    public void addRef(BeanDefinition ref) {
        this.refs.add(ref);
    }
}
