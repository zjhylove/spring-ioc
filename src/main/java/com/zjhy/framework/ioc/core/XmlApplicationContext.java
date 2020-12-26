package com.zjhy.framework.ioc.core;

import com.zjhy.framework.ioc.bean.BeanDefinition;
import com.zjhy.framework.ioc.utils.XmlUtils;

import java.io.File;
import java.util.List;

/**
 * xml应用上下文
 *
 * @author zj
 * @date 2018-4-21
 */
public class XmlApplicationContext extends BeanFactoryImpl {


    private File xmlFile;

    public File getXmlFile() {
        return xmlFile;
    }

    public XmlApplicationContext(File xmlFile) {

        try {
            this.xmlFile = xmlFile;
            List<BeanDefinition> beanDefinitions = XmlUtils.parseXml(xmlFile);
            registerBean(beanDefinitions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
