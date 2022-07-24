package com.cell.http.framework.util;

import com.cell.base.common.utils.UUIDUtils;
import com.cell.http.framework.couple.HttpRequestWrapper;
import com.cell.http.framework.couple.HttpResponseWrapper;
import com.cell.node.core.context.CellContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContextUtils
{
    public static CellContext prepareContext(HttpServletRequest request, HttpServletResponse response)
    {
        CellContext context = CellContext.builder().request(new HttpRequestWrapper(request)).response(new HttpResponseWrapper(response)).build();
        context.setProtocolId(HttpUtils.getRequestPath(request));
        context.setSequenceId(UUIDUtils.generateSequenceId());
        context.setMethod(request.getMethod());
        request.setAttribute(CellContext.CELL_CONTEXT, context);
        return context;
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
