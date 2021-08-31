package com.cell.hook;

import com.cell.annotations.CellOrder;
import com.cell.annotations.ManagerNode;
import com.cell.constant.HookConstants;
import com.cell.exceptions.CommandException;
import com.cell.exceptions.InternalWrapperException;
import com.cell.reactor.IHttpReactor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 22:12
 */
@ManagerNode(group = HookConstants.GROUP_HOOK, name = HookConstants.NAME_HOOK, orderValue = Integer.MAX_VALUE)
public class CommandExecuteHook extends AbstractHttpCommandHook
{
    @Override
    protected HttpCommandHookResult onDeltaHook(HookCommandWrapper wrapper)
    {
        IHttpReactor reactor = wrapper.getReactor();
        try
        {
            reactor.execute(wrapper.getContext());
        } catch (CommandException e)
        {
            // FIXME ?
            throw new InternalWrapperException(e);
        }
        HttpCommandHookResult res = new HttpCommandHookResult();
        res.setContext(wrapper.getContext());
        return res;
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
