package com.cell.node.spring.context;

import com.cell.base.common.constants.CommandLineConstants;
import com.cell.base.core.concurrent.base.EventExecutor;
import com.cell.base.core.concurrent.base.EventLoop;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.node.core.configuration.NodeConfiguration;
import com.cell.node.core.extension.INodeExtension;
import com.cell.node.spring.app.SpringNodeAPP;
import com.cell.node.spring.postprocessors.extension.SpringExtensionManager;
import lombok.Data;
import org.apache.commons.cli.CommandLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SpringNodeContext implements ISpringNodeContext
{
    private long version;
    private String metadataName;
    private String[] args;
    private SpringNodeAPP NodeApp = new SpringNodeAPP();
    private EventExecutor executor;
    private EventLoopGroup eventLoopGroup;
    private CommandLine commandLine;
    private SpringExtensionManager manager;
    private String cluster = CommandLineConstants.DEFAULT_CLSUTER_VALUE;
    private Map<String, String> meta = new HashMap<>();

    private List<INodeExtension> extensions;

    private NodeConfiguration.Node node;

    private String ip;

    public SpringNodeContext(SpringExtensionManager manager)
    {
        this.manager = manager;
    }

    @Override
    public Map<String, String> getMetadata()
    {
        return this.meta;
    }

    @Override
    public NodeConfiguration.NodeInstance getInstanceByType(byte type)
    {
        return this.node.mustGetInstanceByType(type);
    }

//	public void addExclusive(String node) {
//		manager.addExcludeExtension(node);
//	}

    @Override
    public List<INodeExtension> getExtensions()
    {
        return extensions;
    }

    public void setExtensions(List<INodeExtension> extensions)
    {
        this.extensions = extensions;
    }

    @Override
    public CommandLine getCommandLine()
    {
        return commandLine;
    }

    public void setCommandLine(CommandLine commandLine)
    {
        this.commandLine = commandLine;
    }

    @Override
    public EventExecutor getExecutor()
    {
        return executor;
    }

    public void setExecutor(EventExecutor executor)
    {
        this.executor = executor;
    }

    @Override
    public String getNodeId()
    {
        return this.node.getNodeId();
    }


    @Override
    public long getVersion()
    {
        return version;
    }

    public void setVersion(long version)
    {
        this.version = version;
    }

    @Override
    public String getMetadataName()
    {
        return metadataName;
    }

    public void setMetadataName(String metadataName)
    {
        this.metadataName = metadataName;
    }

    @Override
    public String[] getArgs()
    {
        return args;
    }

    public void setArgs(String[] args)
    {
        this.args = args;
    }

    @Override
    public SpringNodeAPP getApp()
    {
        return NodeApp;
    }

    @Override
    public void setMetadata(Map<String, String> meta)
    {
        this.meta = meta;
    }

}
