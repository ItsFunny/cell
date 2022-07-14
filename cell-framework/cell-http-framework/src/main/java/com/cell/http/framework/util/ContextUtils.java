package com.cell.http.framework.util;

import com.cell.base.common.utils.UUIDUtils;
import com.cell.http.framework.context.CellContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class ContextUtils
{
    public static CellContext prepareContext(HttpServletRequest request, HttpServletResponse response)
    {
        CellContext context = CellContext.builder().request(request).response(response).build();
        context.setProtocolId(HttpUtils.getRequestPath(request));
        context.setSequenceId(UUIDUtils.generateSequenceId());
        context.setMethod(request.getMethod());
        request.setAttribute(CellContext.CELL_CONTEXT, context);
        return context;
    }
}
