//package com.cell.application;
//
//import com.cell.constants.ContextConstants;
//import com.cell.protocol.ContextResponseWrapper;
//import com.cell.protocol.IBuzzContext;
//import com.cell.protocol.ICommand;
//import com.cell.reactor.ICommandReactor;
//import lombok.Data;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-09-02 21:19
// */
//@Data
//public class BuzzContextBO
//{
//    private IBuzzContext context;
//    private Object bo;
//    private ICommand cmd;
//
//    public ICommandReactor getReactor(){
//        return this.context.getReactor();
//    }
//    public BuzzContextBO(IBuzzContext context, Object bo, ICommand cmd)
//    {
//        this.context = context;
//        this.bo = bo;
//        this.cmd = cmd;
//    }
//
//    public ContextResponseWrapper.ContextResponseWrapperBuilder builder()
//    {
//        return ContextResponseWrapper.builder()
//                .cmd(this.cmd);
//    }
//
//    public void success(Object ret)
//    {
//        this.context.response(this.builder()
//                .status(ContextConstants.SUCCESS)
//                .ret(ret)
//                .build());
//    }
//
//    public void fail(Object ret)
//    {
//        this.context.response(this.builder()
//                .cmd(this.cmd)
//                .status(ContextConstants.FAIL)
//                .ret(ret)
//                .build());
//    }
//
//    public void fail(Object ret, Exception e)
//    {
//        this.context.response(
//                this.builder()
//                        .cmd(this.cmd)
//                        .status(ContextConstants.FAIL)
//                        .exception(e)
//                        .ret(ret)
//                        .build());
//    }
//}
