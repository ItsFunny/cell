package com.cell.extension.blockchain.web3j.config;

import lombok.Data;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

@Data
public class Web3JWrapper
{
    private Web3j web3j;
    private Credentials credentials;

    public Web3JWrapper(Web3j web3j, Credentials credentials)
    {
        this.web3j = web3j;
        this.credentials = credentials;
    }
}
