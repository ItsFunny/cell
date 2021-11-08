package com.cell.base.core.reactor;

import com.cell.base.common.context.IInitOnce;
import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.protocol.IContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 22:45
 */
public interface ICommandReactor extends IInitOnce
{
    void execute(IContext context);

    void registerCmd(ICommand cmd);
}
