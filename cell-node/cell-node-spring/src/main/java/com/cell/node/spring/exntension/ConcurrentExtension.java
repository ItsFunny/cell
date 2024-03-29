//package com.cell.node.spring.exntension;
//
//import com.cell.base.core.annotations.CellOrder;
//import com.cell.base.core.concurrent.BaseDefaultEventLoopGroup;
//import com.cell.base.core.concurrent.base.EventLoopGroup;
//import com.cell.node.core.configuration.RootConfiguration;
//import com.cell.node.core.context.INodeContext;
//import com.cell.sdk.configuration.Configuration;
//import lombok.Data;
//import org.apache.commons.cli.Options;
//
//import java.util.Optional;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-11-01 09:16
// */
//@CellOrder(value = Integer.MIN_VALUE)
//public class ConcurrentExtension extends AbstractSpringNodeExtension
//{
//    private static EventLoopGroup eventLoopGroup;
//
//    private static final String modulePath = "env.public.concurrent.json";
//
//    public static EventLoopGroup getEventLoopGroup()
//    {
//        return eventLoopGroup;
//    }
//
//    @Data
//    public static class ConcurrentConfiguration
//    {
//        private int threadCount = 256;
//    }
//
//    @Override
//    public Options getOptions()
//    {
//        Options ret = new Options();
//        ret.addOption("threadCount", false, "线程池数量");
//        return ret;
//    }
//
//    @Override
//    public Object loadConfiguration(INodeContext ctx) throws Exception
//    {
//        ConcurrentConfiguration ret = null;
//        try
//        {
//            ret = Configuration.getDefault().getConfigValue(modulePath).asObject(ConcurrentConfiguration.class);
//        } catch (Exception e)
//        {
//            ret = new ConcurrentConfiguration();
//        }
//        return ret;
//    }
//
//
//    @Override
//    protected void onInit(INodeContext ctx) throws Exception
//    {
//        Optional<Object> configuration = RootConfiguration.getInstance().getExtensionConfiguration(ConcurrentExtension.class);
//        ConcurrentConfiguration cfg = (ConcurrentConfiguration) configuration.get();
//        int threadCount = cfg.getThreadCount();
//        eventLoopGroup = new BaseDefaultEventLoopGroup(threadCount);
//    }
//
//    @Override
//    protected void onStart(INodeContext ctx) throws Exception
//    {
//
//    }
//
//    @Override
//    protected void onReady(INodeContext ctx) throws Exception
//    {
//
//    }
//
//    @Override
//    protected void onClose(INodeContext ctx) throws Exception
//    {
//
//    }
//}
