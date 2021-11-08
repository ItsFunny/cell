package com.cell.http.gate.hook;

import com.cell.base.core.annotations.ManagerNode;
import com.cell.base.common.models.Module;
import com.cell.constant.HookConstants;
import com.cell.base.core.constants.ContextConstants;
import com.cell.context.IHttpCommandContext;
import com.cell.base.core.hooks.IChainHook;
import com.cell.base.core.log.LOG;
import com.cell.base.core.protocol.ContextResponseWrapper;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 14:56
 */
@ManagerNode(group = HookConstants.GROUP_CMD_HOOK,
        name = HookConstants.COMMAND_HOOK_TIMEOUT,
        orderValue = 0)
public class CommandDefferResultHook extends AbstractHttpCommandHook
{
//    @Override
//    protected Mono<Void> hook(IContext context, IChainExecutor executor)
//    {
//        IHttpHandlerSuit suit = (IHttpHandlerSuit) context;
//        return super.hook(suit.getBuzContext(), executor);
//    }

    @Override
    protected Mono<Void> doHook(IHttpCommandContext ctx, IChainHook hook)
    {
        DeferredResult<Object> result = ctx.getResult();
        result.onTimeout(() ->
        {
            long currentTime = System.currentTimeMillis();
            long time = ctx.getRequestTimestamp();
            final String sequenceId = ctx.getSummary().getSequenceId();
            LOG.warn(Module.HTTP_FRAMEWORK, "sequenceId = {}, handle command {} timeout[{}] receive time {}ms", sequenceId, ctx.getURI(), currentTime - time, time);
            ctx.response(ContextResponseWrapper.builder()
                    .status(ContextConstants.TIMEOUT)
                    .build());
        });
        return hook.execute(ctx).doOnError((e) ->
        {
            this.onExceptionCaught(e);
        }).then(Mono.fromRunnable(() ->
        {
        }));
    }
}
