package com.cell.postprocessor;

import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.reactor.IHttpReactor;
import org.springframework.beans.BeansException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 07:58
 */
public class ReactorPostProcessor implements IBeanPostProcessortAdapter
{
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        if (bean instanceof IHttpCommandDispatcher)
        {
            DefaultReactorHolder.setDispatcher((IHttpCommandDispatcher) bean);
        } else if (bean instanceof IHttpReactor)
        {
            DefaultReactorHolder.addReactor((IHttpReactor) bean);
        }
        return bean;
    }
}
