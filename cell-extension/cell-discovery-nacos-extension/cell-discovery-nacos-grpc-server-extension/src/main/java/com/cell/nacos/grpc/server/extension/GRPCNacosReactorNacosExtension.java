package com.cell.nacos.grpc.server.extension;

import com.cell.base.common.constants.OrderConstants;
import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.core.annotations.CellOrder;
import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.utils.ClassUtil;
import com.cell.base.framework.server.IServer;
import com.cell.bee.transport.model.ServerMetaData;
import com.cell.discovery.nacos.base.extension.AbstractNacosDiscoveryExtension;
import com.cell.grpc.server.framework.server.IGRPCServer;
import com.cell.node.core.context.INodeContext;
import com.cell.rpc.server.base.framework.annotation.RPCServerCmdAnno;
import org.apache.commons.cli.CommandLine;

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
public class GRPCNacosReactorNacosExtension extends AbstractNacosDiscoveryExtension
{
    @Override
    protected Class<? extends IServer> serverClz()
    {
        return IGRPCServer.class;
    }

//    @Override
//    protected void onStart(INodeContext ctx) throws Exception
//    {
//        IGRPCServer server = (IGRPCServer) Root.getInstance().getServer(IGRPCServer.class);
//        IRPCProxy proxy = (IRPCProxy) server.getProxy();
//        IRPCServerCommandDispatcher dispatcher = (IRPCServerCommandDispatcher) proxy.getDispatcher();
//        List<? extends ICommandReactor> reactors = dispatcher.getReactors();
//        String domain = ctx.getCommandLine().getOptionValue("domain", "demo.com");
//        NacosNodeDiscoveryImpl nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();
//        if (reactors == null || reactors.isEmpty()) return;
//        Map<Class<?>, IRPCServerReactor> reactorMap = new HashMap<>();
//        for (ICommandReactor value : reactors)
//        {
//            // only use one
//            reactorMap.put(value.getClass(), (IRPCServerReactor) value);
//        }
//        ServerMetaData serverMetaData = new ServerMetaData();
//        Collection<IRPCServerReactor> values = reactorMap.values();
//        List<ServerMetaData.ServerMetaReactor> reacotrs = values.stream().map(value ->
//        {
//            ServerMetaData.ServerMetaReactor reactor = new ServerMetaData.ServerMetaReactor();
//
//            List<Class<? extends ICommand>> commands = CommandUtils.getReactorCommands(value).get();
//            List<ServerMetaData.ServerMetaCmd> cmds = commands.stream().map(c ->
//            {
//                ServerMetaData.ServerMetaCmd cmd = new ServerMetaData.ServerMetaCmd();
//                RPCServerCmdAnno annotation = (RPCServerCmdAnno) ClassUtil.mustGetAnnotation(c, RPCServerCmdAnno.class);
//                cmd.setProtocol(annotation.protocol());
////                cmd.setUri(annotation.uri());
////                cmd.setModule(annotation.module().name());
////                cmd.setMethod(annotation.requestType().getId());
//                return cmd;
//            }).collect(Collectors.toList());
//            reactor.setCmds(cmds);
//            return reactor;
//        }).collect(Collectors.toList());
//        serverMetaData.setReactors(reacotrs);
//        ServerMetaData.ServerExtraInfo extraInfo = new ServerMetaData.ServerExtraInfo();
//        extraInfo.setDomain(domain);
//        extraInfo.setType(ProtocolConstants.TYPE_RPC);
//        serverMetaData.setExtraInfo(extraInfo);
//
//        Map<String, String> metadatas = ServerMetaData.toMetaData(serverMetaData);
//
//        Instance instance = Instance.builder()
//                .weight((byte) 1)
//                .metaData(metadatas)
//                .clusterName(ctx.getCluster())
//                .ip(ctx.getIp())
//                .healthy(true)
//                .enable(true)
//                .port((int) server.getPort())
//                .serviceName(ctx.getApp().getApplicationName())
//                .build();
//        nodeDiscovery.registerServerInstance(instance);
//    }

    @Override
    protected byte supposedType()
    {
        return ProtocolConstants.TYPE_RPC;
    }

    @Override
    protected ServerMetaData.ServerMetaCmd getServerCmdMetaFrom(Class<? extends ICommand> c)
    {
        ServerMetaData.ServerMetaCmd cmd = new ServerMetaData.ServerMetaCmd();
        RPCServerCmdAnno annotation = (RPCServerCmdAnno) ClassUtil.mustGetAnnotation(c, RPCServerCmdAnno.class);
        cmd.setProtocol(annotation.protocol());
//                cmd.setUri(annotation.uri());
//                cmd.setModule(annotation.module().name());
//                cmd.setMethod(annotation.requestType().getId());
        return cmd;
    }

//    @Override
//    protected Integer getPort(INodeContext context)
//    {
//        CommandLine cmd = context.getCommandLine();
//        String port = cmd.getOptionValue("grpcPort");
//        return Integer.parseInt(port);
//    }
}
