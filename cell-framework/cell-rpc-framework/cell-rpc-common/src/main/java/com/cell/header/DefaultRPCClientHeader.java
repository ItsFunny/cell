package com.cell.header;

import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.protocol.impl.AbstractBaseHeader;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 08:56
 */
@Data
public class DefaultRPCClientHeader extends AbstractBaseHeader
{

    public DefaultRPCClientHeader(ICommand command)
    {
        super(command);
    }
}
