package com.cell.postprocessor;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.annotation.RPCClient;
import com.cell.annotations.LifeCycle;
import com.cell.context.InitCTX;
import com.cell.enums.EnumLifeCycle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 10:04
 */
@LifeCycle(lifeCycle = EnumLifeCycle.ONCE)
public class ClientFactoryPostProcessor extends AbstractBeanDefiinitionRegistry
{
    @Override
    public List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor()
    {
        return Arrays.asList(ClientPostProcessor.class);
    }

    @Override
    public void choseInterestAnnotations(Map<Class<? extends Annotation>, List<Class<?>>> classListMap)
    {
        List<Class<?>> rpcClients = classListMap.get(RPCClient.class);

    }

    @Override
    protected void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {

    }

    @Override
    protected void onPostProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {

    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
