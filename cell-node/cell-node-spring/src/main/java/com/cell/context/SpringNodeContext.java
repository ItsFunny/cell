package com.cell.context;

import com.cell.app.SpringNodeAPP;
import com.cell.concurrent.base.EventExecutor;
import com.cell.extension.INodeExtension;
import com.cell.postprocessors.extension.SpringExtensionManager;
import lombok.Data;
import org.apache.commons.cli.CommandLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SpringNodeContext implements ISpringNodeContext
{
    private String nodeName;
    private int nodeId;
    private long version;
    private String metadataName;
    private String[] args;
    private SpringNodeAPP NodeApp = new SpringNodeAPP();
    private EventExecutor executor;
    private CommandLine commandLine;
    private SpringExtensionManager manager;
    private Map<String, String> meta = new HashMap<>();

    private List<INodeExtension> extensions;

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
    public String getNodeName()
    {
        return nodeName;
    }

    public void setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
    }

    @Override
    public int getNodeId()
    {
        return nodeId;
    }

    public void setNodeId(int nodeId)
    {
        this.nodeId = nodeId;
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
