package com.cell.context;

import com.cell.app.IApp;
import com.cell.concurrent.base.EventExecutor;

import java.util.List;
import java.util.Map;

import com.cell.discovery.nacos.http.extension.INodeExtension;
import org.apache.commons.cli.CommandLine;

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
