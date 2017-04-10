package com.happylifeplat.service.search.helper;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class SpringBeanUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SpringBeanUtils.class);
    private ConfigurableApplicationContext cfgContext;
    /**
     * 实体对象
     */
    private static final SpringBeanUtils INSTANCE = new SpringBeanUtils();

    private SpringBeanUtils() {
        if (INSTANCE != null) {
            throw new Error("error");
        }
    }

    public static SpringBeanUtils getInstance() {
        return INSTANCE;
    }

    /**
     * 防止序列化产生对象
     *
     * @return 防止序列化
     */
    private Object readResolve() {
        return INSTANCE;
    }

    /**
     * 获取一个Bean信息
     *
     * @param type 类型
     * @param <T>  泛型
     * @return 对象
     */
    public <T> T getBean(Class<T> type) {
        Assert.notNull(type);
        return cfgContext.getBean(type);
    }

    public  Object getBeanByName(String beanName){
       return cfgContext.getBean(beanName);
    }

    /**
     * 获取bean的名字
     *
     * @param type 类型
     * @return bean名字
     */
    public String getBeanName(Class type) {
        Assert.notNull(type);
        return cfgContext.getBeanNamesForType(type)[0];
    }

    /**
     * 判断一个bean是否存在Spring容器中.
     *
     * @param type 类型
     * @return 成功 true 失败 false
     */
    public boolean exitsBean(Class type) {
        Assert.notNull(type);
        return cfgContext.containsBean(type.getName());
    }

    /**
     * 动态注册一个Bean动Spring容器中
     *
     * @param beanName  名称
     * @param beanClazz 定义bean
     */
    public void registerBean(String beanName, Class beanClazz, Map<String, Object> propertyMap) {
        Assert.notNull(beanName);
        Assert.notNull(beanClazz);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
        if (propertyMap != null) {
            propertyMap.forEach(builder::addPropertyValue);
        }
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        registerBean(beanName, builder.getBeanDefinition());

    }

    public void registerBean(String beanName, Object obj) {
        Assert.notNull(beanName);
        Assert.notNull(obj);
        cfgContext.getBeanFactory().registerSingleton(beanName, obj);
    }

    /**
     * 注册Bean信息
     *
     * @param beanDefinition
     */
    public void registerBean(String beanName, BeanDefinition beanDefinition) {
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) cfgContext.getBeanFactory();
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * bean对象转换成map对象
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> transBean2Map(Object obj) {
        Assert.notNull(obj);
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            LOG.error("transBean2Map Error " + e);
        }
        return map;
    }

    /**
     * 根据枚举类型获取Spring注册的Bean
     *
     * @param annotationType 枚举
     * @return
     */
    public Map<String, Object> getBeanWithAnnotation(Class<? extends Annotation> annotationType) {
        Assert.notNull(annotationType);
        return cfgContext.getBeansWithAnnotation(annotationType);
    }

    /**
     * 动态注册一个Bean动Spring容器中
     *
     * @param beanName  名称
     * @param beanClazz 定义bean
     */
    public void registerBean(String beanName, Class beanClazz) {
        registerBean(beanName, beanClazz, null);
    }

    public void setCfgContext(ConfigurableApplicationContext cfgContext) {
        this.cfgContext = cfgContext;
    }
}
