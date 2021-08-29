package com.cell.reactor;

import com.cell.command.IDynamicHttpCommand;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:22
 */
public interface IDynamicHttpReactor extends IReactor
{
    List<IDynamicHttpCommand> getCmds();
}
