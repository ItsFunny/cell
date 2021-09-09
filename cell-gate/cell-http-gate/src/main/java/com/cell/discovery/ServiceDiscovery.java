package com.cell.discovery;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.cell.annotations.AutoPlugin;
import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.log.LOG;
import com.cell.model.Instance;
import com.cell.model.ServerMetaInfo;
import com.cell.models.Module;
import com.cell.service.INodeDiscovery;
import com.cell.transport.model.ServerMetaData;

import java.util.*;

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
    private INodeDiscovery nodeDiscovery;

    private static ServiceDiscovery instance;

    private Map<String, List<ServerMetaInfo>> serverMetas = new HashMap<>();

    @AutoPlugin
    public void setInstance(ServiceDiscovery serviceDiscovery)
    {
        ServiceDiscovery.instance = serviceDiscovery;
    }

    private ServiceDiscovery()
    {

    }

    public static ServiceDiscovery getInstance()
    {
        return instance;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        nodeDiscovery = NacosNodeDiscoveryImpl.getInstance(true, new InstanceHooker());
        Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList();
        Set<String> keys = serverInstanceList.keySet();
        keys.stream().forEach(k ->
        {
            List<Instance> instances = serverInstanceList.get(k);
            instances.stream().forEach(inst ->
                    {
                        final ServerMetaInfo info = new ServerMetaInfo();
                        info.setIp(inst.getIp());
                        info.setPort(Short.valueOf(String.valueOf(inst.getPort())));
                        info.setServiceName(k);

                        ServerMetaData metaData = ServerMetaData.fromMetaData(inst.getMetaData());
                        List<ServerMetaData.ServerMetaReactor> reactors = metaData.getReactors();
                        if (CollectionUtils.isEmpty(reactors)) return;
                        reactors.stream().forEach(r ->
                        {
                            List<ServerMetaData.ServerMetaCmd> cmds = r.getCmds();
                            if (CollectionUtils.isEmpty(cmds)) return;
                            cmds.stream().forEach(c ->
                            {
                                String uri = c.getUri();
                                List<ServerMetaInfo> serverMetaInfos = this.serverMetas.get(uri);
                                if (CollectionUtils.isEmpty(serverMetaInfos))
                                {
                                    serverMetaInfos = new ArrayList<>();
                                    this.serverMetas.put(uri, serverMetaInfos);
                                }
                                serverMetaInfos.add(info);
                            });
                        });
                    }
            );
        });
    }

    private class InstanceHooker extends Subscriber<InstancesChangeEvent>
    {
        @Override
        public void onEvent(InstancesChangeEvent event)
        {
            LOG.info(Module.HTTP_GATEWAY, "收到event:{}", event);
        }

        @Override
        public Class<? extends Event> subscribeType()
        {
            return InstancesChangeEvent.class;
        }
    }

}