package com.cell.postprocessor;

import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.command.IHttpCommand;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumHttpResponseType;
import com.cell.exceptions.ProgramaException;
import com.cell.reactor.IDynamicHttpReactor;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.ClassUtil;
import com.cell.utils.CollectionUtils;
import com.cell.utils.ReflectUtil;
import com.cell.utils.StringUtils;
import com.sun.webkit.network.URLs;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.print.DocFlavor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 07:58
 */
public class ReactorPostProcessor implements IBeanPostProcessortAdapter
{
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        if (bean instanceof IHttpCommandDispatcher)
        {
            DefaultReactorHolder.setDispatcher((IHttpCommandDispatcher) bean);
        } else if (bean instanceof IHttpReactor)
        {
            this.fillCmd((IHttpReactor) bean);
            if (bean instanceof IDynamicHttpReactor)
            {
            }
            DefaultReactorHolder.addReactor((IHttpReactor) bean);
        }
        return bean;
    }

    private void fillCmd(IHttpReactor reactor)
    {
        List<Class<? extends IHttpCommand>> httpCommandList = reactor.getHttpCommandList();
        ReactorAnno anno = (ReactorAnno) ClassUtil.mustGetAnnotation(reactor.getClass(), ReactorAnno.class);
        String group = anno.group();
        if (StringUtils.isEmpty(group)) return;
        httpCommandList.stream().forEach(c ->
        {
            HttpCmdAnno annotation = c.getAnnotation(HttpCmdAnno.class);
            if (annotation == null)
            {
                throw new ProgramaException("asd");
            }
            final String urlStr = group + "/" + annotation.uri();
            try
            {
                new URL(urlStr);
            } catch (MalformedURLException e)
            {
                throw new ProgramaException("url不合法:" + urlStr);
            }
            final HttpCmdAnno newAnno = new HttpCmdAnno()
            {
                @Override
                public EnumHttpRequestType requestType()
                {
                    return annotation.requestType();
                }

                @Override
                public EnumHttpResponseType responseType()
                {
                    return annotation.responseType();
                }

                @Override
                public String uri()
                {
                    return urlStr;
                }

                @Override
                public String viewName()
                {
                    return annotation.viewName();
                }

                @Override
                public String group()
                {
                    return annotation.group();
                }

                @Override
                public boolean websocket()
                {
                    return annotation.websocket();
                }

                @Override
                public Class<? extends Annotation> annotationType()
                {
                    return annotation.annotationType();
                }
            };
            ReflectUtil.overRideAnnotationOn(c, HttpCmdAnno.class, newAnno);
        });
    }

    // FIXME 这个或许不应该丢这?
    private Object proxyDymanicReactor(IHttpReactor reactor)
    {
        return reactor;
    }
}
