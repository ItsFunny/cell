package com.cell.hook;

import com.cell.annotations.Manager;
import com.cell.center.AbstractHookCenter;
import com.cell.constant.HookConstants;
import com.cell.hooks.IChainHook;
import com.cell.manager.IReflectManager;
import com.cell.protocol.IContext;
import reactor.core.publisher.Mono;

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


    @Override
    public Mono<Void> execute(IContext context)
    {
        return this.pipeline.chainExecutor().execute(context);
    }
}
