package com.cell.http.framework.service.impl;

import com.cell.base.common.enums.EnumHttpRequestType;
import com.cell.base.common.exceptions.ProgramaException;
import com.cell.base.common.models.Module;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.base.core.annotations.AutoPlugin;
import com.cell.base.core.annotations.ReactorAnno;
import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.reactor.ICommandReactor;
import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.reactor.IDynamicHttpReactor;
import com.cell.http.framework.reactor.IMapDynamicHttpReactor;
import com.cell.http.framework.server.IHttpServer;
import com.cell.http.framework.service.IDynamicControllerService;
import com.cell.http.framework.reactor.IHttpReactor;
import com.cell.http.framework.command.IHttpCommand;
import com.cell.sdk.log.LOG;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
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
    @AutoPlugin
    private IHttpServer httpServer;

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
        ReactorAnno annotation = reactor.getClass().getAnnotation(ReactorAnno.class);
//        this.overrideReactor(reactor);

        List<Class<? extends ICommand>> httpCommandList = Arrays.asList(annotation.cmds());
        try
        {
            for (Class<? extends ICommand> aClass : httpCommandList)
            {
                this.registerCmd(annotation.group(), (Class<? extends IHttpCommand>) aClass);
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
            Set<String> names = reactor.getDependencyListByName();
            if (CollectionUtils.isEmpty(dependencies) && CollectionUtils.isEmpty(names)) return;
            if (CollectionUtils.isNotEmpty(dependencies))
            {
                for (Class<?> dependency : dependencies)
                {
                    Object bean = this.context.getBean(dependency);
                    reactor.registerDependency(dependency, bean);
                }
            }

            if (CollectionUtils.isNotEmpty(names))
            {
                for (String name : names)
                {
                    Object bean = this.context.getBean(name);
                    reactor.registerDependency(bean.getClass(), bean);
                }
            }
        } finally
        {
            this.registerHandlerReactor(reactor);
        }
    }

    @Override
    public void batchRegisterReactor(Collection<ICommandReactor> reactors)
    {
        for (ICommandReactor reactor : reactors)
        {
            this.registerReactor((IHttpReactor) reactor);
        }
    }

    private void registerCmd(String group, Class<? extends IHttpCommand> clz) throws Exception
    {
        HttpCmdAnno annotation = clz.getAnnotation(HttpCmdAnno.class);
        if (null == annotation)
        {
            LOG.warn(Module.MVC, "未知的cmd,{}", clz);
            return;
        }

        EnumHttpRequestType requestType = annotation.requestType();
        String uri = annotation.uri();

        Method method = this.httpServer.getClass().getMethod(requestMethod, HttpServletRequest.class, HttpServletResponse.class);
//        Method method = DefaultReactorHolder.getInstance().getClass().getMethod(requestMethod, HttpServletRequest.class, HttpServletResponse.class);
        RequestMappingInfo mappingInfo = RequestMappingInfo.paths(uri).methods(getRequestMethod(requestType)).build();
        LOG.info(Module.HTTP_FRAMEWORK, "注册uri:{},method:{}", uri, annotation.requestType().name());
        handlerMapping.registerMapping(mappingInfo, this.httpServer, method); // 注册映射处理
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

