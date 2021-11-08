package com.cell.http.framework.util;

import com.cell.base.common.models.Module;
import com.cell.base.common.utils.JSONUtil;
import com.cell.base.core.annotations.ReactorAnno;
import com.cell.base.core.protocol.ICommand;
import com.cell.http.framework.command.IHttpCommand;
import com.cell.http.framework.reactor.IHttpReactor;
import com.cell.sdk.log.LOG;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 22:38
 */
public class HttpUtils
{
    public static String getIpAddress(HttpServletRequest request)
    {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0)
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0)
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0)
        {
            ip = request.getRemoteAddr();
        }
        if (ip != null)
        {
            String[] temp = ip.split(",");
            if (temp.length > 1)
            {
                ip = temp[0];
            }
        }
        return ip.trim();
    }

    public static String readStringFromPostRequest(HttpServletRequest request) throws IOException
    {
        int length = request.getContentLength();
        if (length <= 0)
        {
            LOG.debug(Module.HTTP_FRAMEWORK, "empty content received, len: [{}]", request.getContentLength());
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(length);
        StreamUtils.copy(request.getInputStream(), out);
        byte[] bytes = out.toByteArray();
        LOG.info(Module.HTTP_FRAMEWORK, "readStringFromRequest, len: [{}]", bytes.length);
        String message = new String(bytes, StandardCharsets.UTF_8);
        return message;
    }

    public static String fromHttpRequest(HttpServletRequest request) throws IOException
    {
        Map<String, Object> parameterMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements())
        {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            parameterMap.put(name, value);
        }
        return JSONUtil.toJsonString(parameterMap);
    }

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    public static Map<String, String> getRegexValues(String patten, String uri)
    {
        return MATCHER.extractUriTemplateVariables(patten, uri);
    }

    public static Optional<List<Class<? extends IHttpCommand>>> getReactorCommands(IHttpReactor reactor)
    {
        ReactorAnno annotation = reactor.getClass().getAnnotation(ReactorAnno.class);
        Class<? extends ICommand>[] cmds = annotation.cmds();
        if (cmds.length == 0) return Optional.empty();
        return Optional.of(Stream.of(cmds).map(c -> (Class<? extends IHttpCommand>) c).collect(Collectors.toList()));
    }

}
