package com.cell.plugin.develop.context;

import com.cell.base.common.utils.StringUtils;
import com.cell.base.common.utils.UUIDUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContextUtils
{
    public static CellContext prepareContext(HttpServletRequest request, HttpServletResponse response)
    {
        CellContext context = CellContext.emptyContext();
        context.setRequest(request);
        context.setResponse(response);
        context.setProtocolId(getRequestPath(request));
        String header = request.getHeader(CellContext.sequenceIdHeader);
        if (StringUtils.isEmpty(header))
        {
            context.setSequenceId(UUIDUtils.generateSequenceId());
        } else
        {
            context.setSequenceId(header);
        }
        context.setMethod(request.getMethod());
        request.setAttribute(CellContext.CELL_CONTEXT, context);
        return context;
    }

    public static String getRequestPath(HttpServletRequest request)
    {
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null)
        {
            url = org.springframework.util.StringUtils.hasLength(url) ? url + pathInfo : pathInfo;
        }
        return url;
    }

    public static CellContext mustGetContext(HttpServletRequest request)
    {
        CellContext context = (CellContext) request.getAttribute(CellContext.CELL_CONTEXT);
        if (null == context)
        {
            throw new RuntimeException("framework error");
        }
        return context;
    }
}
