package com.cell.discovery;

import com.cell.Configuration;
import com.cell.utils.ReflectUtil;
import org.junit.Test;

public class ServiceDiscoveryTest
{
    @Test
    public void testGetInstance()
    {
        Configuration.autoInitialize();
        ServiceDiscovery discovery = (ServiceDiscovery) ReflectUtil.newInstance(ServiceDiscovery.class);
        discovery.setInstance(discovery);
        discovery.onInit(null);
    }

}