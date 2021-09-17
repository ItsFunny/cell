//package com.cell.hooks.abs;
//
//import com.cell.handler.IHandler;
//import com.cell.hooks.IChainExecutor;
//import com.cell.protocol.IContext;
//import com.cell.services.IHandlerContext;
//import reactor.core.publisher.Mono;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-09-18 19:53
// */
//public abstract class AbstractReactorHandler implements IHandler
//{
//    @Override
//    public void init(IHandlerContext ctx)
//    {
//// NOOP
//    }
//
//    @Override
//    public void close(IHandlerContext ctx)
//    {
//// NOOP
//    }
//
//
//
//    @Override
//    public void exceptionCaught(IHandlerContext context, Throwable err)
//    {
//// NOOP
//    }
//
//    @Override
//    public Mono<Void> execute(IContext context, IChainExecutor executor)
//    {
//        return this.handle((IHandlerContext) context).then(executor.execute(context));
//    }
//
//    protected abstract Mono<Void> handle(IHandlerContext context);
//}
