package com.cell.extension.blockchain.web3j.config;

import com.cell.base.core.services.IFrom;
import lombok.Data;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Web3JWrapper implements IFrom<CellWeb3JConfig.Web3jNode>
{
    private Web3j web3j;
    private Integer chainId;
    private String address;
    private String name;
    private Credentials credentials;

    private AtomicInteger successCount=new AtomicInteger(0);

    public Web3JWrapper(Web3j web3j, Integer chainId, Credentials credentials)
    {
        this.web3j = web3j;
        this.chainId = chainId;
        this.credentials = credentials;
    }

    @Override
    public void from(CellWeb3JConfig.Web3jNode web3jNode)
    {
        this.chainId = web3jNode.getChainId();
        this.address = web3jNode.getAddress();
        this.name = web3jNode.getName();
    }

    @Override
    public String toString()
    {
        return "Web3JWrapper{" +
                "chainId=" + chainId +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public int inc()
    {
        return this.successCount.incrementAndGet();
    }
}
