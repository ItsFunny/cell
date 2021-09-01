package com.cell.postprocessor;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.annotations.LifeCycle;
import com.cell.context.InitCTX;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.enums.EnumLifeCycle;
import com.cell.reactor.IHttpReactor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    }

    @Override
    protected void onPostProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {
        System.out.println("----");
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
