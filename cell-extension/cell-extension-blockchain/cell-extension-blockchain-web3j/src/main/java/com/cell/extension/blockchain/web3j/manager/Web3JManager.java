package com.cell.extension.blockchain.web3j.manager;

import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.common.context.InitCTX;
import org.web3j.protocol.Web3j;

public class Web3JManager extends AbstractInitOnce
{
    private Web3j web3j;

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.web3j=Web3j.build()
    }

    private static class Web3JManagerFactory
    {
        private static final Web3JManager instance = new Web3JManager();

        private Web3JManagerFactory() {}
    }

    public static Web3JManager getInstance()
    {
        return Web3JManagerFactory.instance;
    }

}
