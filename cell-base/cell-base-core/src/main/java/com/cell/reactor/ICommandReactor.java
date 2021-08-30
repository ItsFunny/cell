package com.cell.reactor;

import com.cell.exceptions.CommandException;
import com.cell.protocol.ICommand;
import com.cell.protocol.IContext;

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
    void execute(IContext context) throws CommandException;

    void registerCmd(ICommand cmd);
}
