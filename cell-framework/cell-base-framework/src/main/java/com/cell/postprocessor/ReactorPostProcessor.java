package com.cell.postprocessor;

import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.dispatcher.IDispatcher;
import com.cell.reactor.ICommandReactor;
import com.cell.root.Root;
import com.cell.server.IServer;
import org.springframework.beans.BeansException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 18:16
 */
public class ReactorPostProcessor implements IBeanPostProcessortAdapter
{
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        if (bean instanceof ICommandReactor)
        {
            Root.getInstance().addReactor((ICommandReactor) bean);
        } else if (bean instanceof IServer)
        {
            Root.getInstance().addServer((IServer) bean);
        } else if (bean instanceof IDispatcher)
        {
            Root.getInstance().addDispatcher((IDispatcher) bean);
        }
        return bean;
    }
}
