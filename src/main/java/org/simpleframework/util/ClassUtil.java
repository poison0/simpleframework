package org.simpleframework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ClassUtil {

	public static final String FILE_PROTOCOL = "file";

	/**
	 * 获取包下类集合
	 * @param packageName 包名
	 * @return 类集合
	 */
	public static Set<Class<?>> extractPackageClass(String packageName) {
		ClassLoader classLoader = getClassLoader();
		URL resource = classLoader.getResource(packageName.replace(".", "/"));
		if (resource == null) {
			log.warn("获取不到任何东西：{}",packageName);
			return null;
		}
		Set<Class<?>> classSet = null;
		if (resource.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
			classSet = new HashSet<>();
			File packageDirectory = new File(resource.getPath());
			extractClassFile(classSet,packageDirectory,packageName);
		}
		return classSet;
	}

	private static void extractClassFile(Set<Class<?>> classSet,File fileSource, String packageName) {
		if (!fileSource.isDirectory()) {
			return;
		}
		File[] files = fileSource.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				} else {
					String absoluteFilePath = file.getAbsolutePath();
					if(absoluteFilePath.endsWith(".class")){
						addToClassSet(absoluteFilePath);
					}
				}
				return false;
			}

			private void addToClassSet(String absoluteFilePath) {
				absoluteFilePath = absoluteFilePath.replace(File.separator, ".");
				String className = absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));
				className = className.substring(0,className.lastIndexOf("."));
				Class<?> aClass = loadClass(className);
				classSet.add(aClass);
			}
		});
		if (files != null) {
			for (File file : files) {
				extractClassFile(classSet,file,packageName);
			}
		}
	}

	public static Class<?> loadClass(String className) {

		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			log.error("加载类错误错误，{}",e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	/**
	 * 获取classLoader
	 * @return classLoader
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz,boolean accessible) {
		try {
			Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
			declaredConstructor.setAccessible(accessible);
			return (T) declaredConstructor.newInstance();
		} catch (Exception e) {
			log.error("实例化class出错：{}",e.getMessage());
			throw new RuntimeException();
		}
	}

	public static void setField(Field field, Object target, Object value, boolean accessible) {
		field.setAccessible(accessible);
		try {
			field.set(target,value);
		} catch (IllegalAccessException e) {
			log.error("设置值异常");
		}
	}
}
