package com.cell.http.extension;

import com.cell.base.common.constants.OrderConstants;
import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.common.models.Module;
import com.cell.base.core.annotations.CellOrder;
import com.cell.base.core.annotations.Plugin;
import com.cell.base.core.reactor.ICommandReactor;
import com.cell.base.framework.root.Root;
import com.cell.http.framework.channel.DefaultHttpChannel;
import com.cell.http.framework.dispatcher.DefaultHttpDispatcher;
import com.cell.http.framework.dispatcher.IHttpDispatcher;
import com.cell.http.framework.manager.ReactorSelectorManager;
import com.cell.http.framework.manager.WebHandlerManager;
import com.cell.http.framework.proxy.DefaultHttpProxy;
import com.cell.http.framework.proxy.IHttpProxy;
import com.cell.http.framework.server.DefaultHttpServer;
import com.cell.http.framework.server.IHttpServer;
import com.cell.http.framework.service.IDynamicControllerService;
import com.cell.http.framework.service.impl.DynamicControllerServiceImpl;
import com.cell.node.core.configuration.NodeConfiguration;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.sdk.log.LOG;
import org.apache.commons.cli.CommandLine;

import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:48
 */
@CellOrder(value = OrderConstants.HTTP_EXTENSION)
public class HttpExtension extends AbstractSpringNodeExtension
{
    private IDynamicControllerService dynamicControllerService;
    private IHttpServer httpServer;
    private IHttpProxy httpProxy;
    private IHttpDispatcher httpDispatcher;

    @Plugin
    public IDynamicControllerService dynamicControllerService()
    {
        return this.dynamicControllerService;
    }

    @Plugin
    public IHttpProxy httpProxy()
    {
        return this.httpProxy;
    }

    @Plugin
    public IHttpDispatcher dispatcher()
    {
        return this.httpDispatcher;
    }

    @Plugin
    public IHttpServer httpServer()
    {
        return this.httpServer;
    }


    public HttpExtension()
    {

    }

    @Override
    public void onInit(INodeContext ctx) throws Exception
    {
        this.dynamicControllerService = new DynamicControllerServiceImpl();
        this.httpDispatcher = new DefaultHttpDispatcher(ReactorSelectorManager.getInstance());
        this.httpProxy = new DefaultHttpProxy(this.httpDispatcher);
        this.httpServer = new DefaultHttpServer(this.httpProxy);
        NodeConfiguration.NodeInstance nodeInstance = ctx.getInstanceByType(this.httpServer.type());
        this.httpServer.setPort(nodeInstance.getVisualPort());
    }

    @Override
    public void onStart(INodeContext ctx) throws Exception
    {
        Set<ICommandReactor> reactors = Root.getInstance().getReactor(ProtocolConstants.REACTOR_TYPE_HTTP);
        for (ICommandReactor reactor : reactors)
        {
            LOG.info(Module.HTTP_FRAMEWORK, "添加http Reactor,info:{}", reactor);
            this.httpDispatcher.addReactor(reactor);
        }
        DefaultHttpChannel instance = DefaultHttpChannel.getInstance();
        instance.setPipeline(WebHandlerManager.getInstance().getPipeline());
        this.httpDispatcher.setChannel(instance);
        this.httpDispatcher.initOnce(null);
        this.dynamicControllerService.batchRegisterReactor(reactors);
    }

    @Override
    public void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void onClose(INodeContext ctx) throws Exception
    {

    }
}
