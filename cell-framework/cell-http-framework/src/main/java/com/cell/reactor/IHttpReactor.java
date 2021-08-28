package com.cell.reactor;

import com.cell.command.IHttpCommand;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 22:51
 */
public interface IHttpReactor extends IReactor
{
    List<IHttpCommand> getHttpCommandList();
}
