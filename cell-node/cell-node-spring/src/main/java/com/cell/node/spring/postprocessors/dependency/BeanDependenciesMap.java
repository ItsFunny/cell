package com.cell.node.spring.postprocessors.dependency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class BeanDependenciesMap
{
    private Map<Class<?>, Set<Class<?>>> dependenciesMap = new HashMap<>(256);

    public synchronized boolean addDependenOn(Class<?> target, Class<?> dependOn)
    {
        Set<Class<?>> dependOns = dependenciesMap.get(target);
        if (dependOns == null)
        {
            dependOns = new HashSet<>();
            dependenciesMap.put(target, dependOns);
        }
        return dependOns.add(dependOn);
    }

    public synchronized Set<Class<?>> getDependencies(Class<?> target)
    {
        return dependenciesMap.get(target);
    }
}
