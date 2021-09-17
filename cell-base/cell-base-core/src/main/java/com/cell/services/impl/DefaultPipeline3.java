//package com.cell.services.impl;
//
//import com.cell.annotations.Manager;
//import com.cell.center.AbstractReflectManager;
//import com.cell.constants.HttpConstant;
//import com.cell.constants.ManagerConstants;
//import com.cell.handler.IChainHandler;
//import com.cell.handler.IHandler;
//import com.cell.hooks.IChainExecutor;
//import com.cell.manager.IReflectManager;
//import com.cell.services.Pipeline;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-09-19 05:06
// */
//@Manager(name = ManagerConstants.MANAGER_WEB_HANDLER)
//public class DefaultPipeline extends AbstractReflectManager implements Pipeline<IHandler, IChainHandler>
//{
//    private static final DefaultPipeline instance = new DefaultPipeline();
//
//    public static DefaultPipeline getInstance()
//    {
//        return instance;
//    }
//
//    private List<IHandler> reactorExecutors = new ArrayList<>();
//
//    private DefaultPipeline() {}
//
//    public IChainHandler chainExecutor()
//    {
//        return new DefaultHandlerMutableChainExecutor(this.reactorExecutors);
//    }
//
//    @Override
//    public final Pipeline addFirst(String name, IHandler handler)
//    {
//        synchronized (this)
//        {
//            this.reactorExecutors.add(handler);
//        }
//        return this;
//    }
//
//    @Override
//    public Pipeline addLast(IHandler... handlers)
//    {
//        return null;
//    }
//
//    @Override
//    public Pipeline remove(IHandler handler)
//    {
//        return null;
//    }
//
//    @Override
//    protected void onInvokeInterestNodes(Collection<Object> nodes)
//    {
//        for (Object node : nodes)
//        {
//            if (!(node instanceof IHandler))
//            {
//                continue;
//            }
//            this.addFirst(node.getClass().getName(), (IHandler) node);
//        }
//    }
//
//    @Override
//    public IReflectManager createOrDefault()
//    {
//        return instance;
//    }
//}
