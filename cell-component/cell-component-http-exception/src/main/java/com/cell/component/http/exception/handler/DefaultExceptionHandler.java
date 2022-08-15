package com.cell.component.http.exception.handler;

import com.cell.base.common.dto.ResultDTO;
import com.cell.base.common.dto.ResultUtils;
import com.cell.base.common.models.Module;
import com.cell.base.common.utils.JSONUtil;
import com.cell.component.http.exception.exception.BusinessException;
import com.cell.component.http.exception.exception.ErrorConstant;
import com.cell.component.http.exception.exception.WrapContextException;
import com.cell.component.http.filter.ICellExceptionHandler;
import com.cell.http.framework.couple.HttpResponseWrapper;
import com.cell.node.core.context.CellContext;
import com.cell.sdk.log.LOG;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class DefaultExceptionHandler implements ICellExceptionHandler
{
    @Override
    public void handleException(CellContext context, Throwable e) throws IOException
    {
        ResultDTO<Object> fail = null;
        if (e instanceof WrapContextException)
        {
            e = ((WrapContextException) e).getException();
        }
        if (e instanceof BusinessException)
        {
            String code = ((BusinessException) e).getCode();
            fail = ResultUtils.fail(Integer.parseInt(code), e.getMessage(), null);
        }
        {
            fail = ResultUtils.fail(ErrorConstant.AUTH_FAILED, null);
        }
        LOG.error(Module.EXCEPTION_HANDLER, e, "调用发生错误,sequenceId:{},", context.getSequenceId());
        HttpResponseWrapper wrapper = (HttpResponseWrapper) context.getResponse();
        wrapper.getInternalResponse().setContentType("text/html;charset=utf-8");
        wrapper.getInternalResponse().getWriter().write(JSONUtil.obj2Json(fail));
    }
}
