package com.cell.protocol;

import com.cell.reactor.ICommandReactor;
import com.cell.reactor.IReactor;
import lombok.Builder;
import lombok.Data;
import sun.tools.jconsole.inspector.IconManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 15:20
 */
@Data
@Builder
public class ContextResponseWrapper
{
    private long status;
    private String msg;
    private ICommandReactor reactor;
    private Exception exception;
    private ICommand cmd;
    private Object ret;

}
