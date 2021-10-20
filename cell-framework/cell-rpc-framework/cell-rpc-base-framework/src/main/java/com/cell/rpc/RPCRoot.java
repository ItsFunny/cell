package com.cell.rpc;

import com.cell.cmd.IRPCInvoker;
import com.cell.rpc.registry.IRPCRegistry;
import com.cell.rpc.registry.IRegistryFactory;
import com.cell.rpc.registry.RegisterParam;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-20 22:07
 */
@Data
public class RPCRoot
{
    private IRegistryFactory factory;
    private Map<String, IRPCRegistry> registries = new HashMap<>();
    private static final String defaultRegistryName = "default";
    private static final RPCRoot instance = new RPCRoot();

    public static RPCRoot getInstance()
    {
        return instance;
    }


    private RPCRoot()
    {

    }

    public void register(IRPCInvoker invoker)
    {

    }

    protected synchronized void register(String regName, IRPCInvoker invoker, RegisterParam param)
    {
        IRPCRegistry irpcRegistry = this.registries.get(regName);
        if (null == irpcRegistry)
        {
            irpcRegistry = this.factory.create(regName);
            registries.put(regName, irpcRegistry);
        }
    }
}
