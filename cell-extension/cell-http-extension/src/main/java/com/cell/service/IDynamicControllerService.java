package com.cell.service;

import com.cell.reactor.ICommandReactor;
import com.cell.reactor.IHttpReactor;

import java.util.Collection;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:17
 */
public interface IDynamicControllerService
{
    void registerReactor(IHttpReactor reactor);

    void batchRegisterReactor(Collection<ICommandReactor> reactors);
}
