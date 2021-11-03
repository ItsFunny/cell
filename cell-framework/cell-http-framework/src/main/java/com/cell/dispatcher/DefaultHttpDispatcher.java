package com.cell.dispatcher;

import com.cell.base.common.annotation.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.channel.IChannel;
import com.cell.grpc.server.framework.command.IHttpCommand;
import com.cell.context.InitCTX;
import com.cell.couple.IHttpServerRequest;
import com.cell.exceptions.ProgramaException;
import com.cell.executor.IChainExecutor;
import com.cell.executor.IReactorExecutor;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.log.LOG;
import com.cell.manager.IReflectManager;
import com.cell.manager.ReactorSelectorManager;
import com.cell.manager.context.OnAddReactorContext;
import com.cell.manager.context.SelectByUriContext;
import com.cell.models.Module;
import com.cell.protocol.*;
import com.cell.reactor.ICommandReactor;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.ClassUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 23:39
 */
public class DefaultHttpDispatcher extends AbstractCommandDispatcher implements IHttpDispatcher
{
    public DefaultHttpDispatcher(IReflectManager<IReactorExecutor<IContext>, IChainExecutor<IContext>, IContext> selectorStrategy)
    {
        this.selectorStrategy = selectorStrategy;
    }

    // strategy
    private IReflectManager<IReactorExecutor<IContext>, IChainExecutor<IContext>, IContext> selectorStrategy;


    @Override
    protected CommandWrapper getCommandFromRequest(Map<String, CommandWrapper> commands, IServerRequest request)
    {
        IHttpServerRequest httpServerRequest = (IHttpServerRequest) request;
        String command = httpServerRequest.getInternalRequest().getRequestURI();
        CommandWrapper wp = commands.get(command);
        if (wp != null)
        {
            return wp;
        }

        SelectByUriContext ret = SelectByUriContext.builder().uri(command).build();
        this.selectorStrategy.execute(ReactorSelectorManager.selectByUri, ret).subscribe();
        if (ret.getRet() == null)
        {
            // should be internal server error
            LOG.info(Module.HTTP_FRAMEWORK, "should not happen ,uri:{}", command);
            return null;
        }
        wp = new CommandWrapper();
        wp.setCmd(ret.getRet().getV1());
        wp.setReactor(ret.getRet().getV2());
        return wp;
    }

    @Override
    protected ICommandSuit createSuit(IServerRequest request, IServerResponse response, IChannel<IHandler, IChainHandler> channel, CommandWrapper wp)
    {
        HttpCommandContext context = new HttpCommandContext(channel, request, response, wp);
        DefaultHttpHandlerSuit ctx = new DefaultHttpHandlerSuit(context, (IHttpReactor) wp.getReactor(), (Class<? extends IHttpCommand>) wp.getCmd());
        return ctx;
    }

    @Override
    protected void onAddReactor(Map<String, CommandWrapper> commands, ICommandReactor reactor)
    {
        ReactorAnno annotation = reactor.getClass().getAnnotation(ReactorAnno.class);
        if (annotation == null)
        {
            throw new ProgramaException("reactor annotation必须有 @ReactorAnno 注解 ");
        }

        List<Class<? extends ICommand>> clist = Arrays.asList(annotation.cmds());
        for (Class<? extends ICommand> cc : clist)
        {
            HttpCmdAnno anno = (HttpCmdAnno) ClassUtil.getAnnotation(cc, HttpCmdAnno.class);

            CommandWrapper wrapper = new CommandWrapper();
            wrapper.setReactor(reactor);
            wrapper.setCmd(cc);
            OnAddReactorContext ctx = OnAddReactorContext.builder()
                    .anno(anno)
                    .cmd((Class<? extends IHttpCommand>) cc)
                    .reactor((IHttpReactor) reactor).build();
            this.selectorStrategy.execute(ReactorSelectorManager.onAddReactor, ctx).subscribe();

            CommandProtocolID protocolID = new HttpUriProtocolId(anno.uri());
            if (!ctx.isSatisfy())
            {
                commands.put(protocolID.id(), wrapper);
            }
        }
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }

}
