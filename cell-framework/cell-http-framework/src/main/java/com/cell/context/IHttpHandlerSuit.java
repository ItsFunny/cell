package com.cell.context;

import com.cell.protocol.ICommandSuit;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 06:12
 */
public interface IHttpHandlerSuit extends ICommandSuit
{
    IHttpCommandContext getBuzContext();
}
