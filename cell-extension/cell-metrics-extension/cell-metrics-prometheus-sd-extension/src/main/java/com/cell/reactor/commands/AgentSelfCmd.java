package com.cell.reactor.commands;

import com.alibaba.fastjson.JSONObject;
import com.cell.annotations.HttpCmdAnno;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.context.IHttpCommandContext;
import com.cell.enums.EnumHttpRequestType;
import com.cell.reactor.ServiceReactor;
import com.cell.serialize.ISerializable;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-23 18:32
 */
@HttpCmdAnno(uri = "/agent/self", httpCommandId = 1, group = ServiceReactor.prometheusServiceReactor, requestType = EnumHttpRequestType.HTTP_URL_GET)
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
