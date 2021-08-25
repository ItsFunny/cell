package com.cell;

import java.util.List;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-09 22:13
 */
public interface  IContext
{
    String getNodeName();
    EventExecutor getExecutor();
    int getNodeId();
    long getVersion();
    String getMetadataName();
    String [] getArgs();
    INodeApp getApp();
    CommandLine getCommandLine();
    List<INodeExtension> getExtensions();
    void setMetadata(Map<String, String> meta);
    Map<String, String> getMetadata();
}
