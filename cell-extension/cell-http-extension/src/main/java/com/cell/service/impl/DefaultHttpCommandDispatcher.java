package com.cell.service.impl;

import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.channel.IHttpChannel;
import com.cell.command.IHttpCommand;
import com.cell.config.AbstractInitOnce;
import com.cell.constant.HttpConstants;
import com.cell.context.DefaultHttpHandlerSuit;
import com.cell.context.InitCTX;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.exception.HttpFramkeworkException;
import com.cell.exceptions.ProgramaException;
import com.cell.protocol.CommandContext;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.ClassUtil;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 23:00
 */
@Data
public class DefaultHttpCommandDispatcher extends AbstractInitOnce implements IHttpCommandDispatcher, InitializingBean, ApplicationContextAware
{
    private volatile boolean ready;
    private short port = 8080;

    private IHttpChannel httpChannel;

    private Map<String, IHttpReactor> reactorMap = new HashMap<>();

    @Override
    public short getPort()
    {
        return this.port;
    }

    @Override
    public void setPort(short port)
    {
        this.port = port;
    }

    public long getResultTimeout()
    {
        return HttpConstants.DEFAULT_RESULT_TIME_OUT;
    }

    @Override
    // TODO ,干掉responseBody
    @ResponseBody
    public DeferredResult<Object> request(HttpServletRequest request, HttpServletResponse response) throws HttpFramkeworkException
    {
        String command = request.getRequestURI();
        IHttpReactor reactor = this.getReactor(command);
        long timeOut = reactor == null ? this.getResultTimeout() : reactor.getResultTimeout();
        CommandContext context = new CommandContext(request, response, timeOut, command);
        DefaultHttpHandlerSuit ctx = new DefaultHttpHandlerSuit(this.httpChannel, context, reactor);
        this.httpChannel.readCommand(ctx);
        return context.getResponseResult();
    }


    @Override
    public void addReactor(IHttpReactor reactor)
    {
        ReactorAnno annotation = reactor.getClass().getAnnotation(ReactorAnno.class);
        if (annotation == null)
        {
            throw new ProgramaException("reactor annotation必须有 @ReactorAnno 注解 ");
        }
        List<Class<? extends IHttpCommand>> clist = reactor.getHttpCommandList();
        for (Class<? extends IHttpCommand> cc : clist)
        {
            HttpCmdAnno anno = (HttpCmdAnno) ClassUtil.getAnnotation(cc, HttpCmdAnno.class);
            this.reactorMap.put(anno.uri(), reactor);
        }
    }


    private IHttpReactor getReactor(String uri)
    {
        return this.reactorMap.get(uri);
    }

    @Override
    public boolean ready()
    {
        return ready;
    }

    // FIXME
    @Override
    public Map<String, IHttpReactor> getReactors()
    {
        return this.reactorMap;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.ready = true;
    }


    @Override
    public void afterPropertiesSet() throws Exception
    {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {

    }
}
