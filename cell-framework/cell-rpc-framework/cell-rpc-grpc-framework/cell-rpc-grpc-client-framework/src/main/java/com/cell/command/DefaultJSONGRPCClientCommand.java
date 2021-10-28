package com.cell.command;

import com.cell.cmd.impl.AbstractRPCClientCommand;
import com.cell.constants.ProtocolConstants;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 14:42
 */
public class DefaultJSONGRPCClientCommand<REQ, RESP> extends AbstractRPCClientCommand
{


    @Override
    public byte serializeType()
    {
        return ProtocolConstants.SERIALIZE_JSON;
    }
}
