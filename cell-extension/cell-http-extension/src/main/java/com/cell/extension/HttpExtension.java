package com.cell.extension;

import com.cell.annotations.CellOrder;
import com.cell.annotations.Plugin;
import com.cell.constants.OrderConstants;
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
import com.cell.utils.StringUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

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
        this.dispatcher = new DefaultHttpCommandDispatcher();
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
        ((DefaultHttpCommandDispatcher) this.dispatcher).setTracker(CmdHookManager.getInstance().getHook());
        ((DefaultHttpCommandDispatcher) this.dispatcher).initOnce(null);

        this.dynamicControllerService.batchRegisterReactor(reactors);


        //        Collection<IDynamicHttpReactor> reactors = ReactorCache.getReactors();
//        for (IDynamicHttpReactor reactor : reactors)
//        {
//            this.dynamicControllerService.reigsterReactor(reactor);
//        }
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
