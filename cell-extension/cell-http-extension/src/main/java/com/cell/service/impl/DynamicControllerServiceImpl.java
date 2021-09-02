package com.cell.service.impl;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.HttpCmdAnno;
import com.cell.command.IDynamicHttpCommand;
import com.cell.command.IHttpCommand;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.enums.EnumHttpRequestType;
import com.cell.exceptions.ProgramaException;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.reactor.IDynamicHttpReactor;
import com.cell.reactor.IHttpReactor;
import com.cell.service.IDynamicControllerService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:19
 */
public class DynamicControllerServiceImpl implements IDynamicControllerService
{
    @AutoPlugin
    private RequestMappingHandlerMapping handlerMapping;


    private static final String requestMethod = "request";

    static class A extends AbstractController
    {
        @Override
        protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception
        {
            return null;
        }
    }

    @Override
    public void registerReactor(IHttpReactor reactor)
    {
        if (!IDynamicHttpReactor.class.isAssignableFrom(reactor.getClass()))
        {
            return;
        }
        List<Class<? extends IHttpCommand>> httpCommandList = reactor.getHttpCommandList();
        try
        {
            for (Class<? extends IHttpCommand> aClass : httpCommandList)
            {
                this.registerCmd(aClass);
            }
        } catch (Exception e)
        {
            throw new ProgramaException(e);
        }
    }

    @Override
    public void batchRegisterReactor(Collection<IHttpReactor> reactors)
    {
        for (IHttpReactor reactor : reactors)
        {
            this.registerReactor(reactor);
        }
    }

    private void registerCmd(Class<? extends IHttpCommand> clz) throws Exception
    {
        HttpCmdAnno annotation = clz.getAnnotation(HttpCmdAnno.class);
        if (null == annotation)
        {
            LOG.warn(Module.MVC, "未知的cmd,{}", clz);
            return;
        }
        EnumHttpRequestType requestType = annotation.requestType();
        String uri = annotation.uri();
        Method method = DefaultReactorHolder.getInstance().getClass().getMethod(requestMethod, HttpServletRequest.class, HttpServletResponse.class);
        RequestMappingInfo mappingInfo = RequestMappingInfo.paths(uri).methods(getRequestMethod(requestType)).build();
        handlerMapping.registerMapping(mappingInfo, DefaultReactorHolder.getInstance(), method); // 注册映射处理
    }

    public RequestMethod getRequestMethod(EnumHttpRequestType requestType)
    {
        switch (requestType)
        {
            case HTTP_URL_GET:
                return RequestMethod.GET;
            case HTTP_POST:
                return RequestMethod.POST;
            default:
                throw new ProgramaException("asd");
        }
    }

}

