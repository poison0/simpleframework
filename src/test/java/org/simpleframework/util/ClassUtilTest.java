package org.simpleframework.util;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ClassUtilTest {

	@DisplayName("提取目标类方法")
	@Test
	public void extractPackageClass() {
		Set<Class<?>> classSet = ClassUtil.extractPackageClass("com.imooc.controller");
		System.out.println(classSet);
	}

}
