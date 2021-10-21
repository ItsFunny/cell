package com.cell.context;

import com.cell.model.RPCServerSummary;
import com.cell.protocol.CommandProtocolID;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;
import lombok.Data;

import java.util.concurrent.Future;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:51
 */
@Data
public class RPCServerCommandContext
{
    private IServerRequest request;
    private IServerResponse response;
    private RPCServerSummary summary;
    private CommandProtocolID protocolID;

    private Future<Object>result;
}
