package com.cell.event;

import com.cell.concurrent.base.Future;
import com.cell.concurrent.base.GenericFutureListener;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 18:42
 */
public interface IProcessEvent
{

    class EmptyNotifyEvent implements IProcessEvent
    {
    }
}
