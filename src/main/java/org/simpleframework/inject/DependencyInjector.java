package org.simpleframework.inject;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.inject.annotation.Autowired;
import org.simpleframework.util.ClassUtil;

import java.lang.reflect.Field;
import java.util.Set;

@Slf4j
public class DependencyInjector {

	private final BeanContainer beanContainer;

	public DependencyInjector() {
		beanContainer = BeanContainer.getInstance();
	}

	public void doIoc() {
		Set<Class<?>> classes = beanContainer.getClasses();
		if (classes == null || classes.isEmpty()) {
			log.warn("没有对象");
			return;
		}
		for (Class<?> aClass : classes) {
			Field[] declaredFields = aClass.getDeclaredFields();
			if (declaredFields.length == 0) {
				continue;
			}
			for (Field declaredField : declaredFields) {
				if (declaredField.isAnnotationPresent(Autowired.class)) {
					Autowired autowired = declaredField.getAnnotation(Autowired.class);
					String value = autowired.value();
					Class<?> fieldClass = declaredField.getType();
					Object newFiledValue = getFieldInstance(fieldClass,value);
					if (newFiledValue == null) {
						throw new RuntimeException("找不到实例："+fieldClass.getName());
					}else {
						Object targetBean = beanContainer.getBean(aClass);
						ClassUtil.setField(declaredField,targetBean,newFiledValue,true);
					}
				}
			}
		}
	}

	private Object getFieldInstance(Class<?> fieldClass,String value) {
		Object bean = beanContainer.getBean(fieldClass);
		if (bean != null) {
			return bean;
		}else {
			Class<?> implementedClass = getImplementClass(fieldClass,value);
			if (implementedClass != null) {
				return beanContainer.getBean(implementedClass);
			}else {
				return null;
			}
		}
	}

	/**
	 * 获取接口的实现类
	 */
	private Class<?> getImplementClass(Class<?> fieldClass,String value) {
		Set<Class<?>> classSet = beanContainer.getClassesBySuper(fieldClass);
		if (classSet == null || classSet.isEmpty()) {
			return null;
		}
		if (value == null || value.equals("")) {
			if (classSet.size() == 1) {
				return classSet.iterator().next();
			}else {
				log.error("有多个实现类");
			}
		}else {
			for (Class<?> aClass : classSet) {
				if (aClass.getSimpleName().equals(value)) {
					return aClass;
				}
			}
		}
		return null;
	}
}
