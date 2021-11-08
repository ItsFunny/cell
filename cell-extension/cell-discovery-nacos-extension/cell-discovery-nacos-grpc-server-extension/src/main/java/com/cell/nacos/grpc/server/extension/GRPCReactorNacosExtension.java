package com.cell.nacos.grpc.server.extension;

import com.cell.annotations.CellOrder;
import com.cell.base.common.constants.ProtocolConstants;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.discovery.nacos.discovery.NacosNodeDiscoveryImpl;
import com.cell.dispatcher.IRPCServerCommandDispatcher;
import com.cell.grpc.server.framework.server.IGRPCServer;
import com.cell.model.Instance;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.protocol.ICommand;
import com.cell.proxy.IRPCProxy;
import com.cell.reactor.ICommandReactor;
import com.cell.reactor.IRPCServerReactor;
import com.cell.root.Root;
import com.cell.rpc.server.base.annotation.RPCServerCmdAnno;
import com.cell.transport.model.ServerMetaData;
import com.cell.utils.ClassUtil;
import com.cell.utils.CommandUtils;

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
 * @Date 创建时间：2021-11-02 08:53
 */
@CellOrder(value = OrderConstants.RPC_NACOS_DISCOVERY_EXTENSION)
// TODO: 公共基类
public class GRPCReactorNacosExtension extends AbstractSpringNodeExtension
{

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        NacosNodeDiscoveryImpl.setupDiscovery();
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {
        IGRPCServer server = (IGRPCServer) Root.getInstance().getServer(IGRPCServer.class);
        IRPCProxy proxy = (IRPCProxy) server.getProxy();
        IRPCServerCommandDispatcher dispatcher = (IRPCServerCommandDispatcher) proxy.getDispatcher();
        List<? extends ICommandReactor> reactors = dispatcher.getReactors();
        String domain = ctx.getCommandLine().getOptionValue("domain", "demo.com");
        NacosNodeDiscoveryImpl nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();
        if (reactors == null || reactors.isEmpty()) return;
        Map<Class<?>, IRPCServerReactor> reactorMap = new HashMap<>();
        for (ICommandReactor value : reactors)
        {
            // only use one
            reactorMap.put(value.getClass(), (IRPCServerReactor) value);
        }
        ServerMetaData serverMetaData = new ServerMetaData();
        Collection<IRPCServerReactor> values = reactorMap.values();
        List<ServerMetaData.ServerMetaReactor> reacotrs = values.stream().map(value ->
        {
            ServerMetaData.ServerMetaReactor reactor = new ServerMetaData.ServerMetaReactor();

            List<Class<? extends ICommand>> commands = CommandUtils.getReactorCommands(value).get();
            List<ServerMetaData.ServerMetaCmd> cmds = commands.stream().map(c ->
            {
                ServerMetaData.ServerMetaCmd cmd = new ServerMetaData.ServerMetaCmd();
                RPCServerCmdAnno annotation = (RPCServerCmdAnno) ClassUtil.mustGetAnnotation(c, RPCServerCmdAnno.class);
                cmd.setProtocol(annotation.protocol());
//                cmd.setUri(annotation.uri());
//                cmd.setModule(annotation.module().name());
//                cmd.setMethod(annotation.requestType().getId());
                return cmd;
            }).collect(Collectors.toList());
            reactor.setCmds(cmds);
            return reactor;
        }).collect(Collectors.toList());
        serverMetaData.setReactors(reacotrs);
        ServerMetaData.ServerExtraInfo extraInfo = new ServerMetaData.ServerExtraInfo();
        extraInfo.setDomain(domain);
        extraInfo.setType(ProtocolConstants.TYPE_RPC);
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

    @Override
    protected void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext ctx) throws Exception
    {

    }
}
