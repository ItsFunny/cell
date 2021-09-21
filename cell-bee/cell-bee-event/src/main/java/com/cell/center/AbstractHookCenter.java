package com.cell.center;

import com.cell.hooks.IChainHook;
import com.cell.hooks.IHook;
import com.cell.hooks.IListChainExecutor;
import com.cell.services.ChainExecutorFactory;
import com.cell.services.impl.DefaultHookMutableChainExecutor;
import com.cell.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-17 19:44
 */
public abstract class AbstractHookCenter extends AbstractReflectManager<IHook, IChainHook>
{
    public static final String GROUP_EVENT_CENTER = "GROUP_EVENT_CENTER";


    @Override
    protected ChainExecutorFactory<? extends IListChainExecutor> factory()
    {
        return () ->
                new DefaultHookMutableChainExecutor();
    }

    //    @Override
//    protected void onInvokeInterestNodes(Collection<Object> nodes)
//    {
//        if (CollectionUtils.isEmpty(nodes))
//        {
//            return;
//        }
//        for (Object node : nodes)
//        {
//            if (!(node instanceof IHook))
//            {
//                continue;
//            }
//            this.hooks.add((IHook) node);
//        }
//        this.afterInvoke();
//    }

    protected abstract void afterInvoke();


}
