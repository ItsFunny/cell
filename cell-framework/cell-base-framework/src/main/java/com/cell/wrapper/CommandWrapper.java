package com.cell.wrapper;

import com.cell.protocol.ICommand;
import com.cell.reactor.IReactor;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 22:18
 */
@Data
public class CommandWrapper
{
    private IReactor reactor;
    private Class<? extends ICommand> cmd;
}
