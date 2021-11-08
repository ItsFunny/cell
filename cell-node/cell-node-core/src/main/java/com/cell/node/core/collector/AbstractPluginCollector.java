package com.cell.node.core.collector;

import com.cell.base.common.models.Module;
import com.cell.base.core.comparators.OrderComparator;
import com.cell.base.core.config.AbstractInitOnce;
import com.cell.base.core.log.LOG;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-25 22:22
 */
@Data
public abstract class AbstractPluginCollector extends AbstractInitOnce implements IActivePluginCollector
{
    protected Set<Class<?>> activePlugins;
    protected String defaultPluginPrefixGroup = "activePlugin.";
    private static String SCAN_ROOT = "com.cell";

    @Override
    public <T> List<T> getPlugins(Class<T> superclz)
    {
        List<Class<?>> clzs = getClassBySuperClz(this.activePlugins, superclz);
        return getTs(clzs);
    }

    protected abstract <T> T getInstance(Class<?> clz);

    <T> List<Class<?>> getClassBySuperClz(Collection<Class<?>> clzs, Class<T> superClz)
    {
        List<Class<?>> res = new ArrayList<>();
        for (Class<?> clz : clzs)
        {
            if (superClz.isAssignableFrom(clz))
            {
                res.add(clz);
            }
        }
        Collections.sort(res, new OrderComparator());
        return res;
    }

    <A extends Annotation> List<Class<?>> getClassByAnno(Collection<Class<?>> clzs, Class<A> ano)
    {
        List<Class<?>> res = new ArrayList<>();
        for (Class<?> clz : clzs)
        {
            if (clz.isAnnotationPresent(ano))
            {
                res.add(clz);
            }
        }
        Collections.sort(res, new OrderComparator());
        return res;
    }

    @Override
    public <A extends Annotation, T> List<T> getPlugins(Class<T> superclz, Class<A> ano)
    {
        List<Class<?>> clzs = getClassBySuperClz(this.activePlugins, superclz);
        clzs = getClassByAnno(clzs, ano);
        return getTs(clzs);
    }

    private <T> List<T> getTs(List<Class<?>> clzs)
    {
        List<T> res = new ArrayList<>();
        for (Class<?> clz : clzs)
        {
            T b = this.getInstance(clz);
            if (b == null)
            {
                LOG.erroring(Module.CONTAINER, "unexisted clz instance in bean factory: [%s]", clz);
            }
            res.add(b);
        }
        return res;
    }

    @Override
    public <A extends Annotation> List<Object> getPluginsByAnno(Class<A> ano)
    {
        return null;
    }


}
