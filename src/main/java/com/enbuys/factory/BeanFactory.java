package com.enbuys.factory;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pace
 * @version v1.0
 * @Type BeanFactory.java
 * @Desc
 * @date 2020/4/22 22:33
 */
public class BeanFactory {

    // 创建Map，用来保存类id与类对象
    private static Map<String,Object> map = new HashMap<>();

    // 根据id获取Bean
    public static Object getBean(String id){
        return map.get(id);
    }

    // 加载时初始化
    // 一、读取配置文件
    // 二、使用反射实例化
    static {
        // 获取配置文件流
        InputStream inputStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");

        // 使用dom4j解析配置文件
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(inputStream);
            // 获取根标签
            Element rootElement = document.getRootElement();
            // 获取bean标签
            List<Element> beanNodes = rootElement.selectNodes("//bean");

            // 循环bean，使用反射创建对象，保存到容器中
            for (Element element : beanNodes) {
                String id = element.attributeValue("id"); // 获取id 当key
                String clazz = element.attributeValue("class"); // 获取全限定类名，用作反射

                // 反射创建对象
                Class<?> aClass = Class.forName(clazz);
                Object o = aClass.newInstance();
                // 保存到容器中
                map.put(id,o);
            }

            // 优化实例化方式，获取带有property的标签，将其所属父标签的实例进行依赖注入
            List<Element> propertyNodes = rootElement.selectNodes("//property");
            for (Element element : propertyNodes) {
                // 获取标签中的name与ref
                String name = element.attributeValue("name");
                String ref = element.attributeValue("ref");
                String methodName = "set" + name; // set方法名称

                // 获取父标签
                Element parent = element.getParent();
                // 获取父标签的id，好从Map容器中拿出
                String parId = parent.attributeValue("id");
                Object parentObject = map.get(parId);
                // 获取该对象的所有方法
                Method[] methods = parentObject.getClass().getMethods();
                // 循环方法，找到set + name方法
                for (Method method : methods) {
                    if(method.getName().equalsIgnoreCase(methodName)){
                        // 依赖注入
                        Object propertyObject = map.get(ref); // 依赖的对象
                        method.invoke(parentObject,propertyObject);
                    }
                }

                // 设置完依赖后，将该对象重新放入容器
                map.put(parId,parentObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 关闭流
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
