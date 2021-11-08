package com.cell.base.framework.postprocessor;

import com.cell.base.core.reactor.ICommandReactor;
import com.cell.base.framework.dispatcher.IDispatcher;
import com.cell.base.framework.root.Root;
import com.cell.base.framework.server.IServer;
import com.cell.node.spring.adapter.IBeanPostProcessortAdapter;
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
