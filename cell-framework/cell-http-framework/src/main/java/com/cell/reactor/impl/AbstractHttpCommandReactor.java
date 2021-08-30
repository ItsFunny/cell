package com.cell.reactor.impl;

import com.cell.protocol.ICommand;
import com.cell.reactor.IHttpReactor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 14:24
 */
public abstract  class AbstractHttpCommandReactor implements IHttpReactor
{
    @Override
    public void execute(ICommand cmd)
    {
    }
}
