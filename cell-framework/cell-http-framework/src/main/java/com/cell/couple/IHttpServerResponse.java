package com.cell.couple;

import com.cell.base.core.protocol.IServerResponse;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 00:31
 */
public interface IHttpServerResponse extends IServerResponse
{
    HttpServletResponse getInternalResponse();

    void setDeferredResponse(DeferredResult<Object> response);
}
