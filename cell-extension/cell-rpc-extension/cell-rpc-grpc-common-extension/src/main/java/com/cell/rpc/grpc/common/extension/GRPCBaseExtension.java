package com.cell.rpc.grpc.common.extension;

import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import org.apache.commons.cli.Options;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-11 09:28
 */
public class GRPCBaseExtension extends AbstractSpringNodeExtension
{
    @Override
    public Options getOptions()
    {
        Options ret = new Options();
        ret.addOption("grpcPort", true, "grpc 端口号");
        ret.addOption("grpcAddr", true, "本地的grpcAddr,默认为localhost");
        return ret;
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext ctx) throws Exception
    {

    }
}
