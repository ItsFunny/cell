package com.cell.component.download.bsc.provider;

import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.base.core.protocol.IContext;
import com.cell.base.core.protocol.IOrderEvent;
import com.cell.component.download.bsc.wrapper.BSCEventWrapper;
import com.cell.component.download.common.provider.AbstractProvider;

public class BSCProvider extends AbstractProvider
{
    private String chainAlias;


    public BSCProvider(String chainAlias, EventLoopGroup eventExecutors)
    {
        super(eventExecutors);
        this.chainAlias = chainAlias;
    }


    @Override
    protected void onStart()
    {

    }

    @Override
    protected IContext wrap(IOrderEvent event)
    {
        return new BSCEventWrapper(event, this);
    }

    @Override
    public String chainAlias()
    {
        return this.chainAlias;
    }
}
