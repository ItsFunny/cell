package com.cell.extension;

import com.cell.annotations.CellOrder;
import com.cell.annotations.Plugin;
import com.cell.channel.DefaultHttpChannel;
import com.cell.constants.OrderConstants;
import com.cell.constants.ProtocolConstants;
import com.cell.context.INodeContext;
import com.cell.dispatcher.DefaultHttpDispatcher;
import com.cell.dispatcher.IHttpDispatcher;
import com.cell.log.LOG;
import com.cell.manager.ReactorSelectorManager;
import com.cell.manager.WebHandlerManager;
import com.cell.models.Module;
import com.cell.proxy.DefaultHttpProxy;
import com.cell.proxy.IHttpProxy;
import com.cell.reactor.ICommandReactor;
import com.cell.root.Root;
import com.cell.server.DefaultHttpServer;
import com.cell.server.IHttpServer;
import com.cell.service.IDynamicControllerService;
import com.cell.service.impl.DynamicControllerServiceImpl;
import com.cell.utils.StringUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

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
    public Options getOptions()
    {
        Options options = new Options();
        options.addOption("port", true, "端口号");
        return options;
    }

    @Override
    public void onInit(INodeContext ctx) throws Exception
    {
        CommandLine cmd = ctx.getCommandLine();
        this.dynamicControllerService = new DynamicControllerServiceImpl();
        this.httpDispatcher = new DefaultHttpDispatcher(ReactorSelectorManager.getInstance());
        this.httpProxy = new DefaultHttpProxy(this.httpDispatcher);
        this.httpServer = new DefaultHttpServer(this.httpProxy);

        String port = cmd.getOptionValue("port");
        if (!StringUtils.isEmpty(port))
        {
            this.httpServer.setPort(Short.valueOf(port));
        }
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
