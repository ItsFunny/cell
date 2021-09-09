package com.cell.extension;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.CellOrder;
import com.cell.annotations.DependecyExtension;
import com.cell.annotations.HttpCmdAnno;
import com.cell.command.IHttpCommand;
import com.cell.config.ConfigFactory;
import com.cell.config.NacosConfiguration;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.context.InitCTX;
import com.cell.context.SpringNodeContext;
import com.cell.discovery.NacosNodeDiscoveryImpl;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.model.Instance;
import com.cell.reactor.IHttpReactor;
import com.cell.service.INodeDiscovery;
import com.cell.transport.model.ServerMetaData;
import com.cell.utils.ClassUtil;
import com.cell.utils.CollectionUtils;
import com.cell.utils.JSONUtil;
import com.cell.utils.StringUtils;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
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
@CellOrder(value = OrderConstants.HTTP_EXTENSION + 1)
public class NacosDiscoveryExtension extends AbstractSpringNodeExtension
{
    private static final String DEFAULT_CLUSTER = "default";


    private Options options;

    public NacosDiscoveryExtension()
    {
        this.options = new Options();
        options.addOption("cluster", true, "-cluster metadata name");
    }

    @Override
    public Options getOptions()
    {
        return this.options;
    }

    @Override
    public void init(INodeContext ctx) throws Exception
    {
    }

    @Override
    public void start(INodeContext ctx) throws Exception
    {
        this.register(ctx);
    }

    @Override
    public void ready(INodeContext ctx) throws Exception
    {
    }

    @Override
    public void close(INodeContext ctx) throws Exception
    {

    }

    private void register(INodeContext ctx)
    {
        IHttpCommandDispatcher dispatcher = DefaultReactorHolder.getInstance();

        NacosNodeDiscoveryImpl nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();

        Map<String, IHttpReactor> reactors = dispatcher.getReactors();
        if (reactors == null || reactors.isEmpty()) return;
        Collection<IHttpReactor> values = reactors.values();
        ServerMetaData serverMetaData = new ServerMetaData();
        List<ServerMetaData.ServerMetaReactor> reacotrs = values.stream().map(value ->
        {
            ServerMetaData.ServerMetaReactor reactor = new ServerMetaData.ServerMetaReactor();
            List<Class<? extends IHttpCommand>> commands = value.getHttpCommandList();
            List<ServerMetaData.ServerMetaCmd> cmds = commands.stream().map(c ->
            {
                ServerMetaData.ServerMetaCmd cmd = new ServerMetaData.ServerMetaCmd();
                HttpCmdAnno annotation = (HttpCmdAnno) ClassUtil.mustGetAnnotation(c, HttpCmdAnno.class);
                cmd.setUri(annotation.uri());
                return cmd;
            }).collect(Collectors.toList());
            reactor.setCmds(cmds);
            return reactor;
        }).collect(Collectors.toList());
        serverMetaData.setReactors(reacotrs);


        Map<String, String> metadatas = ServerMetaData.toMetaData(serverMetaData);
        String cluster = this.options.getOption("cluster").getValue();
        cluster = StringUtils.isEmpty(cluster) ? DEFAULT_CLUSTER : cluster;
        Instance instance = Instance.builder()
                .weight((byte) 1)
                .metaData(metadatas)
                .clusterName(cluster)
                .ip(ctx.getIp())
                .healthy(true)
                .enable(true)
                .port((int) dispatcher.getPort())
                .serviceName(ctx.getApp().getApplicationName())
                .build();
        nodeDiscovery.registerServerInstance(instance);
    }
}