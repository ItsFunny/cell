package com.cell.component.download.bsc.wrapper;

import com.cell.base.core.protocol.IOrderEvent;
import com.cell.component.download.bsc.provider.BSCProvider;
import lombok.Data;

@Data
public class BSCEventWrapper implements IOrderEvent
{
    private IOrderEvent internal;
    private BSCProvider bscProvider;

    public BSCEventWrapper(IOrderEvent internal, BSCProvider bscProvider)
    {
        this.internal = internal;
        this.bscProvider = bscProvider;
    }
}
