package com.cell.extension.blockchain.web3j.config;

import com.cell.node.core.context.INodeContext;
import com.cell.sdk.configuration.Configuration;
import lombok.Data;

import java.util.List;

@Data
public class Web3JConfig
{
    private static Web3JConfig instance = new Web3JConfig();

    private static final String module = "web3j";

    private List<Web3jNode> nodes;

    @Data
    public static class Web3jNode
    {
        private Integer chainId;
        private String address;
        private String privateKey;
    }

    public static Web3JConfig getInstance()
    {
        return instance;
    }

    public void seal(INodeContext context)
    {
        instance = Configuration.getDefault().getAndMonitorConfig(module, Web3JConfig.class, (v) ->
        {
            instance = v;
        });
    }
}
