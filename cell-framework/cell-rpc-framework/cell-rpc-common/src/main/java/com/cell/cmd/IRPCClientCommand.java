package com.cell.cmd;

import com.cell.base.core.protocol.ICommand;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 08:55
 */
public interface IRPCClientCommand extends ICommand
{
    byte serializeType();
}
