package com.cell.event;

import com.cell.concurrent.base.Promise;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 18:49
 */
@Data
public class ProcessEvent
{
    private byte proxyId;
    private Promise<Object> promise;
    private IProcessEvent internalProcessEvent;
}
