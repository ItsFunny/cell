package com.cell.protocol;

import com.cell.reactor.ICommandReactor;
import com.cell.reactor.IReactor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import sun.tools.jconsole.inspector.IconManager;

import java.util.HashMap;
import java.util.Map;

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
    private Exception exception;
    private ICommand cmd;
    private Object ret;

    // 从哪里调用了该 wp
    private String from;

    private Object other;
}
