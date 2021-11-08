package com.cell.discovery.nacos.http.extension;

import com.cell.annotations.CellOrder;
import com.cell.constants.OrderConstants;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.root.Root;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-26 04:51
 */
@CellOrder(OrderConstants.ROOT_EXTENSION)
public class RootFrameworkExtension extends AbstractSpringNodeExtension
{

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {
        Root.getInstance().start(ctx);
        Root.getInstance().flushAfterStart();
    }

    @Override
    protected void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext ctx) throws Exception
    {

    }
}
