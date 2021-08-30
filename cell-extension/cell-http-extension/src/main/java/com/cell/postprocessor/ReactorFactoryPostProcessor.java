package com.cell.postprocessor;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.annotations.Exclude;
import com.cell.annotations.LifeCycle;
import com.cell.annotations.Plugin;
import com.cell.annotations.ReactorAnno;
import com.cell.context.InitCTX;
import com.cell.enums.EnumLifeCycle;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.reactor.IDynamicHttpReactor;
import com.cell.utils.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import sun.reflect.misc.ReflectUtil;

import java.lang.annotation.Annotation;
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
    public void choseInterestAnnotations(Map<Class<? extends Annotation>, List<Class<?>>> classListMap)
    {
        List<Class<?>> reactors = classListMap.get(ReactorAnno.class);
        if (CollectionUtils.isEmpty(reactors)){
            return;
        }
        for (Class<?> reactor : reactors)
        {
            if (reactor.getAnnotation(Plugin.class) != null || reactor.getAnnotation(Exclude.class) != null)
            {
                continue;
            }
            try
            {
                if (!IDynamicHttpReactor.class.isAssignableFrom(reactor))
                {
                    continue;
                }
                Object o = ReflectUtil.newInstance(reactor);
                ReactorCache.register(reactor, (IDynamicHttpReactor) o);
            } catch (Exception e)
            {
                LOG.warning(Module.CONTAINER, e, "注册失败");
            }
        }
    }

    @Override
    public List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor()
    {
        return null;
    }



    @Override
    protected void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {
        System.out.println(2);
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
