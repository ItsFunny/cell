package com.cell.node.discovery.nacos.discovery;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.cell.node.discovery.service.INodeDiscovery;


/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-09 15:56
 */
public interface INacosNodeDiscovery extends INodeDiscovery
{
    void registerListen(Subscriber<InstancesChangeEvent> subscriber);
}
