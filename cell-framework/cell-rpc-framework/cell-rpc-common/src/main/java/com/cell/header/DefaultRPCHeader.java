package com.cell.header;

import com.cell.protocol.ICommand;
import com.cell.protocol.impl.AbstractBaseHeader;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 18:22
 */
@Data
public class DefaultRPCHeader extends AbstractBaseHeader
{
    public DefaultRPCHeader(ICommand command)
    {
        super(command);
    }

}
