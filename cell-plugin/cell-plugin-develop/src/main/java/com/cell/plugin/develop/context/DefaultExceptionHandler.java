package com.cell.plugin.develop.context;

import com.cell.base.common.utils.JSONUtil;
import com.cell.sdk.log.LOG;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DefaultExceptionHandler implements ICellExceptionHandler
{

    @Override
    public void handleException(CellContext cellContext, Throwable throwable) throws IOException
    {
        if (cellContext == null)
        {
            return;
        }
        HttpServletResponse resp = cellContext.getResponse();
        logException(throwable);
        resp.getWriter().write(JSONUtil.obj2Json(ResultUtils.fail(400, throwable.getMessage(), null)));
    }

    private void logException(Throwable e)
    {
        LOG.error(ModuleEnums.PUFF, e, "err");
    }
}
