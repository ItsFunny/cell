package com.cell.base.framework.extension;

import com.cell.base.common.constants.OrderConstants;
import com.cell.base.core.annotations.CellOrder;
import com.cell.base.framework.root.Root;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;

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
