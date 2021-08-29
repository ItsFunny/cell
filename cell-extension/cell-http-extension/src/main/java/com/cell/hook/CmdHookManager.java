package com.cell.hook;

import com.cell.manager.IReflectManager;
import com.cell.utils.CollectionUtils;
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
public class CmdHookManager implements IReflectManager
{
    private static final CmdHookManager instance = new CmdHookManager();

    private IHttpCommandHook hook;

    private boolean setted;

    public static CmdHookManager getInstance()
    {
        return instance;
    }


    @Override
    public void invokeInterestNodes(Collection<Object> nodes)
    {
        if (this.setted || CollectionUtils.isEmpty(nodes)) return;
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
        this.setted = true;
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
