package com.cell.extension.blockchain.web3j.config;

import com.cell.node.core.context.INodeContext;
import com.cell.sdk.configuration.Configuration;
import lombok.Data;

import java.util.List;

@Data
public class CellWeb3JConfig
{
    private static CellWeb3JConfig instance = new CellWeb3JConfig();

    private static final String module = "cellWeb3j";

    private List<Web3jNode> nodes;

    @Data
    public static class Web3jNode
    {
        private Integer chainId;
        private String name;
        private String address;
        private String privateKey;
    }

    public static CellWeb3JConfig getInstance()
    {
        return instance;
    }

    public void seal(INodeContext context)
    {
        instance = Configuration.getDefault().getAndMonitorConfig(module, CellWeb3JConfig.class, (v) ->
        {
            instance = v;
        });
    }
}
