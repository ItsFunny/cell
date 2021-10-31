package com.cell.context;

import com.cell.channel.IChannel;
import com.cell.constants.DebugConstants;
import com.cell.context.summary.RPCServerSummary;
import com.cell.couple.IRPCServerRequest;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.protocol.*;
import com.cell.utils.UUIDUtils;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:51
 */
@Data
public class RPCServerCommandContext extends CommandContext
{
    private CommandProtocolID protocolID;

    public RPCServerCommandContext(IChannel<IHandler, IChainHandler> channel, IServerRequest request, IServerResponse httpResponse, CommandWrapper wrapper)
    {
        super(channel, request, httpResponse, wrapper);
    }

    @Override
    protected Summary collecSummary(IServerRequest request, CommandWrapper wrapper)
    {
        RPCServerSummary ret = new RPCServerSummary();
        IRPCServerRequest req = (IRPCServerRequest) request;
        ret.setRequestIP(request.getHeader(DebugConstants.IP));
        ret.setProtocolId(req.getProtocol());
        ret.setToken(getHeaderData(TOKEN));
        ret.setReceiveTimestamp(System.currentTimeMillis());
        ret.setSequenceId(getHeaderData(DebugConstants.SEQUENCE_ID, UUIDUtils.uuid2()));
        return ret;
    }

    @Override
    protected void onComplete(com.cell.concurrent.base.Future<? super Object> future) throws Exception
    {

    }


}
