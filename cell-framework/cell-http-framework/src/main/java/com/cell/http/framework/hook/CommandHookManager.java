package com.cell.http.framework.hook;

import com.cell.base.core.annotations.Manager;
import com.cell.base.core.hooks.IChainHook;
import com.cell.bee.event.center.AbstractHookCenter;
import com.cell.http.framework.constant.HookConstants;
import com.cell.plugin.pipeline.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 04:59
 */
@Manager(name = HookConstants.GROUP_CMD_HOOK)
public class CommandHookManager extends AbstractHookCenter implements IChainHook
{
    private static final CommandHookManager instance = new CommandHookManager();

    public static CommandHookManager getInstance()
    {
        return instance;
    }

    @Override
    protected void afterInvoke()
    {

    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
