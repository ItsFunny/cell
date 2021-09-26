package com.cell.extension;

import com.cell.annotation.HttpCmdAnno;
import com.cell.annotations.CellOrder;
import com.cell.command.IHttpCommand;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.discovery.NacosNodeDiscoveryImpl;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.model.Instance;
import com.cell.reactor.IHttpReactor;
import com.cell.transport.model.ServerMetaData;
import com.cell.util.HttpUtils;
import com.cell.utils.ClassUtil;
import org.apache.commons.cli.Options;

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
public class NacosDiscoveryExtension extends AbstractSpringNodeExtension
{

    public NacosDiscoveryExtension()
    {
    }

    @Override
    public Options getOptions()
    {
        Options options = new Options();
        options.addOption("domain", true, "域名,外网域名");
        return options;
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
        String domain = ctx.getCommandLine().getOptionValue("domain", "demo.com");

        IHttpCommandDispatcher dispatcher = DefaultReactorHolder.getInstance();
        NacosNodeDiscoveryImpl nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();

        Collection<IHttpReactor> values = dispatcher.getReactors();
        if (values == null || values.isEmpty()) return;
        Map<Class<?>, IHttpReactor> reactorMap = new HashMap<>();
        for (IHttpReactor value : values)
        {
            // only use one
            reactorMap.put(value.getClass(), value);
        }
        ServerMetaData serverMetaData = new ServerMetaData();
        values = reactorMap.values();
        List<ServerMetaData.ServerMetaReactor> reacotrs = values.stream().map(value ->
        {
            ServerMetaData.ServerMetaReactor reactor = new ServerMetaData.ServerMetaReactor();

            List<Class<? extends IHttpCommand>> commands = HttpUtils.getReactorCommands(value).get();
            List<ServerMetaData.ServerMetaCmd> cmds = commands.stream().map(c ->
            {
                ServerMetaData.ServerMetaCmd cmd = new ServerMetaData.ServerMetaCmd();
                HttpCmdAnno annotation = (HttpCmdAnno) ClassUtil.mustGetAnnotation(c, HttpCmdAnno.class);
                cmd.setUri(annotation.uri());
                cmd.setModule(annotation.module().name());
                cmd.setMethod(annotation.requestType().getId());
                return cmd;
            }).collect(Collectors.toList());
            reactor.setCmds(cmds);
            return reactor;
        }).collect(Collectors.toList());
        serverMetaData.setReactors(reacotrs);
        ServerMetaData.ServerExtraInfo extraInfo = new ServerMetaData.ServerExtraInfo();
        extraInfo.setDomain(domain);
        serverMetaData.setExtraInfo(extraInfo);


        Map<String, String> metadatas = ServerMetaData.toMetaData(serverMetaData);

        Instance instance = Instance.builder()
                .weight((byte) 1)
                .metaData(metadatas)
                .clusterName(ctx.getCluster())
                .ip(ctx.getIp())
                .healthy(true)
                .enable(true)
                .port((int) dispatcher.getPort())
                .serviceName(ctx.getApp().getApplicationName())
                .build();
        nodeDiscovery.registerServerInstance(instance);
    }
}
