package com.cell.plugin.pipeline.executor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-01 22:49
 */
public interface SuperClz<T>
{
    default Class<T> getGenericClass()
    {
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType)
        {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        }
        return (Class<T>) this.getClass();
    }

}
