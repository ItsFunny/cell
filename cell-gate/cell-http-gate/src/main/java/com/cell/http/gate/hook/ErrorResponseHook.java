package com.cell.http.gate.hook;

import com.cell.annotations.ManagerNode;
import com.cell.bee.event.center.EventCenter;
import com.cell.events.IEvent;
import com.cell.hooks.IChainHook;
import com.cell.hooks.IEventHook;
import com.cell.hooks.abs.AbstractEventHook;
import com.cell.log.LOG;
import com.cell.model.ErrorResponseEvent;
import com.cell.models.Module;
import com.cell.protocol.IEventContext;
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
