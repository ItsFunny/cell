package com.cell.manager;

import com.cell.base.core.annotations.ManagerNode;
import com.cell.base.core.executor.IBaseReactorExecutor;
import com.cell.rpc.grpc.client.framework.annotation.ActiveMethod;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-24 04:57
 */
@ManagerNode(group = ReactorSelectorManager.reacotrSelector)
public class DefaultPureUriSelector
{
    @ActiveMethod(id = ReactorSelectorManager.selectByUri)
    public IBaseReactorExecutor selectByUri()
    {
        return (ctx, ch) ->
                ch.execute(ctx);
    }

    @ActiveMethod(id = ReactorSelectorManager.onAddReactor)
    public IBaseReactorExecutor onAddReactor()
    {
        return (ctx, ch) -> ch.execute(ctx);
    }
}
