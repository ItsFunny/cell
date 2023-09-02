package com.cell.extension.blockchain.web3j.config;

import com.cell.node.core.context.INodeContext;
import com.cell.sdk.configuration.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
public class CellWeb3JConfig {
    private static CellWeb3JConfig instance = new CellWeb3JConfig();

    private static final String module = "cellWeb3j";

    private List<Web3jNode> nodes = Arrays.asList(
            new Web3jNode(56, "0", "https://bsc-dataseed.binance.org/"),
            new Web3jNode(56, "1", "https://bsc.nodereal.io"),
            new Web3jNode(56, "2", "https://bsc-dataseed1.defibit.io/"),
            new Web3jNode(56, "3", "https://bsc-dataseed1.ninicoin.io/"),
            new Web3jNode(56, "4", "https://bsc-dataseed2.defibit.io/"),
            new Web3jNode(56, "5", "https://bsc-dataseed3.defibit.io/"),
            new Web3jNode(56, "6", "https://bsc-dataseed4.defibit.io/"),
            new Web3jNode(56, "7", "https://bsc-dataseed3.ninicoin.io/"),
            new Web3jNode(56, "8", "https://bsc-dataseed4.ninicoin.io/"),
            new Web3jNode(56, "9", "https://bsc-dataseed1.binance.org/"),
            new Web3jNode(56, "10", "https://bsc-dataseed2.binance.org/"),
            new Web3jNode(56, "11", "https://bsc-dataseed3.binance.org/"),
            new Web3jNode(56, "12", "https://bsc-dataseed4.binance.org/"),
            new Web3jNode(56, "13", "https://bsc-dataseed2.ninicoin.io/")
            );

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Web3jNode {
        private Integer chainId;
        private String name;
        private String address;
        private String privateKey;

        public Web3jNode(Integer chainId, String name, String address) {
            this.chainId = chainId;
            this.name = name;
            this.address = address;
        }

    }

    public static CellWeb3JConfig getInstance() {
        return instance;
    }

    public void seal(INodeContext context) {
        instance = Configuration.getDefault().getAndMonitorConfig(module, CellWeb3JConfig.class, (v) ->
        {
            instance = v;
        });
    }
}
