package com.cell.http.gate.hook;

import com.cell.base.core.annotations.ManagerNode;
import com.cell.base.core.center.EventCenter;
import com.cell.base.core.events.IEvent;
import com.cell.base.core.hooks.IChainHook;
import com.cell.base.core.hooks.IEventHook;
import com.cell.hooks.abs.AbstractEventHook;
import com.cell.base.core.log.LOG;
import com.cell.model.ErrorResponseEvent;
import com.cell.base.common.models.Module;
import com.cell.base.core.protocol.IEventContext;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 21:57
 */
@ManagerNode(group = EventCenter.GROUP_EVENT_CENTER, name = "ERROR_RESPONSE")
public class ErrorResponseHook extends AbstractEventHook implements IEventHook
{
    @Override
    protected Mono<Void> onEventHook(IEventContext context, IChainHook hook)
    {
        LOG.info(Module.HTTP_GATEWAY, "收到event {}", context.getEvent());
        return hook.execute(context);
    }

    @Override
    public boolean onPredict(IEvent event)
    {
        return event instanceof ErrorResponseEvent;
    }


}
