package com.cell.extension;

import com.cell.annotations.Plugin;
import com.cell.context.INodeContext;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.hook.CmdHookManager;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.postprocessor.ReactorCache;
import com.cell.reactor.IDynamicHttpReactor;
import com.cell.reactor.IHttpReactor;
import com.cell.service.IDynamicControllerService;
import com.cell.service.impl.DefaultHttpCommandDispatcher;
import com.cell.service.impl.DynamicControllerServiceImpl;

import java.util.Collection;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:48
 */
public class HttpExtension extends AbstractSpringNodeExtension
{
    private IDynamicControllerService dynamicControllerService;

    private IHttpCommandDispatcher dispatcher;

    @Plugin
    public IDynamicControllerService dynamicControllerService()
    {
        return this.dynamicControllerService;
    }

    @Plugin
    public IHttpCommandDispatcher dispatcher()
    {
        return this.dispatcher;
    }

    @Override
    public void init(INodeContext ctx) throws Exception
    {
        this.dynamicControllerService = new DynamicControllerServiceImpl();
        this.dispatcher = new DefaultHttpCommandDispatcher();
    }

    @Override
    public void start(INodeContext ctx) throws Exception
    {
        ((DefaultHttpCommandDispatcher) this.dispatcher).setTracker(CmdHookManager.getInstance().getHook());
        ((DefaultHttpCommandDispatcher) this.dispatcher).initOnce(null);
        Collection<IHttpReactor> reactors = DefaultReactorHolder.getReactors();
        for (IHttpReactor reactor : reactors)
        {
            LOG.info(Module.HTTP_FRAMEWORK, "添加http Reactor,info:{}", reactor);
            this.dispatcher.addReactor(reactor);
        }

        //        Collection<IDynamicHttpReactor> reactors = ReactorCache.getReactors();
//        for (IDynamicHttpReactor reactor : reactors)
//        {
//            this.dynamicControllerService.reigsterReactor(reactor);
//        }
    }

    @Override
    public void ready(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void close(INodeContext ctx) throws Exception
    {

    }
}