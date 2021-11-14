package com.cell.node.core.configuration;

import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.common.exceptions.ConfigException;
import com.cell.base.common.exceptions.ValidateException;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.common.validators.IValidator;
import com.cell.sdk.configuration.Configuration;
import lombok.Data;

import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description 存储了所有server的信息, 包含了nodeId, 公网, 内网等信息
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-12 18:25
 */
@Data
public class NodeConfiguration implements IValidator
{
    private static NodeConfiguration instance = null;

    private static final String serverNodeModule = "public.server.json";


    private List<Node> nodes;

    private DefaultConfiguraiton defaultProperty;

    private static final Map<Byte, NodeInstance> defaultTypeInstance = new HashMap<>();
    private static final DefaultConfiguraiton DEFAULT_CONFIGURAITON = new DefaultConfiguraiton();

    static
    {
        DEFAULT_CONFIGURAITON.ports = new HashMap<>();
        DEFAULT_CONFIGURAITON.ports.put(ProtocolConstants.TYPE_HTTP, (short) 8000);
        DEFAULT_CONFIGURAITON.ports.put(ProtocolConstants.TYPE_RPC, (short) 7000);
        DEFAULT_CONFIGURAITON.ports.put(ProtocolConstants.TYPE_HTTP_GATE, (short) 9999);
        DEFAULT_CONFIGURAITON.publicAddress = ProtocolConstants.DEFAULT_PUBLIC_ADDRESS;
        NodeInstance httpInstance = new NodeInstance();
        httpInstance.setPublicAddress(ProtocolConstants.LOCAL_HOST);
        httpInstance.setPublicPort((short) 8080);
        httpInstance.setType(ProtocolConstants.TYPE_HTTP);
        httpInstance.setVisualPort((short) 8080);

        NodeInstance rpcServerInstance = new NodeInstance();
        rpcServerInstance.setPublicAddress(ProtocolConstants.LOCAL_HOST);
        rpcServerInstance.setPublicPort((short) 7000);
        rpcServerInstance.setType(ProtocolConstants.TYPE_RPC);
        rpcServerInstance.setVisualPort((short) 7000);

        NodeInstance gateInstance = new NodeInstance();
        gateInstance.setPublicAddress(ProtocolConstants.LOCAL_HOST);
        gateInstance.setPublicPort((short) 9999);
        gateInstance.setType(ProtocolConstants.TYPE_HTTP_GATE);
        gateInstance.setVisualPort((short) 9999);


        defaultTypeInstance.put(ProtocolConstants.TYPE_HTTP, httpInstance);
        defaultTypeInstance.put(ProtocolConstants.TYPE_RPC, rpcServerInstance);
        defaultTypeInstance.put(ProtocolConstants.TYPE_HTTP_GATE, gateInstance);
    }

    @Data
    public static class DefaultConfiguraiton
    {
        private String publicAddress;
        private Map<Byte, Short> ports;
    }

    @Override
    public void valid() throws ValidateException
    {
        if (this.defaultProperty == null)
        {
            this.defaultProperty = DEFAULT_CONFIGURAITON;
        } else
        {
            if (StringUtils.isEmpty(this.defaultProperty.publicAddress))
            {
                this.defaultProperty.publicAddress = ProtocolConstants.DEFAULT_PUBLIC_ADDRESS;
            }
            if (this.defaultProperty.ports == null || this.defaultProperty.ports.size() == 0)
            {
                this.defaultProperty.ports = DEFAULT_CONFIGURAITON.ports;
            } else
            {
                Map<Byte, Short> ports = this.defaultProperty.getPorts();
                if (!ports.containsKey(ProtocolConstants.TYPE_HTTP_GATE))
                {
                    ports.put(ProtocolConstants.TYPE_HTTP_GATE, (short) 9999);
                }
                if (!ports.containsKey(ProtocolConstants.TYPE_HTTP))
                {
                    ports.put(ProtocolConstants.TYPE_HTTP, (short) 8000);
                }
                if (!ports.containsKey(ProtocolConstants.TYPE_RPC))
                {
                    ports.put(ProtocolConstants.TYPE_RPC, (short) 7000);
                }
            }
        }
        nodes.stream().forEach(n ->
        {
            if (CollectionUtils.isEmpty(n.instances)) return;
            n.instances.stream().forEach(inst ->
            {
                if ((inst.type & ProtocolConstants.VALID) <= 0) throw new ConfigException("not valid config ");

                if ((inst.type & ProtocolConstants.TYPE_HTTP) >= ProtocolConstants.TYPE_HTTP)
                {
                    Short port = this.defaultProperty.ports.get(ProtocolConstants.TYPE_HTTP);
                    if (inst.visualPort == 0 || inst.publicPort == 0)
                    {
                        swapPort(inst, port);
                    }
                } else if ((inst.type & ProtocolConstants.TYPE_RPC) >= ProtocolConstants.TYPE_RPC)
                {
                    Short port = this.defaultProperty.ports.get(ProtocolConstants.TYPE_RPC);
                    swapPort(inst, port);
                } else if ((inst.type & ProtocolConstants.TYPE_HTTP_GATE) >= ProtocolConstants.TYPE_HTTP_GATE)
                {
                    Short port = this.defaultProperty.ports.get(ProtocolConstants.TYPE_HTTP_GATE);
                    swapPort(inst, port);
                }
                if (StringUtils.isEmpty(inst.publicAddress))
                {
                    inst.publicAddress = this.defaultProperty.publicAddress;
                }
            });
        });
    }

    private void swapPort(NodeInstance inst, Short port)
    {
        if (inst.visualPort != 0) inst.visualPort = port;
        if (inst.publicPort != 0) inst.publicPort = port;
//        if (inst.visualPort == 0 && inst.publicPort == 0)
//        {
//            inst.visualPort = port;
//            inst.publicPort = port;
//        } else
//        {
//            if (inst.visualPort == 0) inst.visualPort = inst.publicPort;
//            if (inst.publicPort == 0) inst.publicPort = inst.visualPort;
//        }
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
        private short publicPort = 0;
        private short visualPort = 0;
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
        return this.newDefaultNode(nodeId);
    }

    public static Node newDefaultNode(String nodeId)
    {
        Node ret = new Node();
        ret.setNodeId(nodeId);
        ret.setInstances(new ArrayList<>());
        return ret;
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
