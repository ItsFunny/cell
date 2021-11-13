package com.cell.node.core.configuration;

import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.common.exceptions.ConfigException;
import com.cell.sdk.configuration.Configuration;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Charlie
 * @When
 * @Description 存储了所有server的信息, 包含了nodeId, 公网, 内网等信息
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-12 18:25
 */
@Data
public class NodeConfiguration
{
    private static NodeConfiguration instance = null;

    private static final String serverNodeModule = "public.server.json";

    private List<Node> nodes;

    private static final Map<Byte, NodeInstance> defaultTypeInstance = new HashMap<>();

    static
    {
        NodeInstance httpInstance = new NodeInstance();
        httpInstance.setPublicAddress("localhost");
        httpInstance.setPublicPort(8080);
        httpInstance.setType(ProtocolConstants.TYPE_HTTP);
        httpInstance.setVisualPort((short) 8080);

        NodeInstance rpcServerInstance = new NodeInstance();
        rpcServerInstance.setPublicAddress("localhost");
        rpcServerInstance.setPublicPort(7000);
        rpcServerInstance.setType(ProtocolConstants.TYPE_RPC);
        rpcServerInstance.setVisualPort((short) 7000);


        defaultTypeInstance.put(ProtocolConstants.TYPE_HTTP, httpInstance);
        defaultTypeInstance.put(ProtocolConstants.TYPE_RPC, rpcServerInstance);
    }

    @Data
    public static class Node
    {
        private String nodeId;
        private List<NodeInstance> instances;

        public NodeInstance mustGetInstanceByType(byte type)
        {
            Optional<NodeInstance> first = this.instances.stream().filter(n -> n.getType() == type).findFirst();
            if (!first.isPresent())
            {
                NodeInstance nodeInstance = defaultTypeInstance.get(type);
                if (nodeInstance == null) throw new ConfigException("type not exist:" + type);
                return nodeInstance;
            } else
            {
                return first.get();
            }
        }
    }

    @Data
    public static class NodeInstance
    {
        private String publicAddress;
        private Integer publicPort;
        private short visualPort;
        private byte type = ProtocolConstants.TYPE_HTTP;
    }

    public Node mustGetNode(String nodeId)
    {
        for (Node node : nodes)
        {
            if (node.getNodeId().equalsIgnoreCase(nodeId))
            {
                return node;
            }
        }
        throw new ConfigException("node not exist:" + nodeId);
    }

    public static void setup()
    {
        if (instance != null)
        {
            return;
        }
        synchronized (NodeConfiguration.class)
        {
            if (instance != null)
            {
                return;
            }
            try
            {
                instance = Configuration.getDefault().getConfigValue(serverNodeModule).asObject(NodeConfiguration.class);
            } catch (Exception e)
            {
                throw new ConfigException(e);
            }
        }
    }

    public static NodeConfiguration getInstance()
    {
        return instance;
    }
}
