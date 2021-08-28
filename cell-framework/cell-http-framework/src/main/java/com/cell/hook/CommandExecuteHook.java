package com.cell.hook;

import com.cell.annotations.CellOrder;
import com.cell.annotations.ManagerNode;
import com.cell.command.IHttpCommand;
import com.cell.constant.HookConstants;
import com.cell.hooks.IDeltaChainHook;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 22:12
 */
@ManagerNode(group = HookConstants.GROUP_HOOK, name = HookConstants.NAME_HOOK)
@CellOrder(value = Integer.MAX_VALUE)
public class CommandExecuteHook extends AbstractHttpCommandHook
{

    @Override
    protected HttpCommandHookResult onDeltaHook(HookCommandWrapper wrapper)
    {
        IHttpCommand cmd = wrapper.getCmd();
        return null;
    }

    @Override
    public IDeltaChainHook<HookCommandWrapper, HttpCommandHookResult> next()
    {
        return null;
    }
}
