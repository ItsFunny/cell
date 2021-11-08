package com.cell.base.framework.application;//package com.cell.application;
//
//
//import com.cell.protocol.AbstractCommand;
//import com.cell.base.core.protocol.IBuzzContext;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-09-02 19:09
// */
//public abstract class AbsDeltaHttpCommand extends AbstractCommand
//{
//    private IBuzzExecutor buzzExecutor;
//
//    public AbsDeltaHttpCommand()
//    {
//        super();
//        this.buzzExecutor = this.setUpBuzzExecutor();
//    }
//
//    protected abstract IBuzzExecutor setUpBuzzExecutor();
//
//
//    @Override
//    protected void onExecute(IBuzzContext ctx, Object bo)
//    {
//        BuzzContextBO reqBO = new BuzzContextBO(ctx, bo, this);
//        this.buzzExecutor.execute(reqBO);
//    }
//}
