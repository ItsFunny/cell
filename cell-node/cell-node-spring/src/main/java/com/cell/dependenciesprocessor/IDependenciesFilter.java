package com.cell.dependenciesprocessor;

public interface IDependenciesFilter {
	boolean targetable(Class<?> target, String beanName);
	boolean dependable(Class<?> target, String beanName, Object dependOn);
}
