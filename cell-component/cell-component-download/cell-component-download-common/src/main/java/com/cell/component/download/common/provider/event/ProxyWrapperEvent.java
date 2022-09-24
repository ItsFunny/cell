package com.cell.component.download.common.provider.event;

import com.cell.base.core.protocol.IOrderEvent;
import com.cell.component.download.common.provider.ProxyProvider;
import lombok.Data;

@Data
public class ProxyWrapperEvent implements IOrderEvent
{
    private IOrderEvent internal;
    private ProxyProvider provider;

    public ProxyWrapperEvent(IOrderEvent internal, ProxyProvider provider)
    {
        this.internal = internal;
        this.provider = provider;
    }
}
