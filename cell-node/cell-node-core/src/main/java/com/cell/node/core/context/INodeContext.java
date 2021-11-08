package com.cell.node.core.context;

import com.cell.concurrent.base.EventExecutor;
import com.cell.node.core.app.IApp;
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
    String getNodeName();

    String getCluster();

    EventExecutor getExecutor();

    int getNodeId();

    long getVersion();

    String getMetadataName();

    String[] getArgs();

    IApp getApp();

    CommandLine getCommandLine();

    List<INodeExtension> getExtensions();

    void setMetadata(Map<String, String> meta);

    Map<String, String> getMetadata();

    String getIp();
}
