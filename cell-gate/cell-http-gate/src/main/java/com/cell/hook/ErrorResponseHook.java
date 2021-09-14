package com.cell.hook;

import com.cell.annotations.ManagerNode;
import com.cell.center.EventCenter;
import com.cell.events.IEvent;
import com.cell.hooks.IEventHook;
import com.cell.hooks.IHookChain;
import com.cell.log.LOG;
import com.cell.model.ErrorResponseEvent;
import com.cell.models.Module;
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
public class ErrorResponseHook implements IEventHook
{

    @Override
    public Mono<Void> hook(IEvent event, IHookChain hook)
    {
        LOG.info(Module.HTTP_GATEWAY, "收到event {}", event);
        return hook.hook(event);
    }

    @Override
    public boolean predict(IEvent event)
    {
        return event instanceof ErrorResponseEvent;
    }
}
