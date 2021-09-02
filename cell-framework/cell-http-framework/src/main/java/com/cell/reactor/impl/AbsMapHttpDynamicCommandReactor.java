package com.cell.reactor.impl;

import com.cell.exceptions.ProgramaException;
import com.cell.reactor.IDynamicHttpReactor;
import com.cell.reactor.IMapDynamicHttpReactor;
import com.cell.utils.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-02 21:36
 */
public abstract class AbsMapHttpDynamicCommandReactor extends AbstractHttpCommandReactor implements IMapDynamicHttpReactor
{
    private Map<Class<?>, Object> dependecies = new HashMap<>(1);


    @Override
    public Object getDependency(Class<?> cls)
    {
        return this.dependecies.get(cls);
    }

    @Override
    public void registerDependency(Class<?> cls, Object o)
    {
        if (null != o && this.dependecies.containsKey(cls))
        {
            throw new ProgramaException("重复的依赖");
        }
        this.dependecies.put(cls, o);
    }
}
