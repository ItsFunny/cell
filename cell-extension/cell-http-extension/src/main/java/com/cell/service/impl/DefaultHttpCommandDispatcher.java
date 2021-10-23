//package com.cell.service.impl;
//
//import com.cell.annotation.HttpCmdAnno;
//import com.cell.annotations.ReactorAnno;
//import com.cell.channel.IHttpChannel;
//import com.cell.command.IHttpCommand;
//import com.cell.config.AbstractInitOnce;
//import com.cell.constant.HttpConstants;
//import com.cell.constants.ContextConstants;
//import com.cell.context.DefaultHttpHandlerSuit;
//import com.cell.context.InitCTX;
//import com.cell.dispatcher.IHttpCommandDispatcher;
//import com.cell.exception.HttpFramkeworkException;
//import com.cell.exceptions.ProgramaException;
//import com.cell.executor.IChainExecutor;
//import com.cell.executor.IReactorExecutor;
//import com.cell.log.LOG;
//import com.cell.manager.IReflectManager;
//import com.cell.manager.ReactorSelectorManager;
//import com.cell.manager.context.OnAddReactorContext;
//import com.cell.manager.context.SelectByUriContext;
//import com.cell.model.CommandWrapper;
//import com.cell.models.Module;
//import com.cell.protocol.CommandContext;
//import com.cell.protocol.ICommand;
//import com.cell.protocol.IContext;
//import com.cell.reactor.IHttpReactor;
//import com.cell.utils.ClassUtil;
//import lombok.Data;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.http.HttpStatus;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.context.request.async.DeferredResult;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.*;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-08-27 23:00
// */
//@Data
//public class DefaultHttpCommandDispatcher extends AbstractInitOnce implements IHttpCommandDispatcher, InitializingBean, ApplicationContextAware
//{
//    public DefaultHttpCommandDispatcher(IReflectManager<IReactorExecutor<IContext>, IChainExecutor<IContext>, IContext> selectorStrategy)
//    {
//        this.selectorStrategy = selectorStrategy;
//    }
//
//
//    private volatile boolean ready;
//    private short port = 8080;
//
//    private IHttpChannel httpChannel;
//
//
//    private Map<String, CommandWrapper> commands = new HashMap<>();
//
//    private AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//
//    // strategy
//    private IReflectManager<IReactorExecutor<IContext>, IChainExecutor<IContext>, IContext> selectorStrategy;
//
//
//    @Override
//    public short getPort()
//    {
//        return this.port;
//    }
//
//    @Override
//    public void setPort(short port)
//    {
//        this.port = port;
//    }
//
//    public long getResultTimeout()
//    {
//        return HttpConstants.DEFAULT_RESULT_TIME_OUT;
//    }
//
//    @Override
//    // TODO ,干掉responseBody
//    @ResponseBody
//    public DeferredResult<Object> request(HttpServletRequest request, HttpServletResponse response) throws HttpFramkeworkException
//    {
//        String command = request.getRequestURI();
//        CommandWrapper wp = this.commands.get(command);
//        if (wp == null)
//        {
//            SelectByUriContext ret = SelectByUriContext.builder().uri(command).build();
//            this.selectorStrategy.execute(ReactorSelectorManager.selectByUri, ret).subscribe();
//            if (ret.getRet() == null)
//            {
//                // should be internal server error
//                LOG.info(Module.HTTP_FRAMEWORK, "should not happen ,uri:{}", command);
//                return this.fastFail(response);
//            }
//            wp = new CommandWrapper();
//            wp.setCmd(ret.getRet().getV1());
//            wp.setReactor(ret.getRet().getV2());
//        }
//
//        long timeOut = wp.getReactor().getResultTimeout();
//        CommandContext context = new CommandContext(request, response, command);
//        DefaultHttpHandlerSuit ctx = new DefaultHttpHandlerSuit(this.httpChannel, context, wp.getReactor(), wp.getCmd());
//        this.httpChannel.readCommand(ctx);
//        return context.getResponseResult();
//    }
//
//    private DeferredResult<Object> fastFail(HttpServletResponse response)
//    {
//        response.addHeader(HttpConstants.HTTP_HEADER_CODE, String.valueOf(ContextConstants.FAIL));
//        response.addHeader(HttpConstants.HTTP_HEADER_MSG, "internal server error");
//        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        DeferredResult<Object> ret = new DeferredResult<>();
//        ret.setResult("error");
//        return ret;
//    }
//
//
//    @Override
//    public void addReactor(IHttpReactor reactor)
//    {
//        ReactorAnno annotation = reactor.getClass().getAnnotation(ReactorAnno.class);
//        if (annotation == null)
//        {
//            throw new ProgramaException("reactor annotation必须有 @ReactorAnno 注解 ");
//        }
//
//        List<Class<? extends ICommand>> clist = Arrays.asList(annotation.cmds());
//        String group = annotation.group();
//        for (Class<? extends ICommand> cc : clist)
//        {
//            HttpCmdAnno anno = (HttpCmdAnno) ClassUtil.getAnnotation(cc, HttpCmdAnno.class);
//
//            CommandWrapper wrapper = new CommandWrapper();
//            wrapper.setReactor(reactor);
//            wrapper.setCmd((Class<? extends IHttpCommand>) cc);
//            OnAddReactorContext ctx = OnAddReactorContext.builder()
//                    .anno(anno)
//                    .cmd((Class<? extends IHttpCommand>) cc)
//                    .reactor(reactor).build();
//            this.selectorStrategy.execute(ReactorSelectorManager.onAddReactor, ctx).subscribe();
//            if (!ctx.isSatisfy())
//            {
//                this.commands.put(anno.uri(), wrapper);
//            }
//        }
//    }
//
//
//    @Override
//    public boolean ready()
//    {
//        return ready;
//    }
//
//    // FIXME
//    @Override
//    public Collection<IHttpReactor> getReactors()
//    {
//        Set<IHttpReactor> set = new HashSet<>();
//        for (String s : commands.keySet())
//        {
//            set.add(commands.get(s).getReactor());
//        }
//        return new ArrayList<>(set);
//    }
//
//    @Override
//    protected void onInit(InitCTX ctx)
//    {
//        this.ready = true;
//    }
//
//
//    @Override
//    public void afterPropertiesSet() throws Exception
//    {
//
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
//    {
//
//    }
//}
