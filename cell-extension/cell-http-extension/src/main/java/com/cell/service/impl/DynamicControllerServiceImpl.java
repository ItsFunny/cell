package com.cell.service.impl;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.HttpCmdAnno;
import com.cell.command.IHttpCommand;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.enums.EnumHttpRequestType;
import com.cell.exceptions.ProgramaException;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.reactor.IDynamicHttpReactor;
import com.cell.reactor.IHttpReactor;
import com.cell.reactor.IMapDynamicHttpReactor;
import com.cell.service.IDynamicControllerService;
import com.cell.utils.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:19
 */
public class DynamicControllerServiceImpl implements IDynamicControllerService, ApplicationContextAware
{
    @AutoPlugin
    private RequestMappingHandlerMapping handlerMapping;

    private ApplicationContext context;


    private static final String requestMethod = "request";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.context = applicationContext;
    }


    @Override
    public void registerReactor(IHttpReactor reactor)
    {
        if (IMapDynamicHttpReactor.class.isAssignableFrom(reactor.getClass()))
        {
            this.fillDependency((IMapDynamicHttpReactor) reactor);
            return;
        }
        if (!IDynamicHttpReactor.class.isAssignableFrom(reactor.getClass()))
        {
            return;
        }
        this.registerHandlerReactor((IDynamicHttpReactor) reactor);
    }

    private void registerHandlerReactor(IDynamicHttpReactor reactor)
    {
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

    private void fillDependency(IMapDynamicHttpReactor reactor)
    {
        try
        {
            Set<Class<?>> dependencies = reactor.getDependencyList();
            if (CollectionUtils.isEmpty(dependencies)) return;
            for (Class<?> dependency : dependencies)
            {
                Object bean = this.context.getBean(dependency);
                reactor.registerDependency(dependency, bean);
            }
        } finally
        {
            this.registerHandlerReactor(reactor);
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

