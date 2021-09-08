package com.cell.discovery;

import com.cell.annotations.AutoPlugin;
import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.model.Instance;
import com.cell.protocol.AbstractCommand;
import com.cell.service.INodeDiscovery;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 05:41
 */
public class ServiceDiscovery extends AbstractInitOnce
{
    @AutoPlugin
    private INodeDiscovery nodeDiscovery;


    private static ServiceDiscovery instance;

    @AutoPlugin
    public void setInstance(ServiceDiscovery serviceDiscovery)
    {
        ServiceDiscovery.instance = serviceDiscovery;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        nodeDiscovery.initOnce(ctx);
        Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList();

    }
}
