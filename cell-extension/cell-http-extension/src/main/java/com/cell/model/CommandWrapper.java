package com.cell.model;

import com.cell.command.IHttpCommand;
import com.cell.reactor.IHttpReactor;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-24 05:50
 */
@Data
public class CommandWrapper
{
    private IHttpReactor reactor;
    private Class<? extends IHttpCommand> cmd;

}
