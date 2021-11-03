package com.cell.extension;

import com.cell.base.common.constants.ProtocolConstants;
import com.cell.rpc.client.base.framework.annotation.HttpCmdAnno;
import com.cell.annotations.CellOrder;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.discovery.nacos.discovery.NacosNodeDiscoveryImpl;
import com.cell.dispatcher.IDispatcher;
import com.cell.model.Instance;
import com.cell.protocol.ICommand;
import com.cell.proxy.IHttpProxy;
import com.cell.reactor.ICommandReactor;
import com.cell.reactor.IHttpReactor;
import com.cell.root.Root;
import com.cell.server.DefaultHttpServer;
import com.cell.server.IHttpServer;
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
 * @Date 创建时间：2021-09-08 05:06
 */
@CellOrder(value = OrderConstants.HTTP_NACOS_DISCOVERY_EXTENSION)
public class HttpNacosDiscoveryExtension extends AbstractSpringNodeExtension
{

    public HttpNacosDiscoveryExtension()
    {
    }


    @Override
    public void onInit(INodeContext ctx) throws Exception
    {
        NacosNodeDiscoveryImpl.setupDiscovery();
    }

    @Override
    public void onStart(INodeContext ctx) throws Exception
    {
        this.register(ctx);
    }

    @Override
    public void onReady(INodeContext ctx) throws Exception
    {
    }

    @Override
    public void onClose(INodeContext ctx) throws Exception
    {

    }

    private void register(INodeContext ctx)
    {
        IHttpServer server = (IHttpServer) Root.getInstance().getServer(DefaultHttpServer.class);
        if (server != null)
        {
            this.registerHttp(ctx, server);
        }
    }


    private void registerHttp(INodeContext ctx, IHttpServer server)
    {
        String domain = ctx.getCommandLine().getOptionValue("domain", "demo.com");
        IHttpProxy httpProxy = server.getHttpProxy();
        IDispatcher dispatcher = httpProxy.getDispatcher();

        NacosNodeDiscoveryImpl nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();
        List<? extends ICommandReactor> reactors = dispatcher.getReactors();
        if (reactors == null || reactors.isEmpty()) return;
        Map<Class<?>, IHttpReactor> reactorMap = new HashMap<>();
        for (ICommandReactor value : reactors)
        {
            // only use one
            reactorMap.put(value.getClass(), (IHttpReactor) value);
        }
        ServerMetaData serverMetaData = new ServerMetaData();
        Collection<IHttpReactor> values = reactorMap.values();
        List<ServerMetaData.ServerMetaReactor> reacotrs = values.stream().map(value ->
        {
            ServerMetaData.ServerMetaReactor reactor = new ServerMetaData.ServerMetaReactor();

            List<Class<? extends ICommand>> commands = CommandUtils.getReactorCommands(value).get();
//            List<Class<? extends IHttpCommand>> commands = HttpUtils.getReactorCommands(value).get();
            List<ServerMetaData.ServerMetaCmd> cmds = commands.stream().map(c ->
            {
                ServerMetaData.ServerMetaCmd cmd = new ServerMetaData.ServerMetaCmd();
                HttpCmdAnno annotation = (HttpCmdAnno) ClassUtil.mustGetAnnotation(c, HttpCmdAnno.class);
                cmd.setProtocol(annotation.uri());
                cmd.setModule(annotation.module());
                cmd.setMethod(annotation.requestType().getId());
                return cmd;
            }).collect(Collectors.toList());
            reactor.setCmds(cmds);
            return reactor;
        }).collect(Collectors.toList());
        serverMetaData.setReactors(reacotrs);
        ServerMetaData.ServerExtraInfo extraInfo = new ServerMetaData.ServerExtraInfo();
        extraInfo.setDomain(domain);
        extraInfo.setType(ProtocolConstants.TYPE_HTTP);
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
}
