package com.cell.metrics.prometheus.sd.extension.sd;

import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.common.context.InitCTX;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.discovery.nacos.discovery.abs.AbstractServiceDiscovery;
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
 * @Date 创建时间：2021-11-08 09:29
 */
public class PrometheusServiceDiscovery extends AbstractServiceDiscovery<String, String>
{

    @Override
    protected Collection<ServerCmdMetaInfo> doGetServerByProtocol(String method, String protocol)
    {
        throw new RuntimeException("not supposed");
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
        return this.resolver.resolve(cmd.getProtocol());
    }


    @Override
    protected byte filterType()
    {
        return ProtocolConstants.TYPE_HTTP;
    }
}
