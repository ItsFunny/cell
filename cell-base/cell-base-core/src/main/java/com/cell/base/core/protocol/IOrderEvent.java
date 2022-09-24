package com.cell.base.core.protocol;

import com.cell.base.common.events.IEvent;

public interface IOrderEvent extends IEvent,IContext
{

    default int getOrder(){
        return 0;
    }

}
