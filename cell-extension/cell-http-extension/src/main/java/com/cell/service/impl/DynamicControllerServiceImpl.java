package com.cell.service.impl;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.HttpCmdAnno;
import com.cell.command.IDynamicHttpCommand;
import com.cell.enums.EnumHttpRequestType;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.reactor.IDynamicHttpReactor;
import com.cell.service.IDynamicControllerService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
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

    private static final String executeMethod = "execute";


    @Override
    public void reigsterReactor(IDynamicHttpReactor reactor)
    {
        List<IDynamicHttpCommand> cmds = reactor.getCmds();
        try
        {
            for (IDynamicHttpCommand cmd : cmds)
            {
                this.registerCmd(cmd);
            }
        } catch (Exception e)
        {

        }

    }

    private void registerCmd(IDynamicHttpCommand cmd) throws Exception
    {
        HttpCmdAnno annotation = cmd.getClass().getAnnotation(HttpCmdAnno.class);
        if (null == annotation)
        {
            LOG.warn(Module.MVC, "未知的cmd,{}", cmd);
            return;
        }
        EnumHttpRequestType requestType = annotation.requestType();
        String uri = annotation.uri();
        Method method = cmd.getClass().getMethod(executeMethod);
        PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(uri);
        RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition(getRequestMethod(requestType));
        RequestMappingInfo mappingInfo = new RequestMappingInfo(patternsRequestCondition, requestMethodsRequestCondition, null, null, null, null, null);
        handlerMapping.registerMapping(mappingInfo, annotation.group(), method); // 注册映射处理
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
                throw new RuntimeException("asd");
        }
    }
}

