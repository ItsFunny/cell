package com.cell.rpc.client.base.framework.command.impl;

import com.cell.protocol.ICommand;
import com.cell.protocol.impl.AbstractBaseHeader;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 13:09
 */
public class HttpHeader extends AbstractBaseHeader
{
    public HttpHeader(ICommand command)
    {
        super(command);
    }
}
