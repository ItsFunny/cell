package com.cell.extension;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.HttpCmdAnno;
import com.cell.command.IHttpCommand;
import com.cell.context.INodeContext;
import com.cell.context.SpringNodeContext;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.model.Instance;
import com.cell.reactor.IHttpReactor;
import com.cell.service.INodeDiscovery;
import com.cell.transport.model.ServerMetaData;
import com.cell.utils.ClassUtil;
import com.cell.utils.CollectionUtils;
import com.cell.utils.JSONUtil;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.ObjectUtils;

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

public class NacosDiscoveryExtension extends AbstractSpringNodeExtension
{

    @AutoPlugin
    private IHttpCommandDispatcher dispatcher;

    private INodeDiscovery nodeDiscovery;

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

        String meta = JSONUtil.toJsonString(serverMetaData);
        Map<String, String> metadatas = new HashMap<>();
        metadatas.put(ServerMetaData.PROPERTY_NAME, meta);
        SpringNodeContext springNodeContext = (SpringNodeContext) ctx;

        Instance.builder()
                .weight((byte) 1)
                .metaData(metadatas)
                .clusterName(this.options.getOption("cluster").getValue())

    }

    @Override
    public void ready(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void close(INodeContext ctx) throws Exception
    {

    }

}
