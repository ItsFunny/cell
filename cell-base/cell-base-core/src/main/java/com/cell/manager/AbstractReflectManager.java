package com.cell.manager;

import com.cell.utils.CollectionUtils;

import java.util.Collection;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 13:11
 */
public abstract class AbstractReflectManager implements IReflectManager
{
    private boolean setted;

    @Override
    public void invokeInterestNodes(Collection<Object> nodes)
    {
        if (this.setted || CollectionUtils.isEmpty(nodes)) return;
        this.onInvokeInterestNodes(nodes);
        this.setted = true;
    }

    protected abstract void onInvokeInterestNodes(Collection<Object> nodes);
}
