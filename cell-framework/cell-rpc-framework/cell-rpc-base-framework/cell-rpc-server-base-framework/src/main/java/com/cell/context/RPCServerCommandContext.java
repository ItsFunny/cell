package com.cell.context;

import com.cell.base.common.constants.DebugConstants;
import com.cell.base.common.utils.UUIDUtils;
import com.cell.base.core.channel.IChannel;
import com.cell.base.core.concurrent.base.Future;
import com.cell.base.core.protocol.*;
import com.cell.context.summary.RPCServerSummary;
import com.cell.couple.IRPCServerRequest;
import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.protocol.*;
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
    protected void onComplete(Future<? super Object> future) throws Exception
    {

    }


}
