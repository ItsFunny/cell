package com.cell.node.core.collector;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-24 05:33
 */
public interface IActivePluginCollector
{
    <T> List<T> getPlugins(Class<T> superclz);

    <A extends Annotation, T> List<T> getPlugins(Class<T> superclz, Class<A> ano);

    <A extends Annotation> List<Object> getPluginsByAnno(Class<A> ano);
}
