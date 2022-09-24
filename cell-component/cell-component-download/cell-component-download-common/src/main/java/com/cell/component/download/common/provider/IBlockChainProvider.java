package com.cell.component.download.common.provider;

import com.cell.base.core.protocol.IOrderEvent;

public interface IBlockChainProvider
{
    void start();

    void dispatch(IOrderEvent event);

    String chainAlias();
}
