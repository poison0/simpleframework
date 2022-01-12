package org.simpleframework.core;

import org.junit.jupiter.api.Test;
import org.simpleframework.inject.DependencyInjector;

import static org.junit.jupiter.api.Assertions.*;

class BeanContainerTest {

	@Test
	void loadBeans() {
		BeanContainer instance = BeanContainer.getInstance();
		instance.loadBeans("com.imooc");
		DependencyInjector dependencyInjector = new DependencyInjector();
		dependencyInjector.doIoc();
		int size = instance.size();
		System.out.println(size);
	}
}
