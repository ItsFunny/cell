package com.cell.extension;

import com.cell.annotations.Plugin;
import com.cell.base.IScheduleCounter;
import com.cell.config.GatePropertyNode;
import com.cell.config.GatewayConfiguration;
import com.cell.constants.CommandLineConstants;
import com.cell.context.INodeContext;
import com.cell.discovery.NacosNodeDiscoveryImpl;
import com.cell.discovery.ServiceDiscovery;
import com.cell.schedual.SchedualCaculateErrorCount;
import com.cell.utils.StringUtils;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 05:36
 */
public class NacosHttpGateExtension extends AbstractSpringNodeExtension implements WebServerFactoryCustomizer<ConfigurableWebServerFactory>
{
    private ServiceDiscovery serviceDiscovery;
    private IScheduleCounter schedualCaculateErrorCount;

    private int port = 9999;

    @Plugin
    public ServiceDiscovery serviceDiscovery()
    {
        return this.serviceDiscovery;
    }

    @Bean
    public IScheduleCounter scheduleCounter()
    {
        return this.schedualCaculateErrorCount;
    }


    @Override
    public void onInit(INodeContext ctx) throws Exception
    {
        Integer port = Integer.parseInt(ctx.getCommandLine().getOptionValue("port", "9999"));
        this.port = port;

        GatewayConfiguration.init();
        NacosNodeDiscoveryImpl.setupDiscovery();

        this.serviceDiscovery = new ServiceDiscovery();
        String cluster = ctx.getCommandLine().getOptionValue(CommandLineConstants.CLUSTER);
        cluster = StringUtils.isEmpty(cluster) ? CommandLineConstants.DEFAULT_CLSUTER_VALUE : cluster;
        this.serviceDiscovery.setCluster(cluster);

        GatePropertyNode gateway = GatewayConfiguration.getInstance().getGatePropertyNode();
        this.schedualCaculateErrorCount = new SchedualCaculateErrorCount(gateway.getErrorRespLimit(), gateway.getErrorStatInterval(), gateway.getErrorRespPercentage(), gateway.getGatewayErrTemplate());
    }

    @Override
    public void onStart(INodeContext ctx) throws Exception
    {
        this.serviceDiscovery.initOnce(null);
        this.schedualCaculateErrorCount.start();
    }

    @Override
    public void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void onClose(INodeContext ctx) throws Exception
    {
        this.schedualCaculateErrorCount.stop();
    }

    @Override
    public void customize(ConfigurableWebServerFactory factory)
    {
        factory.setPort(port);
    }
}
