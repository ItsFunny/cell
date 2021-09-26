package com.cell.postprocessor;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.annotation.HttpCmdAnno;
import com.cell.annotations.LifeCycle;
import com.cell.command.IHttpCommand;
import com.cell.context.InitCTX;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.enums.EnumLifeCycle;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.lang.annotation.Annotation;
import java.util.*;

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
    public void choseInterestAnnotations(Map<Class<? extends Annotation>, List<Class<?>>> classListMap)
    {
        List<Class<?>> cmds = classListMap.get(HttpCmdAnno.class);
        Map<Class<? extends IHttpReactor>, Set<Class<? extends IHttpCommand>>> reacotrCmds = new HashMap<>();
        for (Class<?> httpCmd : cmds)
        {
            HttpCmdAnno annotation = httpCmd.getAnnotation(HttpCmdAnno.class);
            Class<? extends IHttpReactor> reactor = annotation.reactor();
            Set<Class<? extends IHttpCommand>> classes = reacotrCmds.get(reactor);
            if (CollectionUtils.isEmpty(classes))
            {
                classes = new HashSet<>();
                reacotrCmds.put(reactor,classes);
            }
            classes.add((Class<? extends IHttpCommand>) httpCmd);
        }
        DefaultReactorHolder.setCommands(reacotrCmds);
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
