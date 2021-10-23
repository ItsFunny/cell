package com.cell.context;

import com.cell.concurrent.base.Future;
import com.cell.concurrent.base.GenericFutureListener;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 15:29
 */
@Data
public class DispatchContext
{
    private IServerRequest serverRequest;
    private IServerResponse serverResponse;
    private GenericFutureListener<? extends Future<? super Object>> onOperationComplete;

}
