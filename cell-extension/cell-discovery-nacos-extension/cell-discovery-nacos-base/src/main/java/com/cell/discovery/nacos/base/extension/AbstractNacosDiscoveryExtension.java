package com.cell.discovery.nacos.base.extension;

import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.reactor.ICommandReactor;
import com.cell.base.core.utils.CommandUtils;
import com.cell.base.framework.dispatcher.IDispatcher;
import com.cell.base.framework.proxy.IFrameworkProxy;
import com.cell.base.framework.root.Root;
import com.cell.base.framework.server.IServer;
import com.cell.bee.transport.model.ServerMetaData;
import com.cell.node.core.configuration.NodeConfiguration;
import com.cell.node.core.context.INodeContext;
import com.cell.node.discovery.model.Instance;
import com.cell.node.discovery.nacos.discovery.NacosNodeDiscoveryImpl;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-02 10:18
 */
public abstract class AbstractNacosDiscoveryExtension extends AbstractSpringNodeExtension
{

    protected abstract Class<? extends IServer> serverClz();

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        NacosNodeDiscoveryImpl.setupDiscovery();
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {
//        // TODO: 通过配置来读取,而不是通过命令行的形式
        IServer server = Root.getInstance().getServer(this.serverClz());
        IFrameworkProxy proxy = (IFrameworkProxy) server.getProxy();
        IDispatcher dispatcher = proxy.getDispatcher();
        List<? extends ICommandReactor> reactors = dispatcher.getReactors();
        NodeConfiguration.NodeInstance nodeInstance = ctx.getInstanceByType(server.type());

        NacosNodeDiscoveryImpl nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();

        Map<Class<?>, ICommandReactor> reactorMap = new HashMap<>();
        for (ICommandReactor value : reactors)
        {
            // only use one
            reactorMap.put(value.getClass(), value);
        }
        ServerMetaData serverMetaData = new ServerMetaData();

        Collection<ICommandReactor> values = reactorMap.values();
        List<ServerMetaData.ServerMetaReactor> reacotrs = values.stream().map(value ->
        {
            ServerMetaData.ServerMetaReactor reactor = new ServerMetaData.ServerMetaReactor();

            List<Class<? extends ICommand>> commands = CommandUtils.getReactorCommands(value).get();
            List<ServerMetaData.ServerMetaCmd> cmds = commands.stream().map(c ->
                    this.getServerCmdMetaFrom(c)).collect(Collectors.toList());
            reactor.setCmds(cmds);
            return reactor;
        }).collect(Collectors.toList());
        serverMetaData.setReactors(reacotrs);
        ServerMetaData.ServerExtraInfo extraInfo = new ServerMetaData.ServerExtraInfo();
        ServerMetaData.NetworkInfo pub = new ServerMetaData.NetworkInfo();
        pub.setAddress(nodeInstance.getPublicAddress());
        pub.setPort(nodeInstance.getPublicPort());
        extraInfo.setPublicNetwork(pub);
        extraInfo.setType(this.supposedType());
        serverMetaData.setExtraInfo(extraInfo);

        Map<String, String> metadatas = ServerMetaData.toMetaData(serverMetaData);
        Instance instance = Instance.builder()
                .weight((byte) 1)
                .metaData(metadatas)
                .clusterName(ctx.getCluster())
                .ip(ctx.getIp())
                .healthy(true)
                .enable(true)
                .port((int) server.getPort())
                .serviceName(ctx.getApp().getApplicationName())
                .build();
        nodeDiscovery.registerServerInstance(instance);
    }

    protected abstract byte supposedType();

    protected abstract ServerMetaData.ServerMetaCmd getServerCmdMetaFrom(Class<? extends ICommand> c);

    // RPC ,HTTP ,GATEWAY 使用的不一定是相同的cmd line
//    protected abstract Integer getPort(INodeContext context);

    @Override
    protected void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext ctx) throws Exception
    {

    }
}
