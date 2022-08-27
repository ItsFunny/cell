package com.cell.extension.blockchain.web3j;

import com.cell.base.core.annotations.CellOrder;
import com.cell.extension.blockchain.web3j.config.CellWeb3JConfig;
import com.cell.extension.blockchain.web3j.utils.CellWeb3jUtils;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;

@CellOrder(value = Integer.MIN_VALUE + 1)
public class CellWeb3JExtension extends AbstractSpringNodeExtension
{

    @Override
    protected void onInit(INodeContext iNodeContext) throws Exception
    {
        CellWeb3JConfig.getInstance().seal(iNodeContext);
    }

    @Override
    protected void onStart(INodeContext iNodeContext) throws Exception
    {
        CellWeb3jUtils.seal();
    }

    @Override
    protected void onReady(INodeContext iNodeContext) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext iNodeContext) throws Exception
    {

    }
}
