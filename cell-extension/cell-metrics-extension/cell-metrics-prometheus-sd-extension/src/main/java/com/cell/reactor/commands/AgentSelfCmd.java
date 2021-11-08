package com.cell.reactor.commands;

import com.alibaba.fastjson.JSONObject;
import com.cell.context.IHttpCommandContext;
import com.cell.enums.EnumHttpRequestType;
import com.cell.reactor.ServiceReactor;
import com.cell.rpc.grpc.client.framework.annotation.HttpCmdAnno;
import com.cell.rpc.grpc.client.framework.command.impl.AbstractHttpCommand;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-23 18:32
 */
@HttpCmdAnno(uri = "/agent/self", reactor = ServiceReactor.class, requestType = EnumHttpRequestType.HTTP_URL_GET)
public class AgentSelfCmd extends AbstractHttpCommand
{
    @Override
    protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
    {
        JSONObject configJo = new JSONObject();
        configJo.put("Datacenter", "default");
        JSONObject jo = new JSONObject();
        jo.put("Config", configJo);
        ctx.response(this.createResponseWp()
                .ret(jo).build());
    }
}
