package com.cell.postprocessor;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.annotations.Exclude;
import com.cell.annotations.LifeCycle;
import com.cell.annotations.Plugin;
import com.cell.annotations.ReactorAnno;
import com.cell.context.InitCTX;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.enums.EnumLifeCycle;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.reactor.IDynamicHttpReactor;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.CollectionUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import sun.reflect.misc.ReflectUtil;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:53
 */
@LifeCycle(lifeCycle = EnumLifeCycle.ONCE)
public class ReactorFactoryPostProcessor extends AbstractBeanDefiinitionRegistry
{
    @Override
    public List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor()
    {
        return Arrays.asList(ReactorPostProcessor.class);
    }


    @Override
    protected void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {
        Collection<IHttpReactor> reactors = DefaultReactorHolder.getReactors();
        for (IHttpReactor reactor : reactors)
        {
            this.defaultRegisterBean(registry, reactor.getClass());
        }
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
