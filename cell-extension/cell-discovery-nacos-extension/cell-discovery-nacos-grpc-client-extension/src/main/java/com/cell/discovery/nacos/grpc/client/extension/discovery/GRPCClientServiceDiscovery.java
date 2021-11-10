package com.cell.discovery.nacos.grpc.client.extension.discovery;

import com.cell.base.core.annotations.AutoPlugin;
import com.cell.base.common.constants.ProtocolConstants;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.base.common.context.InitCTX;
import com.cell.node.discovery.nacos.discovery.IInstanceOnChange;
import com.cell.node.discovery.nacos.discovery.abs.AbstractServiceDiscovery;
import com.cell.resolver.IKeyResolver;
import com.cell.resolver.impl.DefaultPureStringKeyResolver;
import com.cell.bee.transport.model.ServerMetaData;

import java.util.Collection;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 09:34
 */
public class GRPCClientServiceDiscovery extends AbstractServiceDiscovery<String, String>
{
    private static GRPCClientServiceDiscovery instance = null;



    @AutoPlugin
    public void setInstance(GRPCClientServiceDiscovery server)
    {
        GRPCClientServiceDiscovery.instance = server;
    }

    @Override
    protected Collection<ServerCmdMetaInfo> doGetServerByProtocol(String method, String protocol)
    {
        return this.serverMetas.get(protocol);
    }

    @Override
    protected void beforeInit(InitCTX ctx)
    {

    }

    @Override
    protected void afterInit(InitCTX ctx)
    {

    }

    @Override
    protected IKeyResolver<String, String> newKeyResolver()
    {
        return new DefaultPureStringKeyResolver();
    }

    @Override
    protected String resolve(IKeyResolver<String, String> resolver, ServerMetaData.ServerMetaCmd cmd)
    {
        return resolver.resolve(cmd.getProtocol());
    }

    @Override
    protected byte filterType()
    {
        return ProtocolConstants.TYPE_RPC;
    }
}
