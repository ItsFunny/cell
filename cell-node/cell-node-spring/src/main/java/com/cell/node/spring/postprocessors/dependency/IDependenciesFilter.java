package com.cell.node.spring.postprocessors.dependency;

public interface IDependenciesFilter
{
    boolean targetable(Class<?> target, String beanName);

    boolean dependable(Class<?> target, String beanName, Object dependOn);
}
