package com.cell.manager;

import com.cell.hook.IHttpCommandHook;
import com.cell.utils.CollectionUtils;

import java.util.Collection;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 14:56
 */
public class CmdHookManager implements IReflectManager
{
    private static final CmdHookManager instance = new CmdHookManager();

    private IHttpCommandHook hook;

    private boolean setted;


    @Override
    public void invokeInterestNodes(Collection<Object> nodes)
    {
        if (instance.setted || CollectionUtils.isEmpty(nodes)) return;
        IHttpCommandHook tmp = instance.hook;
        for (Object node : nodes)
        {
            if (!(node instanceof IHttpCommandHook))
            {
                continue;
            }
            if (null == instance.hook)
            {
                instance.hook = (IHttpCommandHook) node;
                tmp = instance.hook;
            } else
            {
                tmp.registerNext((IHttpCommandHook) node);
                tmp = (IHttpCommandHook) node;
            }
        }
        instance.setted = true;
    }

    @Override
    public String name()
    {
        return "HOOK";
    }

    @Override
    public boolean override()
    {
        return false;
    }

}
