package com.cell.extension;

import com.cell.annotations.Plugin;
import com.cell.context.INodeContext;
import com.cell.discovery.NacosNodeDiscoveryImpl;
import com.cell.sd.RegistrationService;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-23 05:56
 */
public class PrometheusExtension extends AbstractSpringNodeExtension
{

    private RegistrationService registrationService;

    @Plugin
    public RegistrationService registrationService()
    {
        return this.registrationService;
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        this.registrationService = new RegistrationService();
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
