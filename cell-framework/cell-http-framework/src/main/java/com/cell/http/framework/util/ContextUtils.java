package com.cell.http.framework.util;

import com.cell.base.common.utils.StringUtils;
import com.cell.base.common.utils.UUIDUtils;
import com.cell.http.framework.couple.HttpRequestWrapper;
import com.cell.http.framework.couple.HttpResponseWrapper;
import com.cell.node.core.context.CellContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContextUtils {
    public static CellContext prepareContext(HttpServletRequest request, HttpServletResponse response) {
        CellContext context = CellContext.emptyContext();
        context.setRequest(new HttpRequestWrapper(request));
        context.setResponse(new HttpResponseWrapper(response));
        context.setProtocolId(HttpUtils.getRequestPath(request));
        context.setIp(HttpUtils.getIpAddress(request));
        String header = request.getHeader(CellContext.sequenceIdHeader);
        if (StringUtils.isEmpty(header)) {
            context.setSequenceId(UUIDUtils.generateSequenceId());
        } else {
            context.setSequenceId(header);
        }
        context.setMethod(request.getMethod());
        request.setAttribute(CellContext.CELL_CONTEXT, context);
        return context;
    }

    public static CellContext mustGetContext(HttpServletRequest request) {
        CellContext context = (CellContext) request.getAttribute(CellContext.CELL_CONTEXT);
        if (null == context) {
            throw new RuntimeException("framework error");
        }
        return context;
    }
}
