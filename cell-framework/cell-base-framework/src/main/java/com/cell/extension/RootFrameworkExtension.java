package com.cell.extension;

import com.cell.annotations.CellOrder;
import com.cell.context.INodeContext;
import com.cell.root.Root;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-26 04:51
 */
@CellOrder(Integer.MAX_VALUE)
public class RootFrameworkExtension extends AbstractSpringNodeExtension
{

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {
        Root.getInstance().start();
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
