package com.cell.node.core.config;


import com.cell.node.core.context.INodeContext;
import com.cell.sdk.configuration.Configuration;
import lombok.Data;

@Data
public class NodeConfig
{
    private static final String module = "node";
    private static NodeConfig instance=new NodeConfig();

    private boolean traceEnable=true;

    public static NodeConfig getInstance(){
        return instance;
    }

    public void seal(INodeContext context)
    {
        try
        {
            instance = Configuration.getDefault().getAndMonitorConfig(module, NodeConfig.class, (v) ->
            {
                instance = v;
            });
        } catch (Exception e)
        {
            instance = new NodeConfig();
        }
    }

    private NodeConfig defaultConfig()
    {
        NodeConfig ret = new NodeConfig();
        ret.traceEnable = true;
        return ret;
    }
}
