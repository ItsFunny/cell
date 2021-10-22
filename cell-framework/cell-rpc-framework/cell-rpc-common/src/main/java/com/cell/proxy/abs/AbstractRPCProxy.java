package com.cell.proxy.abs;

import com.cell.dispatcher.IDispatcher;
import com.cell.proxy.AbstractBaseFrameworkProxy;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 22:59
 */
public abstract class AbstractRPCProxy extends AbstractBaseFrameworkProxy
{
    public AbstractRPCProxy(IDispatcher dispatcher)
    {
        super(dispatcher);
    }
}
