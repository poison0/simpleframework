package org.simpleframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.core.annotation.*;
import org.simpleframework.util.ClassUtil;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {

	private final Map<Class<?>,Object> beanMap = new ConcurrentHashMap<>();

	private static final List<Class<? extends Annotation>> BEAN_ANNOTATION = Arrays.asList(Component.class, Controller.class, Repository.class, Service.class);

	public static BeanContainer getInstance() {
		return ContainerHolder.HOLDER.instance;
	}

	private boolean loaded = false;

	public boolean isLoaded() {
		return loaded;
	}

	private enum ContainerHolder{
		HOLDER;
		private final BeanContainer instance;

		ContainerHolder() {
			instance = new BeanContainer();
		}
	}

	public int size() {
		return beanMap.size();
	}

	public synchronized void loadBeans(String packageName) {
		if (loaded) {
			log.warn("容器已经被加载过：{}",packageName);
			return;
		}
		Set<Class<?>> classSet = ClassUtil.extractPackageClass(packageName);
		if (classSet == null || classSet.isEmpty()) {
			log.warn("提取不到对象：{}",packageName);
			return;
		}
		for (Class<?> clazz : classSet) {
			for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
				if (clazz.isAnnotationPresent(annotation)) {
					beanMap.put(clazz, ClassUtil.newInstance(clazz, true));
				}
			}
		}
		loaded = true;

	}

	public Object addBean(Class<?> clazz, Object bean) {
		return beanMap.put(clazz, bean);
	}

	public Object removeBean(Class<?> clazz) {
		return beanMap.remove(clazz);
	}

	public Object getBean(Class<?> clazz){
		return beanMap.get(clazz);
	}

	public Set<Class<?>> getClasses() {
		return beanMap.keySet();
	}
	public Set<Object> getBean() {
		return new HashSet<>(beanMap.values());
	}

	public Set<Object> getClassesByAnnotation(Class<? extends Annotation> annotation) {
		Set<Class<?>> classSet = beanMap.keySet();
		Set<Object> beanSet = new HashSet<>();
		for (Class<?> aClass : classSet) {
			if (aClass.isAnnotationPresent(annotation)) {
				beanSet.add(beanMap.get(aClass));
			}
		}
		return beanSet;
	}

	//获取超类
	public Set<Class<?>> getClassesBySuper(Class<?> interfaceOrClass) {
		Set<Class<?>> classSet = beanMap.keySet();
		Set<Class<?>> beanSet = new HashSet<>();
		for (Class<?> aClass : classSet) {
			if (interfaceOrClass.isAssignableFrom(aClass) && !interfaceOrClass.equals(aClass)) {
				beanSet.add(aClass);
			}
		}
		return beanSet;
	}
}
