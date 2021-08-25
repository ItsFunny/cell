package com.cell.collector;

import com.cell.comparators.OrderComparator;
import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.enums.CellError;
import com.cell.log.LOG;
import com.cell.models.Module;
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
    private Set<Class<?>> activePlugins;
    private String defaultPluginPrefixGroup;


    @Override
    public <T> List<T> getPlugins(Class<T> superclz)
    {
        List<Class<?>> clzs = getClassBySuperClz(this.activePlugins, superclz);
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

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
