package com.cell.discovery.nacos.http.extension;

import com.cell.base.common.constants.OrderConstants;
import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.core.annotations.CellOrder;
import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.utils.ClassUtil;
import com.cell.base.framework.server.IServer;
import com.cell.bee.transport.model.ServerMetaData;
import com.cell.discovery.nacos.base.extension.AbstractNacosDiscoveryExtension;
import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.server.DefaultHttpServer;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 05:06
 */
@CellOrder(value = OrderConstants.HTTP_NACOS_DISCOVERY_EXTENSION)
public class HttpNacosDiscoveryExtension extends AbstractNacosDiscoveryExtension
{

    public HttpNacosDiscoveryExtension()
    {
    }

    @Override
    protected Class<? extends IServer> serverClz()
    {
        return DefaultHttpServer.class;
    }


    @Override
    protected byte supposedType()
    {
        return ProtocolConstants.TYPE_HTTP;
    }

    @Override
    protected ServerMetaData.ServerMetaCmd getServerCmdMetaFrom(Class<? extends ICommand> c)
    {
        ServerMetaData.ServerMetaCmd cmd = new ServerMetaData.ServerMetaCmd();
        HttpCmdAnno annotation = (HttpCmdAnno) ClassUtil.mustGetAnnotation(c, HttpCmdAnno.class);
        cmd.setProtocol(annotation.uri());
        cmd.setModule(annotation.module());
        cmd.setMethod(annotation.requestType().getId());
        return cmd;
    }


//    private void register(INodeContext ctx)
//    {
//        IHttpServer server = (IHttpServer) Root.getInstance().getServer(DefaultHttpServer.class);
//        if (server != null)
//        {
//            this.registerHttp(ctx, server);
//        }
//    }


//    private void registerHttp(INodeContext ctx, IHttpServer server)
//    {
//        String domain = ctx.getCommandLine().getOptionValue("domain", "demo.com");
//        IHttpProxy httpProxy = server.getHttpProxy();
//        IDispatcher dispatcher = httpProxy.getDispatcher();
//
//        NacosNodeDiscoveryImpl nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();
//        List<? extends ICommandReactor> reactors = dispatcher.getReactors();
//        if (reactors == null || reactors.isEmpty()) return;
//        Map<Class<?>, IHttpReactor> reactorMap = new HashMap<>();
//        for (ICommandReactor value : reactors)
//        {
//            // only use one
//            reactorMap.put(value.getClass(), (IHttpReactor) value);
//        }
//        ServerMetaData serverMetaData = new ServerMetaData();
//        Collection<IHttpReactor> values = reactorMap.values();
//        List<ServerMetaData.ServerMetaReactor> reacotrs = values.stream().map(value ->
//        {
//            ServerMetaData.ServerMetaReactor reactor = new ServerMetaData.ServerMetaReactor();
//
//            List<Class<? extends ICommand>> commands = CommandUtils.getReactorCommands(value).get();
////            List<Class<? extends IHttpCommand>> commands = HttpUtils.getReactorCommands(value).get();
//            List<ServerMetaData.ServerMetaCmd> cmds = commands.stream().map(c ->
//            {
//                ServerMetaData.ServerMetaCmd cmd = new ServerMetaData.ServerMetaCmd();
//                HttpCmdAnno annotation = (HttpCmdAnno) ClassUtil.mustGetAnnotation(c, HttpCmdAnno.class);
//                cmd.setProtocol(annotation.uri());
//                cmd.setModule(annotation.module());
//                cmd.setMethod(annotation.requestType().getId());
//                return cmd;
//            }).collect(Collectors.toList());
//            reactor.setCmds(cmds);
//            return reactor;
//        }).collect(Collectors.toList());
//        serverMetaData.setReactors(reacotrs);
//        ServerMetaData.ServerExtraInfo extraInfo = new ServerMetaData.ServerExtraInfo();
//        extraInfo.setDomain(domain);
//        extraInfo.setType(ProtocolConstants.TYPE_HTTP);
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
}
