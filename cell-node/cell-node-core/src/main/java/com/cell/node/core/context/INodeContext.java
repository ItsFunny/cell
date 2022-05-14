package com.cell.node.core.context;

import com.cell.base.core.concurrent.base.EventExecutor;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.node.core.app.IApp;
import com.cell.node.core.configuration.NodeConfiguration;
import com.cell.node.core.extension.INodeExtension;
import org.apache.commons.cli.CommandLine;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-10 21:09
 */
public interface INodeContext
{
    String getCluster();

    EventExecutor getExecutor();
    EventLoopGroup getEventLoopGroup();

    String getNodeId();

    long getVersion();

    String getMetadataName();

    String[] getArgs();

    IApp getApp();

    CommandLine getCommandLine();

    List<INodeExtension> getExtensions();

    void setMetadata(Map<String, String> meta);

    Map<String, String> getMetadata();

    NodeConfiguration.NodeInstance getInstanceByType(byte type);

    String getIp();
}
