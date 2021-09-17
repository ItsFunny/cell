//package com.cell.handler;
//
//import com.cell.hooks.IContextMonoHook;
//import com.cell.hooks.IHookChain;
//import com.cell.protocol.IContext;
//import com.cell.services.IHandlerContext;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-09-19 07:27
// */
//public class DefaultMutableHandlerChain implements IReactHandlerChain
//{
//    private List<? extends IReactorHandler> handlers;
//    private int index;
//
//
//    public DefaultMutableHandlerChain(List<? extends IReactorHandler> handlers)
//    {
//        this.handlers = handlers;
//        this.index = 0;
//    }
//
//    public DefaultMutableHandlerChain(DefaultMutableHandlerChain parent, int index)
//    {
//        this.handlers = parent.handlers;
//        this.index = index;
//    }
//
//    @Override
//    public Mono<Void> handle(IHandlerContext context)
//    {
//        return Mono.defer(() ->
//        {
//            if (this.index < this.handlers.size())
//            {
//                IReactorHandler h = null;
//                DefaultMutableHandlerChain hh = new DefaultMutableHandlerChain(this, this.index + 1);
//                return h.handler(context, hh);
//            } else
//            {
//                return Mono.empty();
//            }
//        });
//    }
//}
