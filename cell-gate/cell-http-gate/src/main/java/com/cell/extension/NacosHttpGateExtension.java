package com.cell.extension;

import com.cell.annotations.Plugin;
import com.cell.constants.CommandLineConstants;
import com.cell.context.INodeContext;
import com.cell.discovery.ServiceDiscovery;
import com.cell.utils.StringUtils;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 05:36
 */
public class NacosHttpGateExtension extends AbstractSpringNodeExtension
{
    private ServiceDiscovery serviceDiscovery;

    @Plugin
    public ServiceDiscovery serviceDiscovery()
    {
        return this.serviceDiscovery;
    }

    @Override
    public void init(INodeContext ctx) throws Exception
    {
        this.serviceDiscovery = new ServiceDiscovery();
        String cluster = ctx.getCommandLine().getOptionValue(CommandLineConstants.CLUSTER);
        cluster = StringUtils.isEmpty(cluster) ? CommandLineConstants.DEFAULT_CLSUTER_VALUE : cluster;
        this.serviceDiscovery.setCluster(cluster);
    }

    @Override
    public void start(INodeContext ctx) throws Exception
    {
        this.serviceDiscovery.initOnce(null);
    }

    @Override
    public void ready(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void close(INodeContext ctx) throws Exception
    {

    }

}
