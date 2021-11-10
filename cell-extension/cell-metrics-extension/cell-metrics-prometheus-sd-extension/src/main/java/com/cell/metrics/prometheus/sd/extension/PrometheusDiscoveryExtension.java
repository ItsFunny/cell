package com.cell.metrics.prometheus.sd.extension;

import com.cell.base.common.constants.CommandLineConstants;
import com.cell.base.common.constants.OrderConstants;
import com.cell.base.core.annotations.CellOrder;
import com.cell.base.core.annotations.Plugin;
import com.cell.metrics.prometheus.sd.extension.sd.IPrometheusServiceDiscovery;
import com.cell.metrics.prometheus.sd.extension.sd.RegistrationService;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-23 05:56
 */
@CellOrder(value = OrderConstants.HTTP_NACOS_DISCOVERY_EXTENSION + 1)
public class PrometheusDiscoveryExtension extends AbstractSpringNodeExtension
{

    private IPrometheusServiceDiscovery registrationService;

    @Plugin
    public IPrometheusServiceDiscovery registrationService()
    {
        return this.registrationService;
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        this.registrationService = new RegistrationService();
        String cluster = ctx.getCommandLine().getOptionValue(CommandLineConstants.CLUSTER);
        cluster = StringUtils.isEmpty(cluster) ? CommandLineConstants.DEFAULT_CLSUTER_VALUE : cluster;
//        this.registrationService.setCluster(cluster);
        this.registrationService.initOnce(null);
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
