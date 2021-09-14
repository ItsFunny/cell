package com.cell.hook;

import com.cell.annotations.Manager;
import com.cell.constant.HookConstants;
import com.cell.manager.AbstractReflectManager;
import com.cell.manager.IReflectManager;
import lombok.Data;

import java.util.Collection;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 14:56
 */
@Data
@Manager(name = HookConstants.GROUP_CMD_HOOK)
public class CmdHookManager extends AbstractReflectManager
{
    private static final CmdHookManager instance = new CmdHookManager();

    private IHttpCommandHook hook;


    public static CmdHookManager getInstance()
    {
        return instance;
    }


    @Override
    protected void onInvokeInterestNodes(Collection<Object> nodes)
    {
        IHttpCommandHook tmp = this.hook;
        for (Object node : nodes)
        {
            if (!(node instanceof IHttpCommandHook))
            {
                continue;
            }
            if (null == this.hook)
            {
                this.hook = (IHttpCommandHook) node;
                tmp = this.hook;
            } else
            {
                tmp.registerNext((IHttpCommandHook) node);
                tmp = (IHttpCommandHook) node;
            }
        }
    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
