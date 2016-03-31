package com.hw.hardware.common.spring;

import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.hw.hardware.common.Constants;

/**
 * 自定义spring属性文件加载器
 * @author cfish
 * @since 2013-09-09
 */
public class CustomizedPropertyConfigurer extends PropertyPlaceholderConfigurer {

	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
		super.processProperties(beanFactory, props);
		for(Entry<Object, Object> entry : props.entrySet()) {
			Constants.SPRING_PROPERTIES.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}
	}
}
