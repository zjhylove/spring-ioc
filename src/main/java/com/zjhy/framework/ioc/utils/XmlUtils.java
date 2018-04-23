package com.zjhy.framework.ioc.utils;

import com.zjhy.framework.ioc.bean.BeanDefinition;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 解析xml 文件工具
 *
 * @author zj
 * @date 2018-4-21
 */
public class XmlUtils {


    private static final String XML_ROOT = "beans";

    private static final String XML_BEAN = "bean";

    private static final String XML_BEAN_ID = "id";

    private static final String XML_BEAN_CLASS = "class";

    private static final String XML_PROPERTY = "property";

    private static final String XML_PROPERTY_NAME = "name";

    private static final String XML_PROPERTY_VALUE = "value";


    /**
     * 解析xml 文件
     * @param xmlFile
     * @return
     * @throws Exception
     */
    public static List<BeanDefinition> parseXml(File xmlFile) throws Exception {

        SAXReader reader = new SAXReader();
        Document document = reader.read(xmlFile);

        Element rootElement = document.getRootElement();
        if (!Objects.equals(rootElement.getName(), XML_ROOT)) {
            throw new ParserConfigurationException();
        }

        Iterator<Element> beans = rootElement.elementIterator();
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        while (beans.hasNext()) {
            Element bean = beans.next();
            BeanDefinition definition = parseBeanTag(bean);
            Iterator<Element> properties = bean.elementIterator();
            while (properties.hasNext()) {
                definition.addRef(parsePropertyTg(properties.next()));
            }
            beanDefinitions.add(definition);
        }
        return beanDefinitions;
    }

    /**
     * 解析property标签
     * @param property
     * @return
     */
    private static BeanDefinition parsePropertyTg(Element property) {
        List<Attribute> propertyAttrs = property.attributes();
        BeanDefinition refDefinition = new BeanDefinition();
        for (Attribute attr : propertyAttrs) {
            switch (attr.getName()) {
                case XML_PROPERTY_NAME:
                    refDefinition.setName(attr.getValue());
                    break;
                case XML_PROPERTY_VALUE:
                    refDefinition.setClzName(attr.getValue());
                    break;
            }
        }
        return refDefinition;
    }

    /**
     * 解析bean标签
     * @param bean
     * @return
     */
    private static BeanDefinition parseBeanTag(Element bean) {
        List<Attribute> beanAttrs = bean.attributes();
        BeanDefinition definition = new BeanDefinition();
        for (Attribute attr : beanAttrs) {
            switch (attr.getName()) {
                case XML_BEAN_ID:
                    definition.setName(attr.getValue());
                    break;
                case XML_BEAN_CLASS:
                    definition.setClzName(attr.getValue());
                    break;
            }

        }
        return definition;
    }

}
