package com.cell.extension;

import com.cell.annotations.CellOrder;
import com.cell.annotations.Plugin;
import com.cell.channel.DefaultHttpChannel;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.log.LOG;
import com.cell.manager.ReactorSelectorManager;
import com.cell.manager.WebHandlerManager;
import com.cell.models.Module;
import com.cell.reactor.IHttpReactor;
import com.cell.service.IDynamicControllerService;
import com.cell.service.impl.DefaultHttpCommandDispatcher;
import com.cell.service.impl.DynamicControllerServiceImpl;
import com.cell.utils.StringUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.util.Collection;

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
        this.dispatcher = new DefaultHttpCommandDispatcher(ReactorSelectorManager.getInstance());
        String port = cmd.getOptionValue("port");
        if (!StringUtils.isEmpty(port))
        {
            this.dispatcher.setPort(Short.valueOf(port));
        }
    }

    @Override
    public void onStart(INodeContext ctx) throws Exception
    {
        Collection<IHttpReactor> reactors = DefaultReactorHolder.getReactors();
        for (IHttpReactor reactor : reactors)
        {
            LOG.info(Module.HTTP_FRAMEWORK, "添加http Reactor,info:{}", reactor);
            this.dispatcher.addReactor(reactor);
        }
        DefaultHttpChannel instance = DefaultHttpChannel.getInstance();
        instance.setPipeline(WebHandlerManager.getInstance().getPipeline());
        ((DefaultHttpCommandDispatcher) this.dispatcher).setHttpChannel(instance);

        ((DefaultHttpCommandDispatcher) this.dispatcher).initOnce(null);

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
