package com.cell.extension.blockchain.web3j.commands;

import com.cell.base.common.enums.EnumHttpRequestType;
import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.command.impl.AbstractHttpCommand;
import com.cell.http.framework.context.IHttpCommandContext;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/3/17 4:15 下午
 */
@HttpCmdAnno(uri = "/web3j/deploy", requestType = EnumHttpRequestType.HTTP_URL_GET)
public class DeployCommand extends AbstractHttpCommand
{
    @Override
    protected void onExecute(IHttpCommandContext ctx, Object o) throws IOException
    {
    }
}
