package com.cell.hook;

import com.cell.annotations.CellOrder;
import com.cell.annotations.ManagerNode;
import com.cell.constant.HookConstants;
import com.cell.constants.Constants;
import com.cell.constants.ContextConstants;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.protocol.ICommand;
import com.cell.reactor.ICommandReactor;
import com.cell.reactor.IReactor;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 14:56
 */
@ManagerNode(group = HookConstants.GROUP_CMD_HOOK, name = HookConstants.COMMAND_HOOK_TIMEOUT, orderValue = 0)
public class CommandDefferResultHook extends AbstractHttpCommandHook
{
    @Override
    protected HttpCommandHookResult onDeltaHook(HookCommandWrapper wrapper)
    {
        DeferredResult<Object> result = wrapper.getContext().getResult();
        result.onTimeout(() ->
        {
            long currentTime = System.currentTimeMillis();
            long time = wrapper.getContext().getRequestTimestamp();
            final String sequenceId = wrapper.getContext().getSequenceId();
            LOG.warn(Module.HTTP_FRAMEWORK, "sequenceId = {}, handle command {} timeout[{}] receive time [{}]ms", sequenceId, currentTime - time, time);
            wrapper.getContext().response(ContextResponseWrapper.builder()
                    .status(ContextConstants.TIMEOUT)
                    .build());
        });
        return wrapper.getLastResult();
    }

    @Override
    protected void onTrackEnd(HttpCommandHookResult res)
    {

    }

    @Override
    protected void onExceptionCaught(Exception e)
    {

    }
}
