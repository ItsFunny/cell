package com.cell.reactor;

import com.cell.protocol.ICommand;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 22:45
 */
public interface ICommandReactor
{
    void execute(ICommand cmd);
}
