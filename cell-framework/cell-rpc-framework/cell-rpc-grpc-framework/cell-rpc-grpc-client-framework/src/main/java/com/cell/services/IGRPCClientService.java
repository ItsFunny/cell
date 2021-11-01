package com.cell.services;

import com.cell.concurrent.base.Future;
import com.cell.protocol.IBuzzContext;
import com.cell.serialize.ISerializable;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 16:34
 */
public interface IGRPCClientService
{
    Future<Object> call(IBuzzContext ctx, ISerializable req);
}
